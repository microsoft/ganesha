package com.microsoft.ganesha.models;

import java.time.OffsetDateTime;

import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;

public class DisplayChatMessage {
    private String message;
    private String role;
    private OffsetDateTime time;

    public DisplayChatMessage(String message, String sender, OffsetDateTime time) {
        this.message = message;
        this.role = sender;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String sender) {
        this.role = sender;
    }

    public OffsetDateTime getTime() {
        return time;
    }

    public void setTime(OffsetDateTime time) {
        this.time = time;
    }
}
