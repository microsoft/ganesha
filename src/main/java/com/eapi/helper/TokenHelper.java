package com.eapi.helper;

import com.eapi.response.HemiAccessTokenResponse;
import com.eapi.token.AccessTokenService;
import com.eapi.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenHelper {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HemiAccessTokenResponse hemiAccessToken;

    @Autowired
    private AccessTokenService accessTokenService;


    /**
     * Method processes Hemi token information and validates token expiry
     *
     * @param correlationId
     * @return String
     */
    public String getHemiAccessToken(String correlationId) {
        logger.info("Processing Hemi Token inside TokenHelper.getHemiAccessToken() for request "
                + "correlationId :: {}", correlationId);

        long timestamp = DateUtil.getCurrentEpochTimestamp() + 600;

        if (hemiAccessToken.getExpiresOn() != 0
                && timestamp < hemiAccessToken.getExpiresOn()) {
            logger.info("Returning existing unexpired Hemi Token value");
            return hemiAccessToken.getAccessToken();

        } else {
            logger.info("Hemi Token expired, Generating new Hemi Token value");
            HemiAccessTokenResponse optionalObj = accessTokenService.getHemiAccessToken(correlationId);

            hemiAccessToken.setTokenType(optionalObj.getTokenType());
            hemiAccessToken.setResource(optionalObj.getResource());
            hemiAccessToken.setExpiresIn(optionalObj.getExpiresIn());
            hemiAccessToken.setExpiresOn(optionalObj.getExpiresOn());
            hemiAccessToken.setExtExpiresIn(optionalObj.getExtExpiresIn());
            hemiAccessToken.setNotBefore(optionalObj.getNotBefore());
            hemiAccessToken.setAccessToken(optionalObj.getAccessToken());
            return optionalObj.getAccessToken();
        }
    }
}
