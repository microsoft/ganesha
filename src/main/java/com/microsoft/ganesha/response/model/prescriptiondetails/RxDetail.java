package com.microsoft.ganesha.response.model.prescriptiondetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RxDetail {
    // @JsonProperty("drug")
    // private Drug drug;

    @JsonProperty("prescribedDrug")
    private Drug prescribedDrug;

    // @JsonProperty("quantity")
    // private Quantity quantity;

    @JsonProperty("daysSupply")
    private String daysSupply;

    @JsonProperty("writtenDate")
    private String writtenDate;

    @JsonProperty("lastFillDate")
    private String lastFillDate;

    @JsonProperty("nextFillDate")
    private String nextFillDate;

    @JsonProperty("discontinuedDate")
    private String discontinuedDate;

    @JsonProperty("expirationDate")
    private String expirationDate;

    @JsonProperty("dropDate")
    private String dropDate;

    @JsonProperty("activeFlag")
    private String activeFlag;

    @JsonProperty("skipNextFill")
    private String skipNextFill;

    @JsonProperty("maxRefillsAllowed")
    private String maxRefillsAllowed;

    @JsonProperty("shipmentIntervalDays")
    private String shipmentIntervalDays;

    @JsonProperty("rxHashKey")
    private String rxHashKey;

    @JsonProperty("refillNo")
    private String refillNo;

    @JsonProperty("refillsRemaining")
    private String refillsRemaining;

    @JsonProperty("refillStatus")
    private String refillStatus;

    @JsonProperty("refillStatusMessage")
    private String refillStatusMessage;

    @JsonProperty("rxIdLink")
    private String rxIdLink;

    @JsonProperty("rxInitiatedBy")
    private String rxInitiatedBy;

    @JsonProperty("dispensedAsWritten")
    private String dispensedAsWritten;

    @JsonProperty("refillable")
    private String refillable;

    @JsonProperty("renewable")
    private String renewable;

    @JsonProperty("hffEligible")
    private String hffEligible;

    @JsonProperty("hffReason")
    private String hffReason;

    @JsonProperty("hffEnrolled")
    private String hffEnrolled;

    @JsonProperty("ruleException")
    private String ruleException;

    @JsonProperty("stateDiagnosisCode")
    private String stateDiagnosisCode;

    @JsonProperty("rawFlag")
    private String rawFlag;

    @JsonProperty("qtyRemaining")
    private String qtyRemaining;

    @JsonProperty("fillNumber")
    private String fillNumber;

    @JsonProperty("tstFlag")
    private String tstFlag;

    @JsonProperty("icd10")
    private String icd10;

    @JsonProperty("unitsPerDay")
    private String unitsPerDay;

    @JsonProperty("cycleDays")
    private String cycleDays;
}