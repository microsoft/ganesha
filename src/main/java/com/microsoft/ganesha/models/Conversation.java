package com.microsoft.ganesha.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import org.bson.Document;

public class Conversation {
    private UUID conversationId;
    private List<DisplayChatMessage> messages;

    public Conversation() {
    }

    public Conversation(Conversation conversation) {
        this.conversationId = conversation.conversationId;
        this.messages = new ArrayList<>();
        for (DisplayChatMessage message : conversation.messages) {
            this.messages.add(new DisplayChatMessage(message));
        }
    }

    public Conversation(UUID conversationId, List<DisplayChatMessage> messages) {
        this.conversationId = conversationId;
        this.messages = messages;
    }

    public Conversation(UUID conversationId, ChatHistory chatHistory) {
        this.conversationId = conversationId;
        messages = chatHistory
                .getMessages()
                .stream()
                //.filter(r -> !r.getAuthorRole().equals(AuthorRole.SYSTEM))
                .map(r -> {
                    var date = Optional.ofNullable(r.getMetadata())
                            .map(metadata -> metadata.getCreatedAt())
                            .orElse(java.time.OffsetDateTime
                                    .now(java.time.ZoneOffset.UTC));

                    return new DisplayChatMessage(r.getContent(),
                            r.getAuthorRole().toString(),
                            date);
                })
                .toList();
    }

    public Conversation(Document document) {
        this.conversationId = UUID.fromString(document.getString("conversationId"));
        List<Document> messageDocs = document.getList("messages", Document.class);
        this.messages = new ArrayList<>();
        for (Document messageDoc : messageDocs) {
            this.messages.add(new DisplayChatMessage(
                    messageDoc.getString("message"),
                    messageDoc.getString("role"),
                    messageDoc.getDate("time").toInstant().atOffset(java.time.ZoneOffset.UTC)));
        }
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }

    public List<DisplayChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<DisplayChatMessage> messages) {
        this.messages = messages;
    }

    public ChatHistory toChatHistory() {
        ChatHistory chatHistory = new ChatHistory();
        for (DisplayChatMessage message : this.messages) {
            switch (message.getRole().toUpperCase()) {
                case "USER":
                    chatHistory.addUserMessage(message.getMessage());
                    break;
                case "SYSTEM":
                    chatHistory.addSystemMessage(message.getMessage());
                    break;
                case "ASSISTANT":
                    chatHistory.addAssistantMessage(message.getMessage());
                    break;
                case "TOOL":
                    chatHistory.addMessage(AuthorRole.TOOL, message.getMessage());
                    break;
                default:
                    throw new IllegalArgumentException("Unknown author role: " + message.getRole());
            }
        }
        return chatHistory;
    }
}
