package com.eapi.token;

import com.eapi.response.HemiAccessTokenResponse;
import com.eapi.response.StargateAccessTokenResponse;

/**
 * Access token generation service
 */
public interface AccessTokenService {

    /**
     * Method signature for the method to call stargate access token generation service and returns
     * access token
     *
     * @param correlationId
     * @return StargateAccessTokenResponse
     */
    StargateAccessTokenResponse getStargateAccessToken(String correlationId);

    /**
     * Method signature for the method to call Hemi access token generation service and returns
     * access token
     *
     * @param correlationId
     * @return HemiAccessTokenResponse
     */
    HemiAccessTokenResponse getHemiAccessToken(String correlationId);
}
