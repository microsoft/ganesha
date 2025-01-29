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

import java.util.List;


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

        if(config.getAZURE_CLIENT_ID() != null && config.getAZURE_CLIENT_ID().isEmpty()) {
            credential = new ClientSecretCredentialBuilder()
                .clientId(config.getAZURE_CLIENT_ID())
                .clientSecret(config.getAZURE_CLIENT_SECRET())
                .tenantId(config.getAZURE_TENANT_ID())
                .build();
        } else {
            var builder = new DefaultAzureCredentialBuilder();

            if (config.getAZURE_TENANT_ID() != null && config.getAZURE_TENANT_ID().isEmpty()) {
                builder.tenantId(config.getAZURE_TENANT_ID());
            }

            credential = builder.build();
        }

        CosmosAsyncClient client;

        client = new CosmosClientBuilder()            
            .credential(credential)
            .endpoint(config.getAZURE_COSMOS_URI())      
            .directMode()
            // .database(config.getAZURE_COSMOS_DATABASE())
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

        return new CosmosTemplate(client, config.getAZURE_COSMOS_DATABASE(), null, null);
    }
}