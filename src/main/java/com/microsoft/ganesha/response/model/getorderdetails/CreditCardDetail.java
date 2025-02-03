package com.microsoft.ganesha.response.model.getorderdetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"instrumentId", "cardType", "cardNumber", "cardTypeCode", "cardPurpose", "amount"})
public class CreditCardDetail implements Serializable {

    private static final long serialVersionUID = 4547716887860445331L;

    @JsonProperty(value = "instrumentId")
    private String instrumentId;

    @JsonProperty(value = "cardType")
    private String cardType;

    @JsonProperty(value = "cardNumber")
    private String cardNumber;

    @JsonProperty(value = "cardTypeCode")
    private String cardTypeCode;

    @JsonProperty(value = "cardPurpose")
    private String cardPurpose;

    @JsonProperty(value = "amount")
    private String amount;
}
