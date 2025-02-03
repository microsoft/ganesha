package com.microsoft.ganesha.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.http.HttpStatusCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"timestamp", "endpoint", "errorTitle", "errorStatus", "message",
        "downstream-error-detail", "correlationId"})
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 3039526521167171289L;

    @JsonProperty(value = "timestamp")
    private LocalDateTime timestamp;

    @JsonProperty(value = "endpoint")
    private String endpoint;

    @JsonProperty(value = "title")
    private HttpStatusCode errorTitle;

    @JsonProperty(value = "status")
    private int errorStatus;

    @JsonProperty(value = "message")
    private String errorMessage;

    @JsonProperty(value = "downstream-error-detail")
    private Object errorBody;

    @JsonProperty(value = "correlationId")
    private String correlationId;
}
