package com.eapi.helper;

import com.eapi.response.DownstreamErrorResponse;
import com.eapi.response.ErrorResponse;
import com.eapi.utils.JSONUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class ErrorMappingHelper {

    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Method maps ErrorResponse
     *
     * @param uri
     * @param response
     * @param error
     * @return ErrorResponse
     */
    public ErrorResponse mapErrorObject(
            String correlationId, String uri, ClientResponse response, Object error) {

        logger.info("Mapping Error RestClient.mapErrorObject for request correlation id :: {}", correlationId);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .endpoint(uri)
                .errorTitle(response.statusCode())
                .errorStatus(response.statusCode().value())
                .correlationId(correlationId)
                .build();

        String json = JSONUtil.convertObjectToJson(error);
        JSONObject errObj = new JSONObject(json);

        if (errObj.has("correlationId") && errObj.has("errors")) {

            DownstreamErrorResponse errorObj =
                    JSONUtil.convertJsonToObject(json, DownstreamErrorResponse.class);

            String errMsg = errorObj.getErrors().stream()
                    .map(s -> s.getMessage()).collect(Collectors.joining(","));

            DownstreamErrorResponse errResponse = DownstreamErrorResponse.builder()
                    .correlationId(errorObj.getCorrelationId())
                    .timeStamp(errorObj.getTimeStamp())
                    .message(errMsg)
                    .exception(errorObj.getException())
                    .build();

            errorResponse.setErrorBody(errResponse);

        } else if (errObj.has("error_description")) {
            String errorDesc = errObj.get("error_description").toString();
            DownstreamErrorResponse errResponse = DownstreamErrorResponse.builder()
                    .message(errorDesc)
                    .build();

            errorResponse.setErrorBody(errResponse);
        } else {
            errorResponse.setErrorBody(errObj);
        }

        logger.info("Returning error response from RestClient.mapErrorObject");

        return errorResponse;
    }
}
