package com.microsoft.ganesha.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.ganesha.request.common.model.SearchInputMetaData;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictReasonRequest {
    
    @JsonProperty("searchInputMetaData")
    private SearchInputMetaData searchInputMetaData;

    @JsonProperty("patientId")
    private String patientId;

}