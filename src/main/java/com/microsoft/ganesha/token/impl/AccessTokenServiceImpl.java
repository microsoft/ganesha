package com.microsoft.ganesha.token.impl;

import com.microsoft.ganesha.config.AppPropertyConfig;
import com.microsoft.ganesha.constant.APIConstants;
import com.microsoft.ganesha.exception.CustomApplicationException;
import com.microsoft.ganesha.exception.RuntimeRestClientErrorException;
import com.microsoft.ganesha.response.ErrorResponse;
import com.microsoft.ganesha.response.HemiAccessTokenResponse;
import com.microsoft.ganesha.rest.RestClient;
import com.microsoft.ganesha.token.AccessTokenService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private RestClient restClient;

    @Autowired
    private AppPropertyConfig appPropertyConfig;

    @Override
    public HemiAccessTokenResponse getHemiAccessToken(String correlationId) {
        logger.info("Inside getHemiAccessToken() calling hemi token generation API");
        HemiAccessTokenResponse token = null;

        try {
            Mono<ResponseEntity<HemiAccessTokenResponse>> monoResponseEntity =
                    restClient.restPostCall(appPropertyConfig.getHemiAccessTokenEndpoint(), correlationId,
                            appPropertyConfig.getHemiAccessTokenRequest(), new HttpHeaders(), String.class,
                            HemiAccessTokenResponse.class);

            Optional<ResponseEntity<HemiAccessTokenResponse>> optionalObj = monoResponseEntity.blockOptional();

            if (optionalObj.isPresent() && null != optionalObj.get() && null != optionalObj.get().getBody()) {
                token = optionalObj.get().getBody();
            }

        } catch (RuntimeRestClientErrorException ex) {
            logger.error("Runtime Client Error exception encountered inside getHemiAccessToken(), "
                    + "Error Message :: {}", ex.getErrorResponse());

            ex.getErrorResponse().setErrorMessage(
                    String.format(APIConstants.EXCEPTION_OCCURRED_MSG, this.getClass().getCanonicalName(),
                            "getHemiAccessToken()"));

            throw new RuntimeRestClientErrorException(ex.getErrorResponse());

        } catch (Exception ex) {
            logger.error("Error encountered inside getHemiAccessToken(), Error Message :: {}, "
                    + "Exception :: {}", ex.getMessage(), ex);

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .endpoint(appPropertyConfig.getHemiAccessTokenEndpoint())
                    .errorTitle(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errorMessage(ex.getMessage())
                    .correlationId(null)
                    .build();
            throw new CustomApplicationException(errorResponse);
        }
        return token;
    }

}
