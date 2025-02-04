package com.microsoft.ganesha.response.model.orderdetailssearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.ganesha.response.model.common.SearchOutputMetaData;

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
@JsonPropertyOrder({"searchOutputMetaData", "patientId", "orderDetails"})
public class OrderDetailsSearchResponse implements Serializable {

    private static final long serialVersionUID = -5306525856556287383L;

    @JsonProperty(value = "searchOutputMetaData")
    private SearchOutputMetaData searchOutputMetaData;

    @JsonProperty(value = "patientId")
    private String patientId;

    @JsonProperty(value = "orderDetails")
    private List<OrderDetailsSearch> orderDetails;
}
