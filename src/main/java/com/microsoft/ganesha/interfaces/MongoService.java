package com.microsoft.ganesha.interfaces;

import java.util.UUID;

import com.microsoft.ganesha.models.Conversation;

public interface MongoService {
    public Conversation GetConversation(UUID conversationId) throws Exception;

    public void UpsertConversation(Conversation conversation) throws Exception;

    public void DeleteConversation(UUID conversationId) throws Exception;

    // consider adding methods to handle individual messages within a conversation
}
