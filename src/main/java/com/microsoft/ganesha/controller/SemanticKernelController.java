package com.microsoft.ganesha.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.microsoft.ganesha.exception.SemanticKernelException;
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
    public SemanticKernelController(SemanticKernel kernel, @Qualifier("mongoDatabaseService") MongoService mongoService) {
        _kernel = kernel;
        _mongoService = mongoService;
    }

    private SemanticKernel _kernel;
    private MongoService _mongoService;

    @PostMapping("/prompt")
    String replaceEmployee(@RequestBody Prompt prompt) throws SemanticKernelException, ServiceNotFoundException {
        return _kernel.GetSKResult(prompt.getPrompt());
    }

    @PostMapping("/predictReason")
    String predictReason(@RequestBody MemberId memberid) throws SemanticKernelException, ServiceNotFoundException {
        return _kernel.GetReasons(memberid.GetMemberId());
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
                    var dbConversation = _mongoService.GetConversation(conversationId);

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
            _mongoService.UpsertConversation(conversation);

            if (conversationDoesNotExist) {
                String reason = _kernel.GetReasons(messages.get(0).getMessage());

                if (reason != null) {
                    var reasonMessage = new DisplayChatMessage(reason, AuthorRole.ASSISTANT.toString(),
                            java.time.OffsetDateTime.now());

                    conversation.getMessages().add(reasonMessage);
                }
            } else {
                var chatHistory = _kernel.Converse(conversation.toChatHistory());

                // right now this constructor filters out tool and system messages
                // we may want to consider if we want to persist them for transparency?
                conversation = new Conversation(chatHistory);
            }

            // now make sure that completion is persisted
            _mongoService.UpsertConversation(conversation);

            return conversation;
        } catch (Exception e) {
            // todo: refine exceptions and handling throughout stack...
            // probably mongo service interface needs closer look
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
