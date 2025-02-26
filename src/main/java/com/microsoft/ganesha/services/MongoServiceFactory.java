package com.microsoft.ganesha.services;

import org.springframework.context.annotation.Configuration;

import com.microsoft.ganesha.config.AppConfig;
import com.microsoft.ganesha.interfaces.MongoService;
import com.mongodb.client.MongoClient;

@Configuration
public class MongoServiceFactory {

    private final AppConfig config;
    private final MongoClient mongoClient;

    public MongoServiceFactory(MongoClient mongoClient, AppConfig config) {
        this.mongoClient = mongoClient;
        this.config = config;
    }

    
    public MongoService create() {
        String constring = config.getAzureCosmosConnString();
        if (constring == null || constring.trim().isEmpty()) {
            return new InMemoryMongoService();
        }else {
            return new MongoDatabaseService(mongoClient, config);
        } 
    }
}
