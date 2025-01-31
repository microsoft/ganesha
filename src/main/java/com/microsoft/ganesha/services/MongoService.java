package com.microsoft.ganesha.services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Service;

@Service
public class MongoService {

    private final MongoClient mongoClient;

    public MongoService(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public MongoCollection<Document> getCollection(String databaseName, String collectionName) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        return database.getCollection(collectionName);
    }

    // Add methods to interact with the collection
}
