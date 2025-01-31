package com.microsoft.ganesha.response.model.getorderdetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.ganesha.response.model.getordersummary.Holds;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"rxNumber", "lineType", "lineAmount", "daysSupply", "quantity", "therapy", "orderLineStatusCode",
        "drugName", "officeBasedIndicator", "holdStatus", "isRefrigerationRequired","shippingPriority","copayAmount",
        "isMedicallyBilled", "estimatedCopay", "estimationDate", "shippingDetails", "holds"})
public class OrderLineDetails implements Serializable {

    private static final long serialVersionUID = 8990363566754204919L;

    @JsonProperty(value = "rxNumber")
    private String rxNumber;

    @JsonProperty(value = "lineType")
    private String lineType;

    @JsonProperty(value = "lineAmount")
    private String lineAmount;

    @JsonProperty(value = "daysSupply")
    private String daysSupply;

    @JsonProperty(value = "quantity")
    private Quantity quantity;

    @JsonProperty(value = "therapy")
    private String therapy;

    @JsonProperty(value = "orderLineStatusCode")
    private String orderLineStatusCode;

    @JsonProperty(value = "drugName")
    private String drugName;

    @JsonProperty(value = "officeBasedIndicator")
     private String officeBasedIndicator;

    @JsonProperty(value = "holdStatus")
    private String holdStatus;

    @JsonProperty(value = "isRefrigerationRequired")
    private Boolean isRefrigerationRequired;

    @JsonProperty(value = "shippingPriority")
    private String shippingPriority;

    @JsonProperty(value = "copayAmount")
    private String copayAmount;

    @JsonProperty(value = "isMedicallyBilled")
    private Boolean isMedicallyBilled;

    @JsonProperty(value = "estimatedCopay")
    private String estimatedCopay;

    @JsonProperty(value = "estimationDate")
    private String estimationDate;

    @JsonProperty(value = "shippingDetails")
    private ShippingDetails shippingDetails;

    @JsonProperty(value = "holds")
    private List<Holds> holds;
}
