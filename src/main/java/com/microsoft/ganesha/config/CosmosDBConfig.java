package com.microsoft.ganesha.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CosmosDBConfig {
    
    @Autowired
    private AppConfig config;
    
    @Bean
    public MongoClient mongoClient() {
        if (config.getAzureCosmosConnString() == null || config.getAzureCosmosConnString().isEmpty()) {
            return MongoClients.create();
        }
        ConnectionString connectionString = new ConnectionString(config.getAzureCosmosConnString());
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();

        return MongoClients.create(mongoClientSettings);        
    }
}
