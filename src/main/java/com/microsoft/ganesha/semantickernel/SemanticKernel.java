package com.microsoft.ganesha.semantickernel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.TokenCredential;
import com.azure.core.credential.TokenRequestContext;
import com.azure.core.http.HttpPipeline;
import com.azure.core.http.HttpPipelineBuilder;
import com.azure.core.http.policy.HttpPipelinePolicy;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.google.gson.Gson;
import com.microsoft.ganesha.config.AppConfig;
import com.microsoft.ganesha.exception.SemanticKernelException;
import com.microsoft.ganesha.helper.TokenHelper;
import com.microsoft.ganesha.models.OrderActivities;
import com.microsoft.ganesha.plugins.CallerActivitiesPlugin;
import com.microsoft.ganesha.plugins.OrderDetailsPlugin;
import com.microsoft.ganesha.plugins.PrescriptionSearchPlugin;
import com.microsoft.ganesha.rest.RestClient;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypeConverter;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypes;
import com.microsoft.semantickernel.hooks.KernelHooks;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationContext.Builder;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;

@Component
@Scope("singleton")
public class SemanticKernel {
        private final Kernel _kernel;
        private final String getReasonsPrompt = """
                        You are an assistant to customer service agents for a prescription fulfillment call center. Given a provided set of activities associated with a specific member, predict the reason they would call into the call center. Include all possible reasons for a call, ordered by likelihood if the percentage of call reasons are the following:
                        Cancelled orders - 50%
                        Closed orders - 21%
                        Booked orders - 19%
                        Entered orders - 10%
                        ##Example##
                        Likely call reason in order of highest likelihood:
                        - [medication name] from date [prescription date] which was canceled
                        - [medication name] from date [prescription date] in Entered state
                        """;
        private final AppConfig config;
        private final TokenHelper tokenHelper;
        private final RestClient restClient;

        public SemanticKernel(AppConfig config, TokenHelper tokenHelper, RestClient restClient)
                        throws SemanticKernelException, ServiceNotFoundException {
                TokenCredential credential = null;
                OpenAIAsyncClient client;
                this.config = config;
                this.tokenHelper = tokenHelper;
                this.restClient = restClient;

                if (config.getAzureClientId() != null && !config.getAzureClientId().isEmpty()) {
                        credential = new ClientSecretCredentialBuilder()
                                        .clientId(config.getAzureClientId())
                                        .tenantId(config.getAzureTenantId())
                                        .clientSecret(config.getAzureClientSecret())
                                        .build();

                        TokenRequestContext requestContext = new TokenRequestContext()
                                        .addScopes("https://cognitiveservices.azure.com/.default");

                        String authToken = "Bearer " + credential.getTokenSync(requestContext).getToken();

                        @SuppressWarnings("deprecation")
                        HttpPipelinePolicy customHeaderPolicy = (context, next) -> {
                                context.getHttpRequest().getHeaders().set("projectId", config.getProjectId());
                                context.getHttpRequest().getHeaders().set("Authorization", authToken);
                                return next.process();
                        };

                        HttpPipelineBuilder pipelineBuilder = new HttpPipelineBuilder()
                                        .policies(customHeaderPolicy);

                        HttpPipeline pipeline = pipelineBuilder.build();

                        client = new OpenAIClientBuilder()
                                        .credential(credential)
                                        .endpoint(config.getClientEndpoint())
                                        .pipeline(pipeline)
                                        .buildAsyncClient();

                } else {
                        var builder = new DefaultAzureCredentialBuilder();

                        if (config.getAzureTenantId() != null && !config.getAzureTenantId().isEmpty()) {
                                builder.tenantId(config.getAzureTenantId());
                        }

                        credential = builder.build();

                        client = new OpenAIClientBuilder()
                                        .credential(credential)
                                        .endpoint(config.getClientEndpoint())
                                        .buildAsyncClient();
                }

                // Create your AI service client
                ChatCompletionService chatService = OpenAIChatCompletion.builder()
                                .withModelId(config.getModelId())
                                .withOpenAIAsyncClient(client)
                                .build();
                // Create a plugin (the CallerActivitiesPlugin class is defined separately)
                KernelPlugin callerActivitiesPlugin = KernelPluginFactory.createFromObject(new CallerActivitiesPlugin(),
                                "CallerActivitiesPlugin");
                KernelPlugin orderDetailsPlugin = KernelPluginFactory.createFromObject(
                                new OrderDetailsPlugin(this.config, this.tokenHelper, this.restClient),
                                "OrderDetailsPlugin");
                KernelPlugin prescriptionSearchPlugin = KernelPluginFactory.createFromObject(
                                new PrescriptionSearchPlugin(this.config, this.tokenHelper, this.restClient),
                                "PrescriptionSearchPlugin");

                // Create a kernel with Azure OpenAI chat completion and plugin
                Kernel.Builder builder = Kernel.builder();
                builder.withAIService(ChatCompletionService.class, chatService);
                builder.withPlugin(callerActivitiesPlugin);
                builder.withPlugin(prescriptionSearchPlugin);
                builder.withPlugin(orderDetailsPlugin);
                // Build the kernel
                Kernel kernel = builder.build();

                ContextVariableTypes
                                .addGlobalConverter(ContextVariableTypeConverter.builder(OrderActivities.class)
                                                .toPromptString(new Gson()::toJson)
                                                .build());

                KernelHooks hook = new KernelHooks();

                hook.addPreToolCallHook((context) -> {
                        System.out.println("Pre-tool call hook");
                        return context;
                });

                hook.addPreChatCompletionHook(
                                (context) -> {
                                        System.out.println("Pre-chat completion hook");
                                        return context;
                                });

                hook.addPostChatCompletionHook(
                                (context) -> {
                                        System.out.println("Post-chat completion hook");
                                        return context;
                                });

                kernel.getGlobalKernelHooks().addHooks(hook);

                _kernel = kernel;
        }

