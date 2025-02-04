package com.microsoft.ganesha.helper;

import com.microsoft.ganesha.response.HemiAccessTokenResponse;
import com.microsoft.ganesha.token.AccessTokenService;
import com.microsoft.ganesha.utils.DateUtil;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class TokenHelper {

    // private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final HemiAccessTokenResponse hemiAccessTokenResponse;
    private final AccessTokenService accessTokenService;

    public TokenHelper(@Qualifier("hemiAccessTokenResponse") HemiAccessTokenResponse hemiAccessTokenResponse, AccessTokenService accessTokenService) {
        this.hemiAccessTokenResponse = hemiAccessTokenResponse;
        this.accessTokenService = accessTokenService;
    }

    /**
     * Method processes Hemi token information and validates token expiry
     *
     * @param correlationId
     * @return String
     */
    public String getHemiAccessToken(String correlationId) {
        // logger.info("Processing Hemi Token inside TokenHelper.getHemiAccessToken() for request "
        //         + "correlationId :: {}", correlationId);

        long timestamp = DateUtil.getCurrentEpochTimestamp() + 600;

        if (hemiAccessTokenResponse.getExpiresOn() != 0
                && timestamp < hemiAccessTokenResponse.getExpiresOn()) {
            // logger.info("Returning existing unexpired Hemi Token value");
            return hemiAccessTokenResponse.getAccessToken();

        } else {
            // logger.info("Hemi Token expired, Generating new Hemi Token value");
            HemiAccessTokenResponse optionalObj = accessTokenService.getHemiAccessToken(correlationId);

            hemiAccessTokenResponse.setTokenType(optionalObj.getTokenType());
            hemiAccessTokenResponse.setResource(optionalObj.getResource());
            hemiAccessTokenResponse.setExpiresIn(optionalObj.getExpiresIn());
            hemiAccessTokenResponse.setExpiresOn(optionalObj.getExpiresOn());
            hemiAccessTokenResponse.setExtExpiresIn(optionalObj.getExtExpiresIn());
            hemiAccessTokenResponse.setNotBefore(optionalObj.getNotBefore());
            hemiAccessTokenResponse.setAccessToken(optionalObj.getAccessToken());
            return optionalObj.getAccessToken();
        }
    }
}
