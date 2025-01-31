package com.eapi.getaccesstoken;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StargateAccessToken {

	private String token_type;
	private int expires_in;
	private String access_token;

}