// package com.microsoft.ganesha.controller;

// import com.microsoft.ganesha.config.AppConfig;
// import com.microsoft.ganesha.interfaces.MongoService;
// import com.microsoft.ganesha.services.MongoDatabaseService;
// import com.mongodb.client.MongoCollection;
// import com.mongodb.client.MongoCursor;
// import com.mongodb.client.model.Filters;

// import org.bson.Document;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;

// import java.util.ArrayList;
// import java.util.List;

// @RestController
// public class MongoDBController {
    
//     @Autowired
//     private AppConfig config;
    
//     public MongoDBController(MongoService mongoService) {
//         this.mongoService = (MongoDatabaseService) mongoService;
//     }

//     private final MongoDatabaseService mongoService;

//     @GetMapping("/getFirst")
//     public String getFirstDocument() {
//         MongoCollection<Document> collection = mongoService.getCollection(config.getAzureCosmosDatabase(), config.getAzureCosmosCollection());
//         Document document = collection.find().first();
//         return document != null ? document.toJson() : "No documents found";
//     }

//     @GetMapping("/alldocs")
//     public List<String> getAllDocuments() {
//         MongoCollection<Document> collection = mongoService.getCollection(config.getAzureCosmosDatabase(), config.getAzureCosmosCollection());
//         List<String> documents = new ArrayList<>();
//         try (MongoCursor<Document> cursor = collection.find().iterator()) {
//             while (cursor.hasNext()) {
//                 documents.add(cursor.next().toJson());
//             }
//         }
//         return documents;
//     }

//     @GetMapping("/findbypages")
//     public List<String> findByPages(@RequestParam int pages) {
//         MongoCollection<Document> collection = mongoService.getCollection(config.getAzureCosmosDatabase(), config.getAzureCosmosCollection());
//         List<String> documents = new ArrayList<>();
//         try (MongoCursor<Document> cursor = collection.find(Filters.eq("pages", pages)).iterator()) {
//             while (cursor.hasNext()) {
//                 documents.add(cursor.next().toJson());
//             }
//         }
//         return documents;
//     }

//     @PostMapping("/upload")
//     public String uploadDocument(@RequestBody Document document) {
//         MongoCollection<Document> collection = mongoService.getCollection(config.getAzureCosmosDatabase(), config.getAzureCosmosCollection());
//         collection.insertOne(document);
//         return "Document uploaded successfully";
//     }
// }