package com.microsoft.ganesha.semantickernel;

import java.util.List;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.TokenCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.google.gson.Gson;
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

    private final String clientId;
    private final String tenantId;
    private final String clientSecret;
    private final String endpoint;
    private final String modelId;

    public SemanticKernel(String clientId, String tenantId, String clientSecret, String endpoint, String modelId) {
        this.clientId = clientId;
        this.tenantId = tenantId;
        this.clientSecret = clientSecret;
        this.endpoint = endpoint;
        this.modelId = modelId;
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
        if(clientId != null && !clientId.isEmpty()) {
            credential = new ClientSecretCredentialBuilder()
            .clientId(clientId)
            .tenantId(tenantId)
            .clientSecret(clientSecret)            
            .build();
        } else {
            credential = new DefaultAzureCredentialBuilder().build();
        }   

        OpenAIAsyncClient client;

        client = new OpenAIClientBuilder()
            .credential(credential)
            .endpoint(endpoint)
            .buildAsyncClient();         
        
        // Create your AI service client
        ChatCompletionService chatService = OpenAIChatCompletion.builder()
            .withModelId(modelId)
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