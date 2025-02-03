package com.microsoft.ganesha.response.model.orderdetailssearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"inventoryItemId", "organizationId", "drugDescription", "ndc", "gpi", "strengthUnit", "therapy",
        "isItemAncillary", "sensitiveConditionFlag", "storageCondition", "officeBasedIndicator",
        "sigRequiredFlag", "isNDCSmfEligible"})
public class DrugDispensed implements Serializable {

    private static final long serialVersionUID = 3672761226222525150L;

    @JsonProperty(value = "inventoryItemId")
    private String inventoryItemId;

    @JsonProperty(value = "organizationId")
    private String organizationId;

    @JsonProperty(value = "drugDescription")
    private String drugDescription;

    @JsonProperty(value = "ndc")
    private String ndc;

    @JsonProperty(value = "gpi")
    private String gpi;

    @JsonProperty(value = "strengthUnit")
    private String strengthUnit;

    @JsonProperty(value = "therapy")
    private String therapy;

    @JsonProperty(value = "isItemAncillary")
    private String isItemAncillary;

    @JsonProperty(value = "sensitiveConditionFlag")
    private String sensitiveConditionFlag;

    @JsonProperty(value = "storageCondition")
    private String storageCondition;

    @JsonProperty(value = "officeBasedIndicator")
    private String officeBasedIndicator;

    @JsonProperty(value = "sigRequiredFlag")
    private String sigRequiredFlag;

    @JsonProperty(value = "isNDCSmfEligible")
    private String isNDCSmfEligible;


}
