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
@JsonPropertyOrder({"expectedDeliveryDate", "shippingAddressId", "shippingAddress", "dispensingSiteCode",
        "dispensingSiteName", "expStartDeliveryDate", "expEndDeliveryDate", "shippingMode"})
public class Shipping implements Serializable {

    private static final long serialVersionUID = 8263777309610901240L;

    @JsonProperty(value = "expectedDeliveryDate")
    private String expectedDeliveryDate;

    @JsonProperty(value = "shippingAddressId")
    private String shippingAddressId;

    @JsonProperty(value = "shippingAddress")
    private ShippingAddress shippingAddress;

    @JsonProperty(value = "dispensingSiteCode")
    private String dispensingSiteCode;

    @JsonProperty(value = "dispensingSiteName")
    private String dispensingSiteName;

    @JsonProperty(value = "expStartDeliveryDate")
    private String expStartDeliveryDate;

    @JsonProperty(value = "expEndDeliveryDate")
    private String expEndDeliveryDate;

    @JsonProperty(value = "shippingMode")
    private String shippingMode;
}
