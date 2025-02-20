package com.microsoft.ganesha.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.microsoft.ganesha.config.AppConfig;
import com.microsoft.ganesha.exception.SemanticKernelException;
import com.microsoft.ganesha.helper.TokenHelper;
import com.microsoft.ganesha.request.PredictReasonRequest;
import com.microsoft.ganesha.rest.RestClient;
import com.microsoft.ganesha.interfaces.MongoService;
import com.microsoft.ganesha.models.Conversation;
import com.microsoft.ganesha.models.DisplayChatMessage;
import com.microsoft.ganesha.models.MulitEntityRequest;
import com.microsoft.ganesha.models.SimplePromptRequest;
import com.microsoft.ganesha.semantickernel.SemanticKernel;
import com.microsoft.ganesha.services.MongoServiceFactory;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;



@RestController
@CrossOrigin(origins = "*")
public class SemanticKernelController {
    
    private final AppConfig config;
    private final TokenHelper tokenHelper;
    private final RestClient restClient;
    private final MongoService mongoService;
    private final SemanticKernel kernel;

    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    public SemanticKernelController(AppConfig config, TokenHelper tokenHelper, RestClient restClient, SemanticKernel kernel, MongoServiceFactory mongoService) {
        this.config = config;
        this.tokenHelper = tokenHelper;
        this.restClient = restClient;
        this.mongoService = mongoService.create();
        this.kernel = kernel;
    }

    @PostMapping("/prompt")
    String replaceEmployee(@RequestBody SimplePromptRequest prompt) throws SemanticKernelException, ServiceNotFoundException {
        return kernel.GetSKResult(prompt.getPrompt());
    }

    @PostMapping("/predictReason")
    String predictReason(@RequestBody PredictReasonRequest request) throws SemanticKernelException, ServiceNotFoundException {
        return kernel.GetReasons(request.getPatientId(), request.getSearchInputMetaData().getCorrelationId());
    }

    @GetMapping("/conversation")
    List<Conversation> getConversations(
            @RequestParam(value = "expanded", required = false, defaultValue = "false") boolean expanded)
            throws ServiceNotFoundException {
        try {
            var conversations = mongoService.GetConversations();

            if (!expanded) {
                for (var conversation : conversations) {
                    conversation.setMessages(null);
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
            return mongoService.GetConversation(conversationId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/conversation/{conversationId}")
    void deleteConversation(@PathVariable("conversationId") UUID conversationId) throws ServiceNotFoundException {
        try {
            mongoService.DeleteConversation(conversationId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/conversation")
    Conversation conversation(@RequestBody Conversation conversation)
            throws SemanticKernelException, ServiceNotFoundException {
        LOGGER.info("Processing conversation in controller with correlation Id {}", conversation.getCorrelationId());
        try {

            if (conversation.getMessages() == null || conversation.getMessages().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conversation messages cannot be empty");
            }

            var conversationDoesNotExist = true;

            if (conversation.getConversationId() == null) {
                conversationDoesNotExist = true;
                conversation.setConversationId(java.util.UUID.randomUUID());
            } else {
                try {
                    LOGGER.info("Entering getConversation in conversation controller with correlation Id {}", conversation.getCorrelationId());

                    var dbConversation = mongoService.GetConversation(conversation.getConversationId());

                    conversationDoesNotExist = dbConversation == null;
                } catch (Exception e) {
                    conversationDoesNotExist = true;
                }
            }

            // add latest message in conversation to db or if convo doesn't exist, make it
            // so
            LOGGER.info("Upserting conversation with ID {}", conversation.getConversationId());  
            mongoService.UpsertConversation(conversation);

            if (conversationDoesNotExist) {
                LOGGER.info("Entering getReasons in conversation controller with correlation Id {}", conversation.getCorrelationId());
                String reason = kernel.GetReasons(conversation.getPatientId(), conversation.getCorrelationId());

                if (reason != null) {
                    var reasonMessage = new DisplayChatMessage(reason, AuthorRole.ASSISTANT.toString(),
                            java.time.OffsetDateTime.now());

                    conversation.getMessages().add(reasonMessage);
                }
            } else {
                LOGGER.info("Entering converse in conversation controller with correlation Id {}", conversation.getCorrelationId());
                var chatHistory = kernel.Converse(conversation.toChatHistory());

                // right now this constructor filters out tool and system messages
                // we may want to consider if we want to persist them for transparency?
                conversation = new Conversation(conversation.getConversationId(), chatHistory);
            }

            // now make sure that completion is persisted
            mongoService.UpsertConversation(conversation);

            return conversation;
        } catch (Exception e) {
            // todo: refine exceptions and handling throughout stack...
            // probably mongo service interface needs closer look
            LOGGER.info("Exception raised {} in conversation controller with correlation Id {}", e.getMessage(), conversation.getCorrelationId());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/claims")
    String getClaims(@RequestBody SimplePromptRequest promptRequest)
            throws SemanticKernelException, ServiceNotFoundException {

        return kernel.getClaims(promptRequest.getPrompt());
    }

    // exceedingly open to a more thought through use case and naming for this
    @PostMapping("/processMultipleEntities")
    String processMultipleEntities(@RequestBody MulitEntityRequest request)
            throws SemanticKernelException, ServiceNotFoundException {
        return kernel.processMultipleEntities(request.GetMemberId(), request.getClaimId());
    }
}
