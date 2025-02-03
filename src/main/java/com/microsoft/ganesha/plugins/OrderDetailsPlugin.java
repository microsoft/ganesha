package com.microsoft.ganesha.plugins;

import com.microsoft.ganesha.config.AppConfig;
import com.microsoft.ganesha.helper.TokenHelper;
import com.microsoft.ganesha.request.OrderDetailsSearchRequest;
import com.microsoft.ganesha.request.common.model.SearchInputMetaData;
import com.microsoft.ganesha.response.model.orderdetailssearch.OrderDetailsSearchResponse;
import com.microsoft.ganesha.rest.RestClient;
import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;

import reactor.core.publisher.Mono;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class OrderDetailsPlugin {

    private final AppConfig appConfig;
    private final TokenHelper tokenHelper;
    private final RestClient restClient;

    public OrderDetailsPlugin(AppConfig appConfig, TokenHelper tokenHelper, RestClient restClient) {
        this.appConfig = appConfig;
        this.tokenHelper = tokenHelper;
        this.restClient = restClient;
    }

    @DefineKernelFunction(name = "getOrderDetails", description = "Gets details of orders relating to the caller via patientId")
    @Async  
    public CompletableFuture<OrderDetailsSearchResponse> getOrderDetails(String patientId, String correlationId) {
        String token = tokenHelper.getHemiAccessToken(correlationId);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        OrderDetailsSearchRequest request = new OrderDetailsSearchRequest();
        request.setPatientId(patientId);
        request.setInclude(Arrays.asList("all"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        request.setStartDate(LocalDate.now().minusDays(7).format(formatter));
        request.setEndDate(LocalDate.now().format(formatter));
        
        request.setIncludeOrderTypes("ALL");
        request.setIncludeTrackingDetails("Y");
        request.setIncludeOnlyLatestOrderForRx("Y");
        request.setIncludeStudyRx("Y");

        SearchInputMetaData searchInputMetaData = new SearchInputMetaData();
            searchInputMetaData.setApplicationId("RXP");
            searchInputMetaData.setCorrelationId(correlationId);
            searchInputMetaData.setCustomerId("HEMI");
            searchInputMetaData.setUserId("HEMI");

        request.setSearchInputMetaData(searchInputMetaData);
        
        CompletableFuture<OrderDetailsSearchResponse> result = null;

        try {
            Mono<ResponseEntity<OrderDetailsSearchResponse>> monoResponseEntity = restClient.restPostCall(appConfig.getHemiOrderDetailsEndpoint(), "correlationId", request, headers, OrderDetailsSearchRequest.class, OrderDetailsSearchResponse.class);
            result = monoResponseEntity.filter(m -> Objects.nonNull(m.getBody())).map(HttpEntity::getBody).toFuture();
            System.out.println("Getting order details");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.failedFuture(e);        
        }
    }
}