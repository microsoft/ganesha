package com.microsoft.ganesha.services;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.context.annotation.Configuration;

import com.microsoft.ganesha.config.AppConfig;
import com.microsoft.ganesha.data.ChatMessageTextContentCodec;
import com.microsoft.ganesha.data.CustomCodecProvider;
import com.microsoft.ganesha.data.OpenAIFunctionToolCallCodec;
import com.microsoft.ganesha.interfaces.MongoService;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoServiceFactory {

    private final AppConfig config;

    public MongoServiceFactory(AppConfig config) {
        this.config = config;
    }

    public MongoService create() {
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                CodecRegistries.fromCodecs(
                        new ChatMessageTextContentCodec(),
                        new OpenAIFunctionToolCallCodec()),
                CodecRegistries.fromProviders(new CustomCodecProvider()),
                MongoClientSettings.getDefaultCodecRegistry());
        ConnectionString connectionString = new ConnectionString(config.getAzureCosmosConnString());
        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(codecRegistry)
                .applyConnectionString(connectionString)
                .build();
        return new MongoDatabaseService(MongoClients.create(settings), config);
    }
}
