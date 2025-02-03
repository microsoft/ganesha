package com.microsoft.ganesha.response.model.prescriptiondetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notes {
    // @JsonProperty("rxId")
    // private String rxId;

    @JsonProperty("noteType")
    private String noteType;

    @JsonProperty("noteText")
    private String noteText;

    // @JsonProperty("statusFlag")
    // private String statusFlag;

    // @JsonProperty("audit")
    // private Audit audit;
}