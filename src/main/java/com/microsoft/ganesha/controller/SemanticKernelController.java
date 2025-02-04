package com.microsoft.ganesha.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.microsoft.ganesha.config.AppConfig;
import com.microsoft.ganesha.exception.SemanticKernelException;
import com.microsoft.ganesha.helper.TokenHelper;
import com.microsoft.ganesha.request.PredictReasonRequest;
import com.microsoft.ganesha.rest.RestClient;
import com.microsoft.ganesha.interfaces.MongoService;
import com.microsoft.ganesha.models.Conversation;
import com.microsoft.ganesha.models.DisplayChatMessage;
import com.microsoft.ganesha.semantickernel.MemberId;
import com.microsoft.ganesha.semantickernel.Prompt;
import com.microsoft.ganesha.semantickernel.SemanticKernel;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;


@RestController
public class SemanticKernelController {
    
    private final AppConfig config;
    private final TokenHelper tokenHelper;
    private final RestClient restClient;
    private final MongoService mongoService;
    private final SemanticKernel kernel;


    public SemanticKernelController(AppConfig config, TokenHelper tokenHelper, RestClient restClient, SemanticKernel kernel, @Qualifier("mongoDatabaseService") MongoService mongoService) {
        this.config = config;
        this.tokenHelper = tokenHelper;
        this.restClient = restClient;
        this.mongoService = mongoService;
        this.kernel = kernel;
    }

    @PostMapping("/prompt")
    String replaceEmployee(@RequestBody Prompt prompt) throws SemanticKernelException, ServiceNotFoundException {
        return kernel.GetSKResult(prompt.getPrompt());
    }

    @PostMapping("/predictReason")
    String predictReason(@RequestBody PredictReasonRequest request) throws SemanticKernelException, ServiceNotFoundException {
        return kernel.GetReasons(request.getPatientId(), request.getSearchInputMetaData().getCorrelationId());
    }

    @PostMapping("/conversation")
    Conversation conversation(@RequestBody Conversation conversation)
            throws SemanticKernelException, ServiceNotFoundException {
        try {
            var conversationId = conversation.getId();
            var messages = conversation.getMessages();

            if (messages == null || messages.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conversation messages cannot be empty");
            }

            var conversationDoesNotExist = true;

            if (conversationId == null) {
                conversationDoesNotExist = true;
            } else {
                try {
                    var dbConversation = mongoService.GetConversation(conversationId);

                    conversationDoesNotExist = dbConversation == null;
                } catch (Exception e) {
                    conversationDoesNotExist = true;
                }
            }

            if (conversationDoesNotExist) {
                conversation.setId(java.util.UUID.randomUUID());
            }

            // add latest message in conversation to db or if convo doesn't exist, make it
            // so
            mongoService.UpsertConversation(conversation);

            if (conversationDoesNotExist) {
                String reason = kernel.GetReasons(messages.get(0).getMessage(), "test");

                if (reason != null) {
                    var reasonMessage = new DisplayChatMessage(reason, AuthorRole.ASSISTANT.toString(),
                            java.time.OffsetDateTime.now());

                    conversation.getMessages().add(reasonMessage);
                }
            } else {
                var chatHistory = kernel.Converse(conversation.toChatHistory());

                // right now this constructor filters out tool and system messages
                // we may want to consider if we want to persist them for transparency?
                conversation = new Conversation(chatHistory);
            }

            // now make sure that completion is persisted
            mongoService.UpsertConversation(conversation);

            return conversation;
        } catch (Exception e) {
            // todo: refine exceptions and handling throughout stack...
            // probably mongo service interface needs closer look
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
