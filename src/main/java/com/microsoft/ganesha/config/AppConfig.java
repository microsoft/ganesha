package com.microsoft.ganesha.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.microsoft.ganesha.response.HemiAccessTokenResponse;

@Configuration
@Scope("singleton")
public class AppConfig {
    @Value("${AZURE_CLIENT_KEY}")
    private String azureClientKey;

    @Value("${UaisAzureOpenaiEndpoint}")
    private String clientEndpoint;

    @Value("${PERE_MODEL_ID}")
    private String modelId;

    @Value("${UaisAzureClientId}")
    private String azureClientId;

    @Value("${UaisAzureTenantId}")
    private String azureTenantId;

    @Value("${UaisAzureAppRegistrationClientSecret}")
    private String azureClientSecret;

    @Value("${PERE_PROJECT_ID}")
    private String projectId;

    @Value("${HemiOrderDetailsEndpoint}")
    private String hemiOrderDetailsEndpoint;

    @Value("${HemiPrescriptionSearchEndpoint}")
    private String hemiPrescriptionSearchEndpoint;

    @Value("${PERE_MONGO_COLLECTION}")
    private String pereMongoCollection;

    @Value("${PERE_MONGO_DATABASE}")
    private String pereMongoDatabase;

    @Value("${mongoServiceName}")
    private String mongoServiceName;

    @Value("${PereMongoConnStr}")
    private String pereMongoConnString;

    public String getAzureClientKey() {
        return azureClientKey;
    }

    public String getClientEndpoint() {
        return clientEndpoint;
    }

    public String getModelId() {
        return modelId;
    }

    public String getAzureClientId() {
        return azureClientId;
    }

    public String getAzureTenantId() {
        return azureTenantId;
    }

    public String getAzureClientSecret() {
        return azureClientSecret;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getHemiOrderDetailsEndpoint() {
        return hemiOrderDetailsEndpoint;
    }

    public String getHemiPrescriptionSearchEndpoint() {
        return hemiPrescriptionSearchEndpoint;
    }

    /**
     * HemiAccessTokenResponse Bean
     *
     * @return HemiAccessTokenResponse
     */
    @Bean
    public HemiAccessTokenResponse hemiAccessTokenResponse() {
        return new HemiAccessTokenResponse();
    }
    
    public String getPereMongoConnString() {
        return pereMongoConnString;
    }

    public String getPereMongoCollection() {
        return pereMongoCollection;
    }

    public String getPereMongoDatabase() {
        return pereMongoDatabase;
    }

    public String getMongoServiceName() {
        return mongoServiceName;
    }
}
