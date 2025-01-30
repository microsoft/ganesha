package com.microsoft.ganesha.controller;

import com.azure.data.cosmos.CosmosClientException;
// import com.azure.spring.data.cosmos.core.CosmosTemplate;

import com.microsoft.ganesha.config.AppConfig;
import com.microsoft.ganesha.mongodb.MongoDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class MongoDBController {
    
    @Autowired
    private AppConfig config;


    @GetMapping("/findbyid")
    String findById(@RequestParam String param) throws CosmosClientException {

        MongoDB mongoDb = new MongoDB(config);
        String result = mongoDb.GetMongoResults(param).toString();
        
        return result;

    }

    // public <T> List<T> findAll(Class<T> entityClass) {
    //     return cosmosTemplate.findAll(entityClass);
    // }

    // public <T> T save(T entity) {
    //     return cosmosTemplate.insert(entity);
    // }

    // public void deleteById(String id, Class<?> entityClass) {
    //     cosmosTemplate.deleteById(id, entityClass);
    // }
}