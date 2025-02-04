package com.microsoft.ganesha.response.model.prescriptiondetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.ganesha.response.model.common.SearchOutputMetaData;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionSearchResponse {
    @JsonProperty("searchOutputMetaData")
    private SearchOutputMetaData searchOutputMetaData;

    @JsonProperty("prescriptions")
    private List<Prescription> prescriptions;

}