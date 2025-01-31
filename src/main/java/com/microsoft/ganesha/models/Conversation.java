package com.microsoft.ganesha.models;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;

public class Conversation {
    private UUID id;
    private List<DisplayChatMessage> messages;

    public Conversation() {
    }

    public Conversation(UUID id, List<DisplayChatMessage> messages) {
        this.id = id;
        this.messages = messages;
    }

    public Conversation(ChatHistory chatHistory) {
        this.id = UUID.randomUUID();
        messages = chatHistory
                .getMessages()
                .stream()
                .filter(r -> !r.getAuthorRole().equals(AuthorRole.SYSTEM)
                        && !r.getAuthorRole().equals(AuthorRole.TOOL))
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
                default:
                    throw new IllegalArgumentException("Unknown author role: " + message.getRole());
            }
        }
        return chatHistory;
    }
}
