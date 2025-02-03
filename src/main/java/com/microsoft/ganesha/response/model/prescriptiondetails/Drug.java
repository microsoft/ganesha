package com.microsoft.ganesha.response.model.prescriptiondetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Drug {
    @JsonProperty("itemId")
    private String itemId;

    @JsonProperty("organizationId")
    private String organizationId;

    @JsonProperty("description")
    private String description;


    @JsonProperty("officeBasedIndicator")
    private String officeBasedIndicator;

    @JsonProperty("drugDeaCode")
    private String drugDeaCode;

    @JsonProperty("itemUom")
    private String itemUom;

    @JsonProperty("compoundDrugIndicator")
    private String compoundDrugIndicator;

    @JsonProperty("itemC2Indicator")
    private String itemC2Indicator;

    @JsonProperty("itemFormularyIndicator")
    private String itemFormularyIndicator;

    @JsonProperty("drugProductType")
    private String drugProductType;

    @JsonProperty("itemIsOkToOrder")
    private boolean itemIsOkToOrder;

    @JsonProperty("itemSpecialityIndicator")
    private boolean itemSpecialityIndicator;

    @JsonProperty("perDaysSupply")
    private String perDaysSupply;

    @JsonProperty("itemStatusCode")
    private String itemStatusCode;

    @JsonProperty("isNDCSmsEligible")
    private String isNDCSmsEligible;

    @JsonProperty("storageCondition")
    private String storageCondition;

    @JsonProperty("longDescription")
    private String longDescription;

    @JsonProperty("itemPartB")
    private boolean itemPartB;

    @JsonProperty("itemAncillary")
    private boolean itemAncillary;
}