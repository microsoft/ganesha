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
@JsonPropertyOrder({"value", "unitOfMeasurement"})
public class OrderLinesQuantity implements Serializable {

    private static final long serialVersionUID = -7934839069379619820L;

    @JsonProperty(value = "value")
    private String value;

    @JsonProperty(value = "unitOfMeasurement")
    private String unitOfMeasurement;

}
