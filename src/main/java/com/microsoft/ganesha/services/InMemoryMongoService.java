package com.microsoft.ganesha.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.microsoft.ganesha.interfaces.MongoService;
import com.microsoft.ganesha.models.Conversation;

@Component
@Scope("singleton")
public class InMemoryMongoService implements MongoService {

    private static InMemoryMongoService instance;
    private Map<UUID, Conversation> conversations;

    InMemoryMongoService() {
        conversations = new HashMap<>();
    }

    public static synchronized InMemoryMongoService getInstance() {
        if (instance == null) {
            instance = new InMemoryMongoService();
        }
        return instance;
    }

    @Override
    public Conversation GetConversation(UUID conversationId) throws Exception {
        if (conversations.containsKey(conversationId)) {
            return conversations.get(conversationId);
        } else {
            throw new Exception("Conversation not found");
        }
    }

    @Override
    public void UpsertConversation(Conversation conversation) throws Exception {
        conversations.put(conversation.getId(), conversation);
    }

    @Override
    public void DeleteConversation(UUID conversationId) throws Exception {
        if (conversations.containsKey(conversationId)) {
            conversations.remove(conversationId);
        } else {
            throw new Exception("Conversation not found");
        }
    }
}