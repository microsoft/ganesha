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
@JsonPropertyOrder({"id", "type"})
public class PaymentMethod implements Serializable {

    private static final long serialVersionUID = 3097485187784161637L;

    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "type")
    private String type;
}
