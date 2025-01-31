package com.microsoft.ganesha.models;

import java.util.Date;

public class Claim {
    private String claimId;
    private String policyNumber;
    private String claimantName;
    private Date dateOfClaim;
    private double claimAmount;
    private String claimStatus;

    // Default constructor
    public Claim() {
    }

    // Parameterized constructor
    public Claim(String claimId, String policyNumber, String claimantName, Date dateOfClaim, double claimAmount,
            String claimStatus) {
        this.claimId = claimId;
        this.policyNumber = policyNumber;
        this.claimantName = claimantName;
        this.dateOfClaim = dateOfClaim;
        this.claimAmount = claimAmount;
        this.claimStatus = claimStatus;
    }

    // Getters and Setters
    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getClaimantName() {
        return claimantName;
    }

    public void setClaimantName(String claimantName) {
        this.claimantName = claimantName;
    }

    public Date getDateOfClaim() {
        return dateOfClaim;
    }

    public void setDateOfClaim(Date dateOfClaim) {
        this.dateOfClaim = dateOfClaim;
    }

    public double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }
}
