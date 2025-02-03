package com.microsoft.ganesha.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.ganesha.request.common.model.SearchInputMetaData;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDetailsRequest {
    @JsonProperty("searchInputMetaData")
    private SearchInputMetaData searchInputMetaData;

    @JsonProperty("patientId")
    private String patientId;

    @JsonProperty("include")
    private String[] include;

    @JsonProperty("prescriptions")
    private String[] prescriptions;

    @JsonProperty("includeInactivePatients")
    private String includeInactivePatients;

    @JsonProperty("includeExpiredPrescriptions")
    private String includeExpiredPrescriptions;

    @JsonProperty("includeDiscontinuedPrescriptions")
    private String includeDiscontinuedPrescriptions;

    @JsonProperty("includeRefillablePrescriptions")
    private String includeRefillablePrescriptions;

    @JsonProperty("includeRenewablePrescriptions")
    private String includeRenewablePrescriptions;

    @JsonProperty("includeOpenOrderPrescriptions")
    private String includeOpenOrderPrescriptions;

    @JsonProperty("includeDuplicateGpi")
    private String includeDuplicateGpi;

    @JsonProperty("includeStudyRx")
    private String includeStudyRx;

    @JsonProperty("filterPrescriptionTypes")
    private String filterPrescriptionTypes;

    @JsonProperty("identity")
    private String identity;
}
