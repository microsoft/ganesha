package com.eapi.filter;

import com.eapi.constants.APIConstants;
// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

@Component
public class WebClientFilter {

    // private static final Logger LOGGER = LogManager.getLogger(WebClientFilter.class);

    /**
     * Filter to log incoming request
     *
     * @return ExchangeFilterFunction
     */
    public ExchangeFilterFunction requestFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            // LOGGER.info("Inside requestFilter()");

            ClientRequest.Builder requestBuilder = ClientRequest.from(clientRequest)
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

            if (clientRequest.url().toString().endsWith(APIConstants.TOKEN)) {
                requestBuilder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            } else {
                requestBuilder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            }

            ClientRequest request = requestBuilder.build();

            // request.headers().forEach((key, value) -> LOGGER.info("Request Header Key :: {} , Value :: {}", key, value));

            // LOGGER.info("Request URL :: {}", request.url());
            // LOGGER.info("Request Method :: {}", request.method());

            return Mono.just(request);
        });
    }

    /**
     * Filter to log response status of request
     *
     * @return ExchangeFilterFunction
     */
    public ExchangeFilterFunction responseFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            // LOGGER.info("RESPONSE CODE:: {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }
}
