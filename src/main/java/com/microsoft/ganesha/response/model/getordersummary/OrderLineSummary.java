package com.microsoft.ganesha.response.model.getordersummary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"expectedDeliveryStatus", "lineType", "drugNames", "rxNumber", "holdStatus",
        "orderLineStatus", "officeBasedIndicator", "isRefrigerationRequired", "shippingPriority", "holds"})
public class OrderLineSummary implements Serializable {

    private static final long serialVersionUID = -943652611223452120L;

    @JsonProperty(value = "expectedDeliveryStatus")
    private String expectedDeliveryStatus;

    @JsonProperty(value = "lineType")
    private String lineType;

    @JsonProperty(value = "drugNames")
    private String drugNames;

    @JsonProperty(value = "rxNumber")
    private String rxNumber;

    @JsonProperty(value = "holdStatus")
    private String holdStatus;

    @JsonProperty(value = "orderLineStatus")
    private String orderLineStatus;

    @JsonProperty(value = "officeBasedIndicator")
    private String officeBasedIndicator;

    @JsonProperty(value = "isRefrigerationRequired")
    private Boolean isRefrigerationRequired;

    @JsonProperty(value = "shippingPriority")
    private String shippingPriority;

    @JsonProperty(value = "holds")
    private List<Holds> holds;
}
