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
@JsonPropertyOrder({"date", "time", "description", "location"})
public class Events implements Serializable {

    private static final long serialVersionUID = 3567796448848220484L;

    @JsonProperty(value = "date")
    private String date;

    @JsonProperty(value = "time")
    private String time;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "location")
    private Location location;
}
