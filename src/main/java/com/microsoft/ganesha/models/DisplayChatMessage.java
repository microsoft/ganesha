package com.microsoft.ganesha.models;

import java.time.OffsetDateTime;

public class DisplayChatMessage {
    private String message;
    private String role;
    private OffsetDateTime time;

    public DisplayChatMessage() {
    }

    public DisplayChatMessage(String message, String sender, OffsetDateTime time) {
        this.message = message;
        this.role = sender;
        this.time = time;
    }

    public DisplayChatMessage(DisplayChatMessage message) {
        this.message = message.message;
        this.role = message.role;
        this.time = message.time;
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
