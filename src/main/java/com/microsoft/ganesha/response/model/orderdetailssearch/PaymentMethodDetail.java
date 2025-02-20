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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"paymentMethodType", "bankAccountNickName", "endDate", "bankAccountNumber", "bankAccountType",
        "cardHolderName", "cardType", "cardTypeCode", "billingId", "defaultCard", "cardNickName", "expiryDate",})
public class PaymentMethodDetail implements Serializable {

    private static final long serialVersionUID = 832548721734197251L;

    @JsonProperty(value = "paymentMethodType")
    private String paymentMethodType;

    @JsonProperty(value = "bankAccountNickName")
    private String bankAccountNickName;

    @JsonProperty(value = "endDate")
    private String endDate;

    @JsonProperty(value = "bankAccountNumber")
    private String bankAccountNumber;

    // @JsonProperty(value = "bankAccountType")
    // private String bankAccountType;

    // @JsonProperty(value = "cardHolderName")
    // private String cardHolderName;

    // @JsonProperty(value = "cardType")
    // private String cardType;

    // @JsonProperty(value = "cardTypeCode")
    // private String cardTypeCode;

    // @JsonProperty(value = "billingId")
    // private String billingId;

    // @JsonProperty(value = "defaultCard")
    // private String defaultCard;

    // @JsonProperty(value = "cardNickName")
    // private String cardNickName;

    // @JsonProperty(value = "expiryDate")
    // private String expiryDate;

    // @JsonProperty(value = "cardPurpose")
    // private String cardPurpose;

    // @JsonProperty(value = "cardNumber")
    // private String cardNumber;

}
