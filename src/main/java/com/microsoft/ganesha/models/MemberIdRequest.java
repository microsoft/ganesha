package com.microsoft.ganesha.models;

import java.util.UUID;

public class MemberIdRequest {
    private String memberId;
    private UUID conversationId;

    // @JsonProperty("memberid")
    public String getMemberId() {
        return this.memberId;
    }

    // @JsonProperty("conversationid")
    public UUID getConversationId() {
        return this.conversationId;
    }
}
