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
@JsonPropertyOrder({"holdName", "heldDate", "heldBy", "holdUntil", "typeCode", "description", "holdComment",
        "actionRequired"})
public class OrderLinesHolds implements Serializable {

    @JsonProperty(value = "holdName")
    private String holdName;

    @JsonProperty(value = "heldDate")
    private String heldDate;

    @JsonProperty(value = "heldBy")
    private String heldBy;

    @JsonProperty(value = "holdUntil")
    private String holdUntil;

    @JsonProperty(value = "typeCode")
    private String typeCode;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "holdComment")
    private String holdComment;

    @JsonProperty(value = "actionRequired")
    private String actionRequired;
}
