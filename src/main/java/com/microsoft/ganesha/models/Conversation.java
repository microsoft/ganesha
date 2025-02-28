package com.microsoft.ganesha.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;

import org.bson.Document;

public class Conversation {
    private UUID conversationId;
    private ChatHistory chatHistory;

    public Conversation() {
    }

    public Conversation(Conversation conversation) {
        this.conversationId = conversation.conversationId;
        this.chatHistory = conversation.chatHistory;
    }

    public Conversation(UUID conversationId, ChatHistory chatHistory) {
        this.conversationId = conversationId;
        this.chatHistory = chatHistory;
    }

    public Conversation(UUID conversationId, List<DisplayChatMessage> messages) {
        this.conversationId = conversationId;
        this.chatHistory = new ChatHistory();
        messages
                .stream()
                //.filter(r -> !r.getAuthorRole().equals(AuthorRole.SYSTEM))
                .forEach(m -> {
                    switch (m.getRole().toUpperCase()) {
                        case "USER":
                            chatHistory.addUserMessage(m.getMessage());
                            break;
                        case "SYSTEM":
                            chatHistory.addSystemMessage(m.getMessage());
                            break;
                        case "ASSISTANT":
                            chatHistory.addAssistantMessage(m.getMessage());
                            break;
                        case "TOOL":
                            chatHistory.addMessage(AuthorRole.TOOL, m.getMessage());
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown author role: " + m.getRole());
                    }
                });
    }

    public Conversation(Document document) {
        this.conversationId = UUID.fromString(document.getString("conversationId"));
        this.chatHistory = document.get("chatHistory", ChatHistory.class);
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }

    public ChatHistory getChatHistory() {
        return chatHistory;
    }

    public void setChatHistory(ChatHistory chatHistory) {
        this.chatHistory = chatHistory;
    }

    public ChatHistory toChatHistory() {
        return this.chatHistory;
    }
}
