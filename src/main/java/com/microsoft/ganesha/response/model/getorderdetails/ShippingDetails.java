package com.microsoft.ganesha.response.model.getorderdetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonPropertyOrder({"expectedDeliveryDate", "shippedOnDate", "carrierName", "trackingNumber", "trackingURL",
        "trackingDetails"})
public class ShippingDetails implements Serializable {

    private static final long serialVersionUID = -6653556874579567090L;

    @JsonProperty(value = "expectedDeliveryDate")
    private String expectedDeliveryDate;

    @JsonProperty(value = "shippedOnDate")
    private String shippedOnDate;

    @JsonProperty(value = "carrierName")
    private String carrierName;

    @JsonProperty(value = "trackingNumber")
    private String trackingNumber;

    @JsonProperty(value = "trackingURL")
    private String trackingURL;

    @JsonProperty(value = "trackingDetails")
    private List<TrackingDetails> trackingDetails;

}