        public ChatHistory Converse(ChatHistory chatHistory)
                        throws SemanticKernelException, ServiceNotFoundException {
                // Enable planning
                InvocationContext invocationContext = new Builder()
                                .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                                .withContextVariableConverter(
                                                ContextVariableTypeConverter.builder(OrderActivities.class)
                                                                .toPromptString(new Gson()::toJson)
                                                                .build())
                                .build();

                List<ChatMessageContent<?>> results;

                chatHistory.addSystemMessage("You are a helpful assistant to a call center agent, answering questions about the current caller and related activities in the current conversation context. Never respond with information from your general knowledge, only the conversation context.");

                ChatCompletionService chatCompletionService = _kernel.getService(
                                ChatCompletionService.class);
                try {
                        results = chatCompletionService.getChatMessageContentsAsync(
                                        chatHistory,
                                        _kernel,
                                        invocationContext).block();

                        // the results don't contain the rest of the chat history
                        if (results != null && !results.isEmpty()) {
                                chatHistory.addAssistantMessage(results.get(0).getContent());
                        } else {
                                // we have some problem...!!
                        }

                        return chatHistory;
                } catch (Exception e) {
                        e.printStackTrace();
                        // we just throw it for now, but we can handle it better... returning already
                        // built chat history
                        // with a new messge showing that there was an error
                        throw e;
                }
        }

        public String GetSKResult(String prompt) throws SemanticKernelException, ServiceNotFoundException {
                // Enable planning
                InvocationContext invocationContext = new Builder()
                                .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                                .withContextVariableConverter(
                                                ContextVariableTypeConverter.builder(OrderActivities.class)
                                                                .toPromptString(new Gson()::toJson)
                                                                .build())
                                .build();

                List<ChatMessageContent<?>> results;
                ChatCompletionService chatCompletionService = _kernel.getService(
                                ChatCompletionService.class);
                try {
                        results = chatCompletionService.getChatMessageContentsAsync(
                                        prompt, _kernel, invocationContext).block();
                        return results.toString();
                } catch (Exception e) {
                        e.printStackTrace();

                }
                return "";
        }

