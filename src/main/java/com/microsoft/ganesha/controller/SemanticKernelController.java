package com.microsoft.ganesha.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.microsoft.ganesha.exception.SemanticKernelException;
import com.microsoft.ganesha.interfaces.MongoService;
import com.microsoft.ganesha.models.Conversation;
import com.microsoft.ganesha.models.DisplayChatMessage;
import com.microsoft.ganesha.models.MemberIdRequest;
import com.microsoft.ganesha.models.MulitEntityRequest;
import com.microsoft.ganesha.models.SimplePromptRequest;
import com.microsoft.ganesha.semantickernel.SemanticKernel;
import com.microsoft.ganesha.services.MongoServiceFactory;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;

@RestController
public class SemanticKernelController {
    public SemanticKernelController(SemanticKernel kernel, MongoServiceFactory mongoService) {
        _kernel = kernel;
        _mongoService = mongoService.create();
    }

    private SemanticKernel _kernel;
    private MongoService _mongoService;

    @PostMapping("/prompt")
    String replaceEmployee(@RequestBody SimplePromptRequest prompt)
            throws SemanticKernelException, ServiceNotFoundException {
        return _kernel.GetSKResult(prompt.getPrompt());
    }

    @PostMapping("/predictReason")
    ChatHistory predictReason(@RequestBody MemberIdRequest request)
            throws SemanticKernelException, ServiceNotFoundException {
        try {
            var response = _kernel.GetReasons(request.getMemberId());

            // persist this ChatHistory object to MongoDB
            _mongoService.UpsertConversation(new Conversation(UUID.randomUUID(), response));
            
            return response;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/conversation")
    List<Conversation> getConversations(
            @RequestParam(value = "expanded", required = false, defaultValue = "false") boolean expanded)
            throws ServiceNotFoundException {
        try {
            var conversations = _mongoService.GetConversations();

            if (!expanded) {
                for (var conversation : conversations) {
                    conversation.setChatHistory(null);
                }
            }

            return conversations;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/conversation/{conversationId}")
    Conversation getConversation(@PathVariable("conversationId") UUID conversationId) throws ServiceNotFoundException {
        try {
            return _mongoService.GetConversation(conversationId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/conversation/{conversationId}")
    void deleteConversation(@PathVariable("conversationId") UUID conversationId) throws ServiceNotFoundException {
        try {
            _mongoService.DeleteConversation(conversationId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/conversation")
    Conversation conversation(@RequestBody Conversation conversation)
            throws SemanticKernelException, ServiceNotFoundException {
        try {

            if (conversation.getChatHistory() == null || conversation.getChatHistory().getMessages().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conversation messages cannot be empty");
            }

            var conversationDoesNotExist = true;

            if (conversation.getConversationId() == null) {
                conversationDoesNotExist = true;
                conversation.setConversationId(java.util.UUID.randomUUID());
            } else {
                try {
                    var dbConversation = _mongoService.GetConversation(conversation.getConversationId());

                    conversationDoesNotExist = dbConversation == null;
                } catch (Exception e) {
                    conversationDoesNotExist = true;
                }
            }

            // add latest message in conversation to db or if convo doesn't exist, make it
            // so
            _mongoService.UpsertConversation(conversation);

            if (conversationDoesNotExist) {
                ChatHistory reason = _kernel.GetReasons(conversation.getChatHistory().getMessages().get(0).getContent());
                conversation.setChatHistory(reason);
            } else {
                var chatHistory = _kernel.Converse(conversation.getChatHistory());

                // right now this constructor filters out tool and system messages
                // we may want to consider if we want to persist them for transparency?
                conversation = new Conversation(conversation.getConversationId(), chatHistory);
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

    @PostMapping("/claims")
    String getClaims(@RequestBody SimplePromptRequest promptRequest)
            throws SemanticKernelException, ServiceNotFoundException {

        return _kernel.getClaims(promptRequest.getPrompt());
    }

    // exceedingly open to a more thought through use case and naming for this
    @PostMapping("/processMultipleEntities")
    String processMultipleEntities(@RequestBody MulitEntityRequest request)
            throws SemanticKernelException, ServiceNotFoundException {
        return _kernel.processMultipleEntities(request.GetMemberId(), request.getClaimId());
    }
}
