package com.microsoft.ganesha.response.model.getordersummary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"holdName", "heldDate", "description", "holdComment", "actionRequired"})
public class Holds implements Serializable {

    private static final long serialVersionUID = -7425436558870599912L;

    @JsonProperty(value = "holdName")
    private String holdName;

    @JsonProperty(value = "heldDate")
    private String heldDate;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "holdComment")
    private String holdComment;

    @JsonProperty(value = "actionRequired")
    private String actionRequired;
}
