package com.microsoft.ganesha.plugins;

import com.microsoft.ganesha.config.AppConfig;
import com.microsoft.ganesha.helper.TokenHelper;
import com.microsoft.ganesha.request.PrescriptionDetailsRequest;
import com.microsoft.ganesha.request.common.model.SearchInputMetaData;
import com.microsoft.ganesha.response.model.prescriptiondetails.PrescriptionSearchResponse;
import com.microsoft.ganesha.rest.RestClient;
import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;

import reactor.core.publisher.Mono;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public class PrescriptionSearchPlugin {

    private final AppConfig appConfig;
    private final TokenHelper tokenHelper;
    private final RestClient restClient;

    public PrescriptionSearchPlugin(AppConfig appConfig, TokenHelper tokenHelper, RestClient restClient) {
        this.appConfig = appConfig;
        this.tokenHelper = tokenHelper;
        this.restClient = restClient;
    }

    @DefineKernelFunction(name = "getPrescriptions", description = "Gets details of patient's prescriptions which provides information of current prescriptions")
    @Async  
    public CompletableFuture<PrescriptionSearchResponse>  getPrescriptions(String patientId, String correlationId) {

        String token = tokenHelper.getHemiAccessToken("test");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        PrescriptionDetailsRequest request = new PrescriptionDetailsRequest();
        request.setPatientId("124028720");
        request.setInclude(new String[]{"all"});
        request.setPrescriptions(new String[]{});
        request.setIncludeInactivePatients("Y");
        request.setIncludeExpiredPrescriptions("Y");
        request.setIncludeDiscontinuedPrescriptions("Y");
        request.setIncludeRefillablePrescriptions("Y");
        request.setIncludeRenewablePrescriptions("Y");
        request.setIncludeOpenOrderPrescriptions("Y");
        request.setIncludeDuplicateGpi("Y");
        request.setIncludeStudyRx("Y");
        request.setFilterPrescriptionTypes(null);
        request.setIdentity(null);


        SearchInputMetaData searchInputMetaData = new SearchInputMetaData();
            searchInputMetaData.setApplicationId("ORXEPE");
            searchInputMetaData.setCorrelationId("Test123");
            searchInputMetaData.setCustomerId(null);
            searchInputMetaData.setUserId(null);

        request.setSearchInputMetaData(searchInputMetaData);
        
        CompletableFuture<PrescriptionSearchResponse> result = null;

        try {
            Mono<ResponseEntity<PrescriptionSearchResponse>> monoResponseEntity = restClient.restPostCall(appConfig.getHemiPrescriptionSearchEndpoint(), "correlationId", request, headers, PrescriptionDetailsRequest.class, PrescriptionSearchResponse.class);
            result = monoResponseEntity.filter(m -> Objects.nonNull(m.getBody())).map(HttpEntity::getBody).toFuture();
            System.out.println("Getting prescriptions");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.failedFuture(e);        
        }
    }
}