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
@JsonPropertyOrder({"sequenceNumber", "paymentDueDate", "paymentStatus", "amount"})
public class EasyPaymentDetails implements Serializable {

    private static final long serialVersionUID = -7417155311217589336L;

    @JsonProperty(value = "sequenceNumber")
    private String sequenceNumber;

    @JsonProperty(value = "paymentDueDate")
    private String paymentDueDate;

    @JsonProperty(value = "paymentStatus")
    private String paymentStatus;

    @JsonProperty(value = "amount")
    private String amount;
}
