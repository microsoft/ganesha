package com.microsoft.ganesha.response.model.orderdetailssearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"respCode", "correlationId", "respMessage"})
public class SearchOutputMetaData implements Serializable {

    private static final long serialVersionUID = 5089228664475161000L;

    @JsonProperty(value = "respCode")
    private String respCode;

    @JsonProperty(value = "correlationId")
    private String correlationId;

    @JsonProperty(value = "respMessage")
    private List<String> respMessage;
}
