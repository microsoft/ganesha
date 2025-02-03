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
@JsonPropertyOrder({"flowStatusCode", "description"})
public class OrderStatus implements Serializable {

    private static final long serialVersionUID = -2923222501139507842L;

    @JsonProperty(value = "flowStatusCode")
    private String flowStatusCode;

    @JsonProperty(value = "description")
    private String description;
}
