package com.eapi.getaccesstoken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GetAccessToken {

    private static final Logger log = LoggerFactory.getLogger(GetAccessToken.class);

    private RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders headers = new HttpHeaders();

    @Value("${StargateAccessNavTokenEndpoint}")
    private String tokenEndPoint;

    @Value("${StargateAccessNavTokenRequest}")
    private String accessTokenRequest;

    public String getToken() {
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> httpEntity = new HttpEntity<String>(accessTokenRequest, headers);
        ResponseEntity<StargateAccessToken> Response = restTemplate.exchange(tokenEndPoint, HttpMethod.POST,
                httpEntity, StargateAccessToken.class);
        StargateAccessToken response = Response.getBody();
        String token = "";
        if (response != null && response.getAccess_token() != null) {
            token = response.getAccess_token();
        }
        return token;
    }

}