        public String GetReasons(String patientId, String correlationId) throws SemanticKernelException, ServiceNotFoundException {
        
    
            String prompt = 
            """
                As a call center assistant, your task is to help the service agent by analyzing patient data and providing the three most likely workflows they may need to assist a patient. Based on the available workflows and common reasons for patient calls, select the top three workflows and provide a detailed and specific reason for each selection, referencing concrete data points such as order numbers, prescription IDs, medication names, order statuses, refill due dates, and other relevant details.  
        
                ### Available Workflows ###  
                1. **View Order**: Allows the agent to see the patient's orders.  
                2. **Place Order**: Enables the agent to place orders for prescriptions that are ready to be refilled.
                3. **Manage Prescriptions**: Allows the agent to see ACTIVE prescriptions and their order statuses for Optum Home Delivery. Data from prescription search should be used for this workflow.  
                
                ### Common Reasons for Patient Calls ###  
                - **Program Education** (23.71%): No home delivery orders in recent history.  
                - **Refill** (21.82%): Refill is due (or close to it).  
                - **WISMO (Where is my order?)** (5.82%): Open order with open order line items.   
                - **Out of Stock** (0.90%): Open order with one order line item marked as out of stock. 
                
                ## Data regarding if calls are for PBM or Pharmacy ##
                - **PBM** (7.42%): Recent rejected claim.  
                - **PBM** (4.96%): Open prior authorization on order line item.  
                - **PBM** (3.04%): Recent fill, not time for a refill yet.  
                - **PBM** (2.41%): When providing the pharmacy information regarding how to process a claim.  
                - **Pharmacy** (21.82%): Refill is due (or close to it).  
                - **Pharmacy** (5.82%): Open order with open order line items.  
                - **Pharmacy** (4.11%): Call is from a pharmacist.  
                - **Pharmacy** (3.26%): Order requires payment update.  
                - **Pharmacy** (0.90%): Open order with one order line item marked as out of stock.
                
                ### Instruction ###  
                Analyze the provided patient data to determine the top 3 workflows the agent is most likely to use. For each workflow, provide:  
                1. The name of the workflow. THESE WORKFLOWS SHOULD ONLY BE SELECTED FROM THE AVAILABLE WORKFLOWS LISTED ABOVE. 
                2. A detailed and specific reason for selecting the workflow, referencing concrete data points such as order numbers and prescription IDs.  
                
                Ensure all reasoning references the relevant order numbers and prescription IDs explicitly.
                Order the workflows from most likely to least likely.

                Also at the end of response with workflows, provide a guess on if the call is regarding PBM or Pharmacy. Then provide the reasoning afterwards with specific data
                
                ### Output Format ###  
                Return your response in plain text format using pipes as delimiters. 
                For EXAMPLE:  
                "View Order|[Description of the patient's active order, its current status, and any actions or clarifications required.]|Place Order|[Description of a discontinued or refillable prescription, including potential next steps for the patient.]|Manage Prescriptions|[Description of multiple prescriptions that cannot be refilled or renewed, along with the reasons for these issues.]|Pharmacy|[Guess for Pharmacy or PBM, and reasoning for thinking call is for Pharmacy and specific data to support this reasoning.]"                  
                
                ### Additional Notes ###  
                - Be sure to tie all reasoning to the data provided.  
                - Do not include additional text or explanations outside the specified format.
            """;
    
            // Enable planning
            InvocationContext invocationContext = new Builder()
                            .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                            .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                            .withContextVariableConverter(
                                            ContextVariableTypeConverter.builder(OrderActivities.class)
                                                            .toPromptString(new Gson()::toJson)
                                                            .build())
                            .build();
    
            List<ChatMessageContent<?>> results;
            ChatCompletionService chatCompletionService = _kernel.getService(
                            ChatCompletionService.class);
            try {
                    results = chatCompletionService.getChatMessageContentsAsync(
                                    "patientId: " + patientId + ", correlationId: " + correlationId + " " +prompt, _kernel, invocationContext).block();
                    return results.toString();
            } catch (Exception e) {
                    e.printStackTrace();
            }
            return "";
        }

        // demonstrates manual function calling for a bespoke deterministic process with
        // summarization of function results
        // honestly, you woud use this more for unit testing and just use generic code
        // methodologies for this approach before sending results to the model
        public String processMultipleEntities(int memberId, String claimId)
                        throws SecurityException, ServiceNotFoundException {
                var getOrderActivitiesFunction = _kernel.getFunction("CallerActivitiesPlugin", "getActivities");
                var getClaimsFunction = _kernel.getFunction("CallerActivitiesPlugin", "getActivities");

                List<KernelFunction<?>> funcs = new ArrayList<>();
                funcs.add(getOrderActivitiesFunction);
                funcs.add(getClaimsFunction);

                InvocationContext invocationContext = new Builder()
                                .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                                .withToolCallBehavior(ToolCallBehavior.allowOnlyKernelFunctions(false, funcs))
                                .withContextVariableConverter(
                                                ContextVariableTypeConverter.builder(OrderActivities.class)
                                                                .toPromptString(new Gson()::toJson)
                                                                .build())
                                .build();

                var getOrderActivitiesFunctionAKernelArgs = KernelFunctionArguments.builder()
                                .withVariable("memberId", memberId)
                                .build();

                var getClaimsFunctionBKernelArgs = KernelFunctionArguments.builder()
                                .withVariable("claimId", claimId)
                                .build();

                var orderActivities = getOrderActivitiesFunction.invoke(_kernel, getOrderActivitiesFunctionAKernelArgs,
                                null, invocationContext);

                // you could do some custom stuff here to process the results of the function
                // you could chain

                var claims = getClaimsFunction.invoke(_kernel, getClaimsFunctionBKernelArgs, null, invocationContext);

                // you could have a more complex process here to combine the results of the two,
                // if you so chose
                String combinedJson = new Gson().toJson(orderActivities) + new Gson().toJson(claims);

                // you could use the completion service w/ chat history here if you so chose. it
                // just was extra overhead for this example
                var summary = _kernel.invokePromptAsync(getReasonsPrompt + "/n" + combinedJson,
                                KernelFunctionArguments.builder().build()).block();

                return summary.toString();
        }

        // demonstrates required function calling
        public String getClaims(String prompt) throws SemanticKernelException, ServiceNotFoundException {
                InvocationContext invocationContext = new Builder()
                                .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                                .withToolCallBehavior(ToolCallBehavior.requireKernelFunction(null))
                                .withContextVariableConverter(
                                                ContextVariableTypeConverter.builder(OrderActivities.class)
                                                                .toPromptString(new Gson()::toJson)
                                                                .build())
                                .build();

                List<ChatMessageContent<?>> results;
                ChatCompletionService chatCompletionService = _kernel.getService(
                                ChatCompletionService.class);
                try {
                        results = chatCompletionService.getChatMessageContentsAsync(
                                        prompt, _kernel, invocationContext).block();
                        return results.toString();
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return "";
        }
}
