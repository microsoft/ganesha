package com.microsoft.ganesha.services;

import com.microsoft.ganesha.config.AppConfig;
import com.microsoft.ganesha.data.ChatMessageTextContentCodec;
import com.microsoft.ganesha.data.OpenAIChatMessageContentCodec;
import com.microsoft.ganesha.interfaces.MongoService;
import com.microsoft.ganesha.models.Conversation;
import com.microsoft.semantickernel.services.chatcompletion.message.ChatMessageTextContent;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.bson.codecs.IntegerCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
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
        MongoDatabase database = mongoClient.getDatabase(config.getAzureCosmosDatabase());
        MongoCollection<Document> collection = database.getCollection(config.getAzureCosmosCollection());
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
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                    CodecRegistries.fromCodecs(new ChatMessageTextContentCodec(), new OpenAIChatMessageContentCodec()),
                    MongoClientSettings.getDefaultCodecRegistry());

        MongoDatabase database = mongoClient.getDatabase(config.getAzureCosmosDatabase());
        MongoCollection<Document> collection = database.getCollection(config.getAzureCosmosCollection()).withCodecRegistry(codecRegistry);

        Document document = new Document("conversationId", conversation.getConversationId().toString())
                .append("chatHistory",
                        conversation.getChatHistory());

        collection.replaceOne(Filters.eq("conversationId", conversation.getConversationId().toString()), document,
                new ReplaceOptions().upsert(true));
    }

    @Override
    public void DeleteConversation(UUID conversationId) throws Exception {
        MongoDatabase database = mongoClient.getDatabase(config.getAzureCosmosDatabase());
        MongoCollection<Document> collection = database.getCollection(config.getAzureCosmosCollection());
        collection.deleteOne(Filters.eq("conversationId", conversationId.toString()));
    }

    @Override
    public List<Conversation> GetConversations() throws Exception {
        MongoDatabase database = mongoClient.getDatabase(config.getAzureCosmosDatabase());
        MongoCollection<Document> collection = database.getCollection(config.getAzureCosmosCollection());

        List<Conversation> conversations = collection.find().map(document -> new Conversation(document)).into(new ArrayList<>());
        return conversations;
    }
}
