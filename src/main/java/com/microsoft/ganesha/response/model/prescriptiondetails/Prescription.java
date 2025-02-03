package com.microsoft.ganesha.response.model.prescriptiondetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {
    @JsonProperty("patientId")
    private String patientId;

    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("rxDetails")
    private List<RxDetails> rxDetails;
}