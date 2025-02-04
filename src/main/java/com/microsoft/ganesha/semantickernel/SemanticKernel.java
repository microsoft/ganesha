package com.microsoft.ganesha.semantickernel;

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
import com.microsoft.ganesha.plugins.CallerActivitiesPlugin;
import com.microsoft.ganesha.plugins.OrderActivities;
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
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;

@Component
@Scope("singleton")
public class SemanticKernel {
        private final AppConfig config;
        private final TokenHelper tokenHelper;
        private final RestClient restClient;

        public SemanticKernel(AppConfig config, TokenHelper tokenHelper, RestClient restClient) {
                this.config = config;
                this.tokenHelper = tokenHelper;
                this.restClient = restClient;
        }

        public ChatHistory Converse(ChatHistory chatHistory)
                        throws SemanticKernelException, ServiceNotFoundException {
                Kernel kernel = InstantiateKernel();

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

                chatHistory.addSystemMessage("You are a goofball who makes up zany one-liners.");

                ChatCompletionService chatCompletionService = kernel.getService(
                                ChatCompletionService.class);
                try {
                        results = chatCompletionService.getChatMessageContentsAsync(
                                        chatHistory,
                                        kernel,
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

                Kernel kernel = InstantiateKernel();

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
                ChatCompletionService chatCompletionService = kernel.getService(
                                ChatCompletionService.class);
                try {
                        results = chatCompletionService.getChatMessageContentsAsync(
                                        prompt, kernel, invocationContext).block();
                        return results.toString();
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return "";
        }

        public String GetReasons(String patientId, String correlationId) throws SemanticKernelException, ServiceNotFoundException {

                Kernel kernel = InstantiateKernel();

                String prompt = 
                """                  
                        As a call center assistant, your task is to help the service agent by analyzing patient data and providing the three most likely workflows they may need to assist a patient. You must choose workflows based on the following list and explain the reasoning behind each selection. 
                        
                        patientId = {{patientId}}
                        correlationId = {{correlationId}}
                           
                        #### Available Workflows ####  
                        1. **View Order**: This workflow allows the agent to see the patient's orders.    
                        2. **Place Order**: This workflow enables the agent to place new orders or refill existing ones.    
                        3. **Manage Prescriptions**: This workflow allows the agent to manage both active and inactive prescriptions.    
                          
                        #### Common Reasons for Patient Calls ####  
                        Based on historical call data, patients contact the call center for the following reasons. Each reason includes its likelihood (percentage of calls) and an indicator to identify the reason from the data provided:    
                        - **Program Education** (23.71%): No home delivery orders in recent history.    
                        - **Refill** (21.82%): Refill is due (or close to it).    
                        - **WISMO (Where is my order?)** (5.82%): Open order with open order line items.    
                        - **Pharmacist** (4.11%): Call is from a pharmacist.    
                        - **Web Portal Help** (1.06%): No identifiable reason.    
                        - **Out of Stock** (0.90%): Open order with one order line item marked as out of stock.    
                          
                        ### Instruction ###  
                        Analyze the provided patient data (e.g., order details, prescription history) to determine the top 3 workflows the agent is most likely to use. For each workflow, provide:  
                        1. The name of the workflow.  
                        2. A detailed and specific reason for selecting the workflow, referencing **concrete data points** such as order numbers, prescription IDs, medication names, order statuses, refill due dates, and other relevant details.  
                        
                        Your reasoning must include **specific examples** using the data provided. For example:  
                        - "Place Order is most likely to be used because the patient's recent order of Advil (Order ID: A001) was canceled on 2023-10-01, and they may want to reorder it."  
                        - "View Order is most likely to be used because the patient has an active order (Order ID: A002) for Tylenol that is scheduled for delivery tomorrow."  
                        - "Manage Prescriptions is most likely to be used because the patient has an inactive prescription for Lipitor (Prescription ID: P123) that needs renewal."  
                        
                        Ensure all reasoning references the relevant **order numbers** and **prescription IDs** explicitly.  
                        
                        ### Output Format ###  
                        Return your response ONLY in the following JSON format:  
                        {  
                        "Workflows": [  
                        {  
                        "workflow": "[workflow name]",  
                        "Reason": "[detailed and specific reason referencing concrete data points, including order numbers and prescription IDs]"  
                        },  
                        {  
                        "workflow": "[workflow name]",  
                        "Reason": "[detailed and specific reason referencing concrete data points, including order numbers and prescription IDs]"  
                        },  
                        {  
                        "workflow": "[workflow name]",  
                        "Reason": "[detailed and specific reason referencing concrete data points, including order numbers and prescription IDs]"  
                        }  
                        ]  
                        }   
                           
                        #### Example Data Analysis ####  
                        - If no home delivery orders exist in the patient's history, "Program Education" is likely the reason for the call, and the "View Order" workflow may be helpful.    
                        - If a refill is due or close to due, prioritize the "Place Order" workflow.    
                        - If the patient has an open order with pending line items, "WISMO" is likely the reason, and "View Order" may be the most relevant workflow.    
                          
                        #### Additional Notes ####  
                        - Be sure to tie all reasoning to the data provided.    
                        - Adhere strictly to the JSON output format; do not include additional text or explanations outside the JSON object.    

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
                ChatCompletionService chatCompletionService = kernel.getService(
                                ChatCompletionService.class);
                try {
                        results = chatCompletionService.getChatMessageContentsAsync(
                                        prompt, kernel, invocationContext).block();
                        return results.toString();
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return "";
        }

        private Kernel InstantiateKernel() throws SemanticKernelException {
                TokenCredential credential = null;
                OpenAIAsyncClient client;

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
                // KernelPlugin callerActivitiesPlugin = KernelPluginFactory.createFromObject(new CallerActivitiesPlugin(),
                //                 "CallerActivitiesPlugin");
                KernelPlugin orderDetailsPlugin = KernelPluginFactory.createFromObject(
                                new OrderDetailsPlugin(this.config, this.tokenHelper, this.restClient),
                                "OrderDetailsPlugin");
                KernelPlugin prescriptionSearchPlugin = KernelPluginFactory.createFromObject(
                                new PrescriptionSearchPlugin(this.config, this.tokenHelper, this.restClient),
                                "PrescriptionSearchPlugin");

                // Create a kernel with Azure OpenAI chat completion and plugin
                Kernel.Builder builder = Kernel.builder();
                builder.withAIService(ChatCompletionService.class, chatService);
                // builder.withPlugin(callerActivitiesPlugin);
                builder.withPlugin(orderDetailsPlugin);
                builder.withPlugin(prescriptionSearchPlugin);
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

                return kernel;
        }

}