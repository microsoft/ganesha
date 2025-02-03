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
@JsonPropertyOrder({"shipSetId", "scheduleShipDate", "actualShipmentDate", "promiseDate", "carrierName", "trackingNumber",
        "trackingUrl", "scheduleDeliveryDate", "trackingInfo", "shippingMethod"})
public class OrderLinesShipping implements Serializable {

    private static final long serialVersionUID = 1337942127755488615L;

    @JsonProperty(value = "shipSetId")
    private String shipSetId;

    @JsonProperty(value = "scheduleShipDate")
    private String scheduleShipDate;

    @JsonProperty(value = "actualShipmentDate")
    private String actualShipmentDate;

    @JsonProperty(value = "promiseDate")
    private String promiseDate;

    @JsonProperty(value = "carrierName")
    private String carrierName;

    @JsonProperty(value = "trackingNumber")
    private String trackingNumber;

    @JsonProperty(value = "trackingUrl")
    private String trackingUrl;

    @JsonProperty(value = "scheduleDeliveryDate")
    private String scheduleDeliveryDate;

    @JsonProperty(value = "trackingInfo")
    private TrackingInfo trackingInfo;

    @JsonProperty(value = "shippingMethod")
    private String shippingMethod;
}
