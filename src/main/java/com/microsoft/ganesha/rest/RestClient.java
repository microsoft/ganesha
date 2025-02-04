package com.microsoft.ganesha.rest;

import com.microsoft.ganesha.constant.APIConstants;
import com.microsoft.ganesha.exception.RuntimeRestClientErrorException;
import com.microsoft.ganesha.exception.RuntimeRestServerErrorException;
import com.microsoft.ganesha.helper.ErrorMappingHelper;
import com.microsoft.ganesha.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Rest Client utility helper class to make api calls using webclient
 */
@Component
public class RestClient {

    // private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private ErrorMappingHelper errorMappingHelper;

    @Autowired
    private WebClient webClient;

    /**
     * Generic method handles post rest api calls
     *
     * @param <T> request class type
     * @param <K> response class type
     * @param uri
     * @param reqId
     * @param reqBody
     * @param httpHeaders
     * @param reqType
     * @param resType
     * @return Mono<ResponseEntity < K>>
     */
    public <T, K> Mono<ResponseEntity<K>> restPostCall(
            String uri, String reqId, T reqBody, HttpHeaders httpHeaders, Class<T> reqType, Class<K> resType) {

        // logger.info("Inside restPostCall for making post api call having request correlation id - {}", reqId);

        return this.webClient.post().uri(uri)
                .headers(header -> header.addAll(httpHeaders))
                .body(Mono.just(reqBody), reqType).retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    // logger.error("Client error received inside RestClient.postRestCall method");
        
                    return response.bodyToMono(Object.class).flatMap(e -> {
                        ErrorResponse errorResponse = errorMappingHelper.mapErrorObject(reqId, uri, response, e);
                        
                        if (null != errorResponse) {                        
                            errorResponse.setErrorMessage(
                                String.format(APIConstants.EXCEPTION_OCCURRED_MSG, this.getClass().getCanonicalName(),
                                        "restPostCall()"));
                            
                            return Mono.error(new RuntimeRestClientErrorException(errorResponse));
                        }                      
                        return Mono.empty();                      
                    });
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    // logger.error("Server error received inside RestClient.postRestCall method");
                    
                    return response.bodyToMono(Object.class).flatMap(e -> {
                        ErrorResponse errorResponse = errorMappingHelper.mapErrorObject(reqId, uri, response, e);
                        
                        if (null != errorResponse) {                        
                            errorResponse.setErrorMessage(
                                String.format(APIConstants.EXCEPTION_OCCURRED_MSG, this.getClass().getCanonicalName(),
                                        "restPostCall()"));
                            
                            return Mono.error(new RuntimeRestServerErrorException(errorResponse));
                        }                      
                        return Mono.empty();                      
                    });
                })
                .toEntity(resType);
                //.retryWhen(Retry.backoff(3, Duration.ofMillis(250)));
    }
}
