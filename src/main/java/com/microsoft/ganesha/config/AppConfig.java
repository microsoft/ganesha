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

    @Value("${CLIENT_ENDPOINT}")
    private String clientEndpoint;

    @Value("${MODEL_ID}")
    private String modelId;

    @Value("${AZURE_CLIENT_ID}")
    private String azureClientId;

    @Value("${AZURE_TENANT_ID}")
    private String azureTenantId;

    @Value("${AZURE_CLIENT_SECRET}")
    private String azureClientSecret;

    @Value("${AZURE_PROJECT_ID}")
    private String projectId;

    @Value("${HemiOrderDetailsEndpoint}")
    private String hemiOrderDetailsEndpoint;

    @Value("${HemiPrescriptionSearchEndpoint}")
    private String hemiPrescriptionSearchEndpoint;
    @Value("${AZURE_COSMOS_CONN_STR}")
    private String azureCosmosConnString;

    @Value("${AZURE_COSMOS_COLLECTION}")
    private String azureCosmosCollection;

    @Value("${AZURE_COSMOS_DATABASE}")
    private String azureCosmosDatabase;

    @Value("${mongoServiceName}")
    private String mongoServiceName;


    // @Value("${mongoServiceName:inMemory}")
    // private String mongoServiceName;

    // @Value("${AZURE_COSMOS_CONN_STR:mongodb+srv://u:p@somecluster.mongocluster.cosmos.azure.com}")
    // private String azureCosmosConnString;

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
    
    public String getAzureCosmosConnString() {
        return azureCosmosConnString;
    }

    public String getAzureCosmosCollection() {
        return azureCosmosCollection;
    }

    public String getAzureCosmosDatabase() {
        return azureCosmosDatabase;
    }

    public String getMongoServiceName() {
        return mongoServiceName;
    }
}
