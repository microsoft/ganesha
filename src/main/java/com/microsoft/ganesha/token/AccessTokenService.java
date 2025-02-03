package com.microsoft.ganesha.token;

import com.microsoft.ganesha.response.HemiAccessTokenResponse;

/**
 * Access token generation service
 */
public interface AccessTokenService {

    /**
     * Method signature for the method to call Hemi access token generation service and returns
     * access token
     *
     * @param correlationId
     * @return HemiAccessTokenResponse
     */
    HemiAccessTokenResponse getHemiAccessToken(String correlationId);
}
