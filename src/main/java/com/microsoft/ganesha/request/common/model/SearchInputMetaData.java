package com.microsoft.ganesha.request.common.model;

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
@JsonPropertyOrder({"consumingAppId", "consumerAppId", "userId", "customerId", "applicationId", "consumerAppType", "consumerType",
        "transactionId", "correlationId"})
public class SearchInputMetaData implements Serializable {

    private static final long serialVersionUID = 7054581916843371876L;

    @JsonProperty(value = "consumingAppId")
    private String consumerAppId;

    @JsonProperty(value = "consumerAppId")
    private String consumerApplicationId;

    @JsonProperty(value = "userId")
    private String userId;

    @JsonProperty(value = "customerId")
    private String customerId;

    @JsonProperty(value = "applicationId")
    private String applicationId;

    @JsonProperty(value = "consumerAppType")
    private String consumerAppType;

    @JsonProperty(value = "consumerType")
    private String consumerType;

    @JsonProperty(value = "transactionId")
    private String transactionId;

    @JsonProperty(value = "correlationId")
    private String correlationId;
}
