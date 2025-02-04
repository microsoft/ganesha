package com.microsoft.ganesha.models;

public class MulitEntityRequest {
    private String claimId;
    private int memberid;

    public MulitEntityRequest() {
    }

    public MulitEntityRequest(String claimId, int memberid) {
        this.claimId = claimId;
        this.memberid = memberid;
    }

    public int GetMemberId() {
        return this.memberid;
    }

    public String getClaimId() {
        return claimId;
    }
}
