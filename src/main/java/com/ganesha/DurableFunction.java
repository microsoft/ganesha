package com.ganesha;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.ganesha.models.LightModel;
import com.ganesha.plugins.LightsPlugin;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.durabletask.DurableTaskClient;
import com.microsoft.durabletask.TaskOrchestrationContext;
import com.microsoft.durabletask.azurefunctions.DurableActivityTrigger;
import com.microsoft.durabletask.azurefunctions.DurableClientContext;
import com.microsoft.durabletask.azurefunctions.DurableClientInput;
import com.microsoft.durabletask.azurefunctions.DurableOrchestrationTrigger;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypeConverter;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypes;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import com.nimbusds.jose.shaded.gson.Gson;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Azure Functions with HTTP Trigger.
 */
public class DurableFunction {
    /**
     * This HTTP-triggered function starts the orchestration.
     */
    @FunctionName("StartOrchestration")
    public HttpResponseMessage startOrchestration(
            @HttpTrigger(name = "req", methods = { HttpMethod.GET,
                    HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            @DurableClientInput(name = "durableContext") DurableClientContext durableContext,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        DurableTaskClient durableClient = durableContext.getClient();
        String instanceId = durableClient.scheduleNewOrchestrationInstance("Cities");
        context.getLogger().info("Created new Java orchestration with instance ID = " + instanceId);

        // begin sk test
        String CLIENT_ENDPOINT = System.getenv("CLIENT_ENDPOINT");
        String AZURE_CLIENT_KEY = System.getenv("AZURE_CLIENT_KEY");
        String MODEL_ID = System.getenv("MODEL_ID");

        Map<String, String> params = request.getQueryParameters();
        String userQuery = params.get("query");

        OpenAIAsyncClient client = new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(AZURE_CLIENT_KEY))
                .endpoint(CLIENT_ENDPOINT)
                .buildAsyncClient();

        // Import the LightsPlugin
        KernelPlugin lightPlugin = KernelPluginFactory.createFromObject(new LightsPlugin(),
                "LightsPlugin");

        // Create your AI service client
        ChatCompletionService chatCompletionService = OpenAIChatCompletion.builder()
                .withModelId(MODEL_ID)
                .withOpenAIAsyncClient(client)
                .build();

        // Create a kernel with Azure OpenAI chat completion and plugin
        Kernel kernel = Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .withPlugin(lightPlugin)
                .build();

        // Add a converter to the kernel to show it how to serialise LightModel objects
        // into a prompt
        ContextVariableTypes
                .addGlobalConverter(
                        ContextVariableTypeConverter.builder(LightModel.class)
                                .toPromptString(new Gson()::toJson)
                                .build());

        // Enable planning
        InvocationContext invocationContext = new InvocationContext.Builder()
                .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                .build();

        // Create a history to store the conversation
        ChatHistory history = new ChatHistory();

        System.out.println("User > " + userQuery);
        history.addUserMessage(userQuery);

        // Prompt AI for response to users input
        List<ChatMessageContent<?>> results = chatCompletionService
                .getChatMessageContentsAsync(history, kernel, invocationContext)
                .block();

        for (ChatMessageContent<?> result : results) {
            // Print the results
            if (result.getAuthorRole() == AuthorRole.ASSISTANT && result.getContent() != null) {
                System.out.println("Assistant > " + result);
            }
            // Add the message from the agent to the chat history
            history.addMessage(result);
        }
        // end sk test

        return durableContext.createCheckStatusResponse(request, instanceId);
    }

    /**
     * This is the orchestrator function, which can schedule activity functions,
     * create durable timers,
     * or wait for external events in a way that's completely fault-tolerant.
     */
    @FunctionName("Cities")
    public String citiesOrchestrator(
            @DurableOrchestrationTrigger(name = "taskOrchestrationContext") TaskOrchestrationContext ctx) {
        String result = "";
        result += ctx.callActivity("Capitalize", "Tokyo", String.class).await() + ", ";
        result += ctx.callActivity("Capitalize", "London", String.class).await() + ", ";
        result += ctx.callActivity("Capitalize", "Seattle", String.class).await() + ", ";
        result += ctx.callActivity("Capitalize", "Austin", String.class).await();
        return result;
    }

    /**
     * This is the activity function that is invoked by the orchestrator function.
     */
    @FunctionName("Capitalize")
    public String capitalize(@DurableActivityTrigger(name = "name") String name, final ExecutionContext context) {
        context.getLogger().info("Capitalizing: " + name);
        return name.toUpperCase();
    }
}
