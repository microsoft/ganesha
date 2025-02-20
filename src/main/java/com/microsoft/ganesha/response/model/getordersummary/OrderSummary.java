package com.microsoft.ganesha.response.model.getordersummary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonPropertyOrder({"orderNumber", "shipmentNumber", "orderStatus", "holdStatus", "orderType", "placedOn",
        "orderLineSummaries", "holds", "officeBasedDelivery"})
public class OrderSummary implements Serializable {

    private static final long serialVersionUID = 7578713877272699289L;

    @JsonProperty(value = "orderNumber")
    private String orderNumber;

    @JsonProperty(value = "shipmentNumber")
    private String shipmentNumber;

    @JsonProperty(value = "orderStatus")
    private String orderStatus;

    @JsonProperty(value = "holdStatus")
    private String holdStatus;

    @JsonProperty(value = "orderType")
    private String orderType;

    @JsonProperty(value = "placedOn")
    private String placedOn;

    @JsonProperty(value = "orderLineSummaries")
    private List<OrderLineSummary> orderLineSummaries;

    @JsonProperty(value = "holds")
    private List<Holds> holds;

    @JsonProperty(value = "officeBasedDelivery")
    private String officeBasedDelivery;
}
