package com.microsoft.ganesha.response.model.getorderdetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"value", "unitOfMeasurement"})
public class Quantity implements Serializable {

    @Serial
    private static final long serialVersionUID = 4233423423423423423L;

    @JsonProperty("value")
    private String value;

    @JsonProperty("unitOfMeasurement")
    private String unitOfMeasurement;
}
