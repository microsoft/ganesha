package com.eapi.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"correlationId", "errors", "timeStamp", "errorType", "status", "message", "exception"})
public class DownstreamErrorResponse implements Serializable {

    private static final long serialVersionUID = -2271609762921002302L;

    @JsonProperty(value = "correlationId")
    private String correlationId;

    @JsonProperty(value = "errors")
    private List<Error> errors;

    @JsonProperty(value = "timeStamp")
    private String timeStamp;

    @JsonProperty(value = "errorType")
    private String errorType;

    @JsonProperty(value = "status")
    private String status;

    @JsonProperty(value = "exception")
    private String exception;
    
    @JsonProperty(value = "message")
    private String message;
}


