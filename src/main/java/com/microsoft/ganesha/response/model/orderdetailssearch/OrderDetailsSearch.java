package com.microsoft.ganesha.response.model.orderdetailssearch;

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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"orderNumber", "orderType", "orderSource", "orderCreationDate", "orderClosedDate", "orderSOFNotes",
        "estimatedAmount", "orderStatus", "irisStatus", "mdoPromiseDate", "amounts", "shipping", "paymentsBackup",
        "payments", "easyPaymentDetails", "holds", "orderLines"})
public class OrderDetailsSearch implements Serializable {

    private static final long serialVersionUID = 2199624511056369310L;

    @JsonProperty(value = "orderNumber")
    private String orderNumber;

    @JsonProperty(value = "orderType")
    private String orderType;

    @JsonProperty(value = "orderSource")
    private String orderSource;

    @JsonProperty(value = "orderCreationDate")
    private String orderCreationDate;

    @JsonProperty(value = "orderClosedDate")
    private String orderClosedDate;

    @JsonProperty(value = "orderSOFNotes")
    private String orderSOFNotes;

    @JsonProperty(value = "estimatedAmount")
    private float estimatedAmount;

    @JsonProperty(value = "orderStatus")
    private OrderStatus orderStatus;

    @JsonProperty(value = "irisStatus")
    private IrisStatus irisStatus;

    @JsonProperty(value = "mdoPromiseDate")
    private String mdoPromiseDate;

    @JsonProperty(value = "amounts")
    private Amounts amounts;

    @JsonProperty(value = "shipping")
    private Shipping shipping;

    @JsonProperty(value = "paymentsBackup")
    private List<Payments> paymentsBackup;

    @JsonProperty(value = "payments")
    private List<Payments> payments;

    @JsonProperty(value = "easyPaymentDetails")
    private List<EasyPaymentDetails> easyPaymentDetails;

    @JsonProperty(value = "holds")
    private List<OrderLinesHolds> holds;

    @JsonProperty(value = "orderLines")
    private List<OrderLines> orderLines;

}
