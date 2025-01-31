package com.microsoft.ganesha.plugins;

import com.eapi.helper.TokenHelper;
import com.microsoft.ganesha.config.AppConfig;
import com.microsoft.ganesha.request.OrderDetailsSearchRequest;
import com.microsoft.ganesha.request.common.model.SearchInputMetaData;
import com.microsoft.ganesha.response.model.orderdetailssearch.OrderDetailsSearchResponse;
import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;

import reactor.core.publisher.Mono;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.WebClient;
import com.eapi.rest.RestClient;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailsPlugin {

    @Autowired
    private AppConfig AppConfig;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private RestClient restClient;

    @DefineKernelFunction(name = "getOrderDetails", description = "Gets details of orders relating to the caller")
    public Mono<OrderDetailsSearchResponse> getOrderDetails(String patientId) {

        String token = tokenHelper.getHemiAccessToken("correlationId");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        OrderDetailsSearchRequest request = new OrderDetailsSearchRequest();
        // Set the necessary fields in the request object
        // request.setPatientId(patientId);
        request.setPatientId("124027968");
        request.setInclude(Arrays.asList("all"));
        request.setStartDate("2023-06-15");
        request.setEndDate("2023-06-16");
        request.setIncludeOrderTypes("ALL");
        request.setIncludeTrackingDetails("Y");
        request.setIncludeOnlyLatestOrderForRx("Y");
        request.setIncludeStudyRx("Y");

        SearchInputMetaData searchInputMetaData = new SearchInputMetaData();
            searchInputMetaData.setApplicationId("RXP");
            searchInputMetaData.setCorrelationId(null);
            searchInputMetaData.setCustomerId("HEMI");
            searchInputMetaData.setUserId("HEMI");

        request.setSearchInputMetaData(searchInputMetaData);

                


        // return webClientBuilder.build()
        //         .post()
        //         .uri("https://stghemi2-cloud.optum.com/v2.0/order/detail/search")
        //         .headers(httpHeaders -> {
        //             httpHeaders.addAll(headers);
        //         })
        //         .bodyValue(request)
        //         .retrieve()
        //         .bodyToMono(OrderDetailsSearchResponse.class);
        Mono<ResponseEntity<OrderDetailsSearchResponse>> monoResponseEntity =
        restClient.restPostCall(AppConfig.getHemiOrderDetailsEndpoint(), "correlationId",
                request, headers, OrderDetailsSearchRequest.class, OrderDetailsSearchResponse.class);

        return monoResponseEntity.filter(m -> Objects.nonNull(m.getBody())).map(HttpEntity::getBody);

    }
}