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
@JsonPropertyOrder({"coverageId", "billLevel", "copay", "planPayment", "planInternalAcctId"})
public class BillingInfo implements Serializable {

    private static final long serialVersionUID = 4244752282040362896L;

    @JsonProperty(value = "coverageId")
    private String coverageId;

    @JsonProperty(value = "billLevel")
    private String billLevel;

    @JsonProperty(value = "copay")
    private String copay;

    @JsonProperty(value = "planPayment")
    private String planPayment;

    @JsonProperty(value = "planInternalAcctId")
    private String planInternalAcctId;
}
