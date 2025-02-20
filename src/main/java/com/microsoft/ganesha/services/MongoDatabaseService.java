package com.microsoft.ganesha.services;

import com.microsoft.ganesha.config.AppConfig;
import com.microsoft.ganesha.interfaces.MongoService;
import com.microsoft.ganesha.models.Conversation;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class MongoDatabaseService implements MongoService {
   
    
    private final MongoClient mongoClient;
    private final AppConfig config;

    public MongoDatabaseService(MongoClient mongoClient, AppConfig config) {
        this.mongoClient = mongoClient;
        this.config = config;
    }

    @Override
    public Conversation GetConversation(UUID conversationId) throws Exception {
        MongoDatabase database = mongoClient.getDatabase(config.getPereMongoDatabase());
        MongoCollection<Document> collection = database.getCollection(config.getPereMongoCollection());
        Document document = collection.find(Filters.eq("conversationId", conversationId.toString())).first();

        if (document != null) {
            // Convert the document to a Conversation object
            return new Conversation(document);
        } else {
            return null;
        }
    }

    @Override
    public void UpsertConversation(Conversation conversation) throws Exception {
        MongoDatabase database = mongoClient.getDatabase(config.getPereMongoDatabase());
        MongoCollection<Document> collection = database.getCollection(config.getPereMongoCollection());

        Document document = new Document("conversationId", conversation.getConversationId().toString())
                .append("messages",
                        conversation.getMessages().stream().map(message -> new Document("message", message.getMessage())
                                .append("role", message.getRole())
                                .append("time", message.getTime().toInstant())).toList());

        collection.replaceOne(Filters.eq("conversationId", conversation.getConversationId().toString()), document,
                new ReplaceOptions().upsert(true));
    }

    @Override
    public void DeleteConversation(UUID conversationId) throws Exception {
        MongoDatabase database = mongoClient.getDatabase(config.getPereMongoDatabase());
        MongoCollection<Document> collection = database.getCollection(config.getPereMongoCollection());
        collection.deleteOne(Filters.eq("conversationId", conversationId.toString()));
    }

    @Override
    public List<Conversation> GetConversations() throws Exception {
        MongoDatabase database = mongoClient.getDatabase(config.getPereMongoDatabase());
        MongoCollection<Document> collection = database.getCollection(config.getPereMongoCollection());

        List<Conversation> conversations = collection.find().map(document -> new Conversation(document)).into(new ArrayList<>());
        return conversations;
    }
}
