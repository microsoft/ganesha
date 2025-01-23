package com.eapi.helper;

import com.eapi.response.HemiAccessTokenResponse;
import com.eapi.response.StargateAccessTokenResponse;
import com.eapi.token.AccessTokenService;
import com.eapi.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TokenHelper {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("stargateAccessTokenResponse")
    private StargateAccessTokenResponse stargateAccessToken;

    @Autowired
    @Qualifier("hemiACQAccessTokenResponse")
    private HemiAccessTokenResponse hemiACQAccessTokenResponse;

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

        if (hemiACQAccessTokenResponse.getExpiresOn() != 0
                && timestamp < hemiACQAccessTokenResponse.getExpiresOn()) {
            logger.info("Returning existing unexpired Hemi Token value");
            return hemiACQAccessTokenResponse.getAccessToken();

        } else {
            logger.info("Hemi Token expired, Generating new Hemi Token value");
            HemiAccessTokenResponse optionalObj = accessTokenService.getHemiAccessToken(correlationId);

            hemiACQAccessTokenResponse.setTokenType(optionalObj.getTokenType());
            hemiACQAccessTokenResponse.setResource(optionalObj.getResource());
            hemiACQAccessTokenResponse.setExpiresIn(optionalObj.getExpiresIn());
            hemiACQAccessTokenResponse.setExpiresOn(optionalObj.getExpiresOn());
            hemiACQAccessTokenResponse.setExtExpiresIn(optionalObj.getExtExpiresIn());
            hemiACQAccessTokenResponse.setNotBefore(optionalObj.getNotBefore());
            hemiACQAccessTokenResponse.setAccessToken(optionalObj.getAccessToken());
            return optionalObj.getAccessToken();
        }
    }

    /**
     * Method processes Stargate token information and validates token expiry
     *
     * @param correlationId
     * @return String
     */
    public String getStargateAccessToken(String correlationId) {
        logger.info("Processing Stargate Token inside TokenHelper.getStargateAccessToken() for request "
                + "correlationId :: {}", correlationId);

        //long timestamp = DateUtil.getCurrentEpochTimestamp() + 600;
        StargateAccessTokenResponse optionalObj = accessTokenService.getStargateAccessToken(correlationId);

        stargateAccessToken.setTokenType(optionalObj.getTokenType());
        stargateAccessToken.setExpiresIn(optionalObj.getExpiresIn());
        stargateAccessToken.setGeneratedOn(DateUtil.getCurrentEpochTimestamp());
        stargateAccessToken.setAccessToken(optionalObj.getAccessToken());
        return optionalObj.getAccessToken();
    }
}
