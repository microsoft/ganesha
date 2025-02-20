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
@JsonPropertyOrder({"freightAmount", "taxAmount", "totalAmount", "estimatedTotalAmount"})
public class Amounts implements Serializable {

    @JsonProperty(value = "freightAmount")
    private String freightAmount;

    @JsonProperty(value = "taxAmount")
    private String taxAmount;

    @JsonProperty(value = "totalAmount")
    private String totalAmount;

    @JsonProperty(value = "estimatedTotalAmount")
    private String estimatedTotalAmount;
}
