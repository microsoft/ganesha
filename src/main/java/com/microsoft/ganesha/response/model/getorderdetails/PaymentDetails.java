package com.microsoft.ganesha.response.model.getorderdetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.ganesha.response.model.orderdetailssearch.Payments;
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
@JsonPropertyOrder({"payments", "creditCards", "achDetails"})
public class PaymentDetails implements Serializable {

    private static final long serialVersionUID = 5284000132120179156L;

    @JsonProperty(value = "payments")
    private List<Payments> payments;

    @JsonProperty(value = "creditCards")
    private List<CreditCardDetail> creditCards;

    @JsonProperty(value = "achDetails")
    private List<AchDetail> achDetails;
}
