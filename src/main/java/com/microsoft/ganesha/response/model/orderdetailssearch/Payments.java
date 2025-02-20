package com.microsoft.ganesha.response.model.orderdetailssearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"paymentMethod", "details", "amount", "paymentDate", "shipSetId", "transactionType"})
public class Payments implements Serializable {

    private static final long serialVersionUID = 4754748483528366902L;

    @JsonProperty(value = "paymentMethod")
    private PaymentMethod paymentMethod;

    @JsonProperty(value = "details")
    private PaymentMethodDetail details;

    @JsonProperty(value = "amount")
    private String amount;

    @JsonProperty(value = "paymentDate")
    private String paymentDate;

    // @JsonProperty(value = "shipSetId")
    // private String shipSetId;

    // @JsonProperty(value = "transactionType")
    // private String transactionType;
}
