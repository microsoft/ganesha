package com.eapi.token.impl;

import com.eapi.config.AppPropertyConfig;
import com.eapi.constants.APIConstants;
import com.eapi.exception.CustomApplicationException;
import com.eapi.exception.RuntimeRestClientErrorException;
import com.eapi.response.ErrorResponse;
import com.eapi.response.HemiAccessTokenResponse;
import com.eapi.response.StargateAccessTokenResponse;
import com.eapi.rest.RestClient;
import com.eapi.token.AccessTokenService;
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
    public StargateAccessTokenResponse getStargateAccessToken(String correlationId) {
        logger.info("Inside getStargateAccessToken() calling stargate token generation API");
        StargateAccessTokenResponse token = null;

        try {
            Mono<ResponseEntity<StargateAccessTokenResponse>> monoResponseEntity =
                    restClient.restPostCall(appPropertyConfig.getStargateAccessNavTokenEndpoint(), correlationId,
                            appPropertyConfig.getStargateAccessNavTokenRequest(), new HttpHeaders(), String.class,
                            StargateAccessTokenResponse.class);

            Optional<ResponseEntity<StargateAccessTokenResponse>> optionalObj = monoResponseEntity.blockOptional();

            if (optionalObj.isPresent() && null != optionalObj.get() && null != optionalObj.get().getBody()) {
                token = optionalObj.get().getBody();
            }

        } catch (RuntimeRestClientErrorException ex) {
            logger.error("Client Error exception encountered inside getStargateAccessToken(), Error Message :: {}",
                    ex.getErrorResponse().toString());

            ex.getErrorResponse().setErrorMessage(
                    String.format(APIConstants.EXCEPTION_OCCURRED_MSG, this.getClass().getCanonicalName(),
                            "getStargateAccessToken()"));

            throw new RuntimeRestClientErrorException(ex.getErrorResponse());

        } catch (Exception ex) {
            logger.error("Error encountered inside getStargateAccessToken(), Error Message :: {}, "
                    + "Exception :: {}", ex.getMessage(), ex);

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .endpoint(appPropertyConfig.getStargateAccessNavTokenEndpoint())
                    .errorTitle(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errorMessage(ex.getMessage())
                    .correlationId(null)
                    .build();
            throw new CustomApplicationException(errorResponse);
        }
        return token;
    }

    @Override
    public HemiAccessTokenResponse getHemiAccessToken(String correlationId) {
        logger.info("Inside getHemiAccessToken() calling hemi token generation API");
        HemiAccessTokenResponse token = null;

        try {
            Mono<ResponseEntity<HemiAccessTokenResponse>> monoResponseEntity =
                    restClient.restPostCall(appPropertyConfig.getHemiAccessTokenEndpoint(), correlationId,
                            appPropertyConfig.getHemiAccessACQTokenRequest(), new HttpHeaders(), String.class,
                            HemiAccessTokenResponse.class);

            Optional<ResponseEntity<HemiAccessTokenResponse>> optionalObj = monoResponseEntity.blockOptional();

            if (optionalObj.isPresent()) {
                optionalObj.get();
                if (null != optionalObj.get().getBody()) {
                    token = optionalObj.get().getBody();
                }
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
