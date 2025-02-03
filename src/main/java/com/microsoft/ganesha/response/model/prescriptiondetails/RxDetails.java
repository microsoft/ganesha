package com.microsoft.ganesha.response.model.prescriptiondetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RxDetails {

    @JsonProperty("rxStatus")
    private String rxStatus;

    @JsonProperty("rxStatusDetails")
    private String rxStatusDetails;

    @JsonProperty("cashOrder")
    private String cashOrder;

    @JsonProperty("smsPortalRefillable")
    private String smsPortalRefillable;

    // @JsonProperty("ancillaryItems")
    // private List<AncillaryItems> ancillaryItems;

    @JsonProperty("validUntil")
    private String validUntil;

    @JsonProperty("rxDetail")
    private RxDetail rxDetail;

    // @JsonProperty("rxInstructions")
    // private RxInstructions rxInstructions;

    // @JsonProperty("provider")
    // private Provider provider;

    // @JsonProperty("ivr")
    // private Ivr ivr;

    // @JsonProperty("rxImaging")
    // private RxImaging rxImaging;

    @JsonProperty("notes")
    private List<Notes> notes;
    
}