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
import com.microsoft.ganesha.plugins.LightModel;
import com.microsoft.ganesha.plugins.LightsPlugin;
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

    

    public SemanticKernel(AppConfig config) {
        this.config = config;
    }
    

    public String GetSKResult(String prompt) throws SemanticKernelException, ServiceNotFoundException {
       
        Kernel kernel = InstantiateKernel();

        // Enable planning
        InvocationContext invocationContext = new Builder()
            .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
            .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
            .withContextVariableConverter(ContextVariableTypeConverter.builder(LightModel.class)
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
            .endpoint(config.getAzureClientId())
            .buildAsyncClient();         
        }
        
        // Create your AI service client
        ChatCompletionService chatService = OpenAIChatCompletion.builder()
            .withModelId(config.getModelId())
            .withOpenAIAsyncClient(client)
            .build();
        // Create a plugin (the LightsPlugin class is defined separately)
        KernelPlugin lightPlugin = KernelPluginFactory.createFromObject(new LightsPlugin(),
            "LightsPlugin");

        // Create a kernel with Azure OpenAI chat completion and plugin
        Kernel.Builder builder = Kernel.builder();
        builder.withAIService(ChatCompletionService.class, chatService);
        builder.withPlugin(lightPlugin);
        // Build the kernel
        Kernel kernel = builder.build();        

        ContextVariableTypes
            .addGlobalConverter(ContextVariableTypeConverter.builder(LightModel.class)
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