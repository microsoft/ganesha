package com.microsoft.ganesha.mongodb;

import com.azure.core.credential.TokenCredential;
import com.azure.cosmos.CosmosAsyncClient;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.data.cosmos.CosmosClientException;
import com.azure.spring.data.cosmos.core.CosmosTemplate;
import com.microsoft.ganesha.config.AppConfig;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;



@Repository
public class MongoDB {

    private final AppConfig config;

    public MongoDB(AppConfig config) {

        this.config = config;
    }


    public T GetMongoResults(String id) throws CosmosClientException{

        CosmosTemplate template = GetCosmosTemplate();

        Class<T> entityClass = null;
        T result = template.findById(id, entityClass);

        return result;

    }

    private CosmosAsyncClient InstantiateMongoClient() throws CosmosClientException {

        TokenCredential credential = null;

        if(config.getAzureClientId() != null && config.getAzureClientId().isEmpty()) {
            credential = new ClientSecretCredentialBuilder()
                .clientId(config.getAzureClientId())
                .clientSecret(config.getAzureClientSecret())
                .tenantId(config.getAzureTenantId())
                .build();
        } else {
            var builder = new DefaultAzureCredentialBuilder();

            if (config.getAzureTenantId() != null && config.getAzureTenantId().isEmpty()) {
                builder.tenantId(config.getAzureTenantId());
            }

            credential = builder.build();
        }

        CosmosAsyncClient client;

        client = new CosmosClientBuilder()            
            .credential(credential)
            .endpoint(config.getAzureCosmosURI())      
            .directMode()
            // .database(config.getAzureCosmosDatabase())
            .buildAsyncClient();

        return client;
  
    }

    public CosmosTemplate GetCosmosTemplate() {

        CosmosAsyncClient client = null;
        try {
            client = InstantiateMongoClient();
        } catch (CosmosClientException e) {
            e.printStackTrace();
        }

        return new CosmosTemplate(client, config.getAzureCosmosDatabase(), null, null);
    }
}