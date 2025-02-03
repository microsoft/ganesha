package com.microsoft.ganesha.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.ganesha.request.common.model.DateRange;
import com.microsoft.ganesha.request.common.model.SearchInputMetaData;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"include", "orderFilter", "patientAccountId", "orderNumbers", "rxNumbers", "includeTrackingDetails",
        "includeOrderTypes", "includeOnlyLatestOrderForRx", "includeInactivePatients", "dateRange",
        "searchInputMetaData", "patientId", "dateOfBirth", "lastName"})
public class OrderDetailsSearchRequest implements Serializable {

    private static final long serialVersionUID = 5914288574632388344L;

    @JsonProperty(value = "include")
    private List<String> include;

    @JsonProperty(value = "orderFilter")
    private List<String> orderFilter;

    @JsonProperty(value = "patientAccountId")
    private String patientAccountId;

    @JsonProperty(value = "orderNumbers")
    private List<String> orderNumbers;

    @JsonProperty(value = "rxNumbers")
    private List<String> rxNumbers;

    @JsonProperty(value = "includeTrackingDetails")
    private String includeTrackingDetails;

    @JsonProperty(value = "includeOrderTypes")
    private String includeOrderTypes;

    @JsonProperty(value = "includeOnlyLatestOrderForRx")
    private String includeOnlyLatestOrderForRx;

    @JsonProperty(value = "includeInactivePatients")
    private String includeInactivePatients;

    @JsonProperty(value = "dateRange")
    private DateRange dateRange;

    @JsonProperty(value = "startDate")
    private String startDate;

    @JsonProperty(value = "endDate")
    private String endDate;

    @JsonProperty(value = "searchInputMetaData")
    private SearchInputMetaData searchInputMetaData;

    @JsonProperty(value = "patientId")
    private String patientId;

    @JsonProperty(value = "dateOfBirth")
    private String dateOfBirth;

    @JsonProperty(value = "lastName")
    private String lastName;

    @JsonProperty(value = "includeStudyRx")
    private String includeStudyRx;

}
