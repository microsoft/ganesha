package com.microsoft.ganesha.response.model.getorderdetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.ganesha.response.model.getordersummary.Holds;
import com.microsoft.ganesha.response.model.orderdetailssearch.ShippingAddress;
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
@JsonPropertyOrder({"orderNumber", "shipmentNumber", "orderCreationDate", "orderStatus", "officeBasedDelivery",
        "totalAmount", "shippingAddress", "paymentDetails", "holdsAtOrderLevel", "orderLines","isSignatureRequired"})
public class OrderDetails implements Serializable {

    private static final long serialVersionUID = 51488908714817079L;

    @JsonProperty(value = "orderNumber")
    private String orderNumber;

    @JsonProperty(value = "shipmentNumber")
    private String shipmentNumber;

    @JsonProperty(value = "orderCreationDate")
    private String orderCreationDate;

    @JsonProperty(value = "orderStatus")
    private String orderStatus;

    @JsonProperty(value = "officeBasedDelivery")
    private String officeBasedDelivery;

    @JsonProperty(value = "totalAmount")
    private String totalAmount;

    @JsonProperty(value = "shippingAddress")
    private ShippingAddress shippingAddress;

    @JsonProperty(value = "paymentDetails")
    private PaymentDetails paymentDetails;

    @JsonProperty(value = "holdsAtOrderLevel")
    private List<Holds> holdsAtOrderLevel;

    @JsonProperty(value = "orderLines")
    private List<OrderLineDetails> orderLines;

    @JsonProperty(value = "isSignatureRequired")
    private Boolean isSignatureRequired;

}
