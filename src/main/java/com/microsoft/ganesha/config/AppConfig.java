package com.microsoft.ganesha.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

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

    @Value("${AZURE_COSMOS_URI}")
    private String azureCosmosURI;

    @Value("${AZURE_COSMOS_KEY}")
    private String azureCosmosKey;

    @Value("${AZURE_COSMOS_DATABASE}")
    private String azureCosmosDatabase;

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
    
    public String getAzureCosmosURI() {
        return azureCosmosURI;
    }

    public String getAzureCosmosKey() {
        return azureCosmosKey;
    }

    public String getAzureCosmosDatabase() {
        return azureCosmosDatabase;
    }

}
