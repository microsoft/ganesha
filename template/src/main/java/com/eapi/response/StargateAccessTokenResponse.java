package com.eapi.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"token_type", "expires_in", "access_token"})
public class StargateAccessTokenResponse implements Serializable {

    private static final long serialVersionUID = 1066957045492900900L;

    @JsonProperty(value = "token_type")
    private String tokenType;
    
    @JsonProperty(value = "expires_in")
    private int expiresIn;
    
    @JsonIgnore
    private long generatedOn;
    
    @JsonProperty(value = "access_token")
    private String accessToken;
}