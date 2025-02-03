package com.microsoft.ganesha.semantickernel;

import java.util.List;

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
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;

public class SemanticKernel {

    private final AppConfig config;
    private final TokenHelper tokenHelper;
    private final RestClient restClient;
    

    public SemanticKernel(AppConfig config, TokenHelper tokenHelper, RestClient restClient) {
        this.config = config;
        this.tokenHelper = tokenHelper;
        this.restClient = restClient;
    }
    

    public String GetSKResult(String prompt) throws SemanticKernelException, ServiceNotFoundException {
       
        Kernel kernel = InstantiateKernel();

        // Enable planning
        InvocationContext invocationContext = new Builder()
            .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
            .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
            .withContextVariableConverter(ContextVariableTypeConverter.builder(OrderActivities.class)
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

    public String GetReasons(String memberid) throws SemanticKernelException, ServiceNotFoundException {
       
        Kernel kernel = InstantiateKernel();

        String prompt = """
            As a call center assistant, your task is to support the service agent by offering a list of likely workflows they may need to assist a member. Pick from the following workflows to answer: 

            View Order: This workflow allows the agent to see the member's orders. 
            Place Order: This workflow enables the agent to place new orders or refill existing ones. 
            Manage Prescriptions: This workflow allows the agent to manage both active and inactive prescriptions. 
            
            Based on historical member calls, the following reasons for contacting have been noted with their respective likelihoods and how to identify them in format of [Reason, percentage of calls, how to identify the reason]: 
            
            [Program education, 23.71%, no home delivery orders in recent history]
            [Refill, 21.82%, Refill is due (or close to it)]
            [WISMO(Where is my order?), 5.82%, open order with open order line items]
            [Pharmacist, 4.11%, call is from a pharmacist]
            [Web portal help, 1.06%, no identifiable reason]
            [Out of stock, 0.90%, Open order with one order line item marked as out of stock]
            
            ### Instruction ### 
            
            Provide the agent with a list of 3 workflows the agent is most likely to use and the reasoning behind the selection of each workflow in 6 words or less, including data that invoked that reasoning. 
            Order them in order of most likely use to least likely to use.
            Look at patient's order details and prescriptions to determine the most likely workflows.
            Also list out the data retrieved from the patient's order details and prescriptions.
            
            Desired JSON OBJECT output format:
            
             {
                Workflows[
                    { 
                        "workflow": "[workflow name]", 
                        "Reason": "[reasoning behind picking the workflow]" 
                    }, 
                    {
                        "workflow": "[workflow name]",
                        "Reason": "[reasoning behind picking the workflow]" 
                    }, 
                    { 
                        "workflow": "[workflow name]",
                        "Reason": "[reasoning behind picking the workflow]" 
                    }
                ]
             }

             After the JSON output, output the order details as well.
        """;
        
        // Enable planning
        InvocationContext invocationContext = new Builder()
            .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
            .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
            .withContextVariableConverter(ContextVariableTypeConverter.builder(OrderActivities.class)
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

    private Kernel InstantiateKernel() throws SemanticKernelException{
        TokenCredential credential = null;
        OpenAIAsyncClient client;

        if(config.getAzureClientId() != null && !config.getAzureClientId().isEmpty()) {
            credential = new ClientSecretCredentialBuilder()
            .clientId(config.getAzureClientId())
            .tenantId(config.getAzureTenantId())
            .clientSecret(config.getAzureClientSecret())            
            .build();

            TokenRequestContext requestContext = new TokenRequestContext().addScopes("https://cognitiveservices.azure.com/.default");

            String authToken = "Bearer " + credential.getTokenSync(requestContext).getToken();

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
        KernelPlugin callerActivitiesPlugin = KernelPluginFactory.createFromObject(new CallerActivitiesPlugin(), "CallerActivitiesPlugin");
        KernelPlugin orderDetailsPlugin = KernelPluginFactory.createFromObject(new OrderDetailsPlugin(this.config, this.tokenHelper, this.restClient), "OrderDetailsPlugin");
        KernelPlugin PrescriptionSearchPlugin = KernelPluginFactory.createFromObject(new PrescriptionSearchPlugin(this.config, this.tokenHelper, this.restClient), "PrescriptionSearchPlugin");
        // Create a kernel with Azure OpenAI chat completion and plugin
        Kernel.Builder builder = Kernel.builder();
        builder.withAIService(ChatCompletionService.class, chatService);
        // builder.withPlugin(callerActivitiesPlugin);
        builder.withPlugin(orderDetailsPlugin);
        builder.withPlugin(PrescriptionSearchPlugin);
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