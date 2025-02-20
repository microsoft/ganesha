package com.microsoft.ganesha.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"token_type", "expires_in", "ext_expires_in", "expires_on", "not_before",
        "resource", "access_token"})
public class HemiAccessTokenResponse implements Serializable {

    private static final long serialVersionUID = 1357113859235203737L;

    @JsonProperty(value = "token_type")
    private String tokenType;
    
    @JsonProperty(value = "expires_in")
    private int expiresIn;
    
    @JsonProperty(value = "ext_expires_in")
    private int extExpiresIn;
    
    @JsonProperty(value = "expires_on")
    private int expiresOn;
    
    @JsonProperty(value = "not_before")
    private int notBefore;
    
    @JsonProperty(value = "resource")
    private String resource;
    
    @JsonProperty(value = "access_token")
    private String accessToken;
}
