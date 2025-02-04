package com.microsoft.ganesha.plugins;

import com.microsoft.ganesha.config.AppConfig;
import com.microsoft.ganesha.helper.TokenHelper;
import com.microsoft.ganesha.request.PrescriptionDetailsRequest;
import com.microsoft.ganesha.request.common.model.SearchInputMetaData;
import com.microsoft.ganesha.response.model.prescriptiondetails.PrescriptionSearchResponse;
import com.microsoft.ganesha.rest.RestClient;
import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;

import org.springframework.http.*;
import org.springframework.stereotype.Component;


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
    public PrescriptionSearchResponse getPrescriptions(String patientId, String correlationId) {

        String token = tokenHelper.getHemiAccessToken("correlationId");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        PrescriptionDetailsRequest request = new PrescriptionDetailsRequest();
        request.setPatientId("124027968");
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
        

        try {  
            ResponseEntity<PrescriptionSearchResponse> responseEntity = restClient  
                .restPostCall(appConfig.getHemiPrescriptionSearchEndpoint(), correlationId, request, headers, PrescriptionDetailsRequest.class, PrescriptionSearchResponse.class)  
                .block(); 
  
            if (responseEntity != null && responseEntity.getBody() != null) {  
                return responseEntity.getBody(); 
            }  
        } catch (Exception e) {  
            e.printStackTrace();
        }  
  
        return null;  
    }
}  