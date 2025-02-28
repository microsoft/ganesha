package com.microsoft.ganesha.services;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.context.annotation.Configuration;

import com.microsoft.ganesha.config.AppConfig;
import com.microsoft.ganesha.data.CustomCodecProvider;
import com.microsoft.ganesha.interfaces.MongoService;
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
                    CodecRegistries.fromProviders(new CustomCodecProvider()),
                    MongoClientSettings.getDefaultCodecRegistry());
        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(codecRegistry)
                .build();
        return new MongoDatabaseService(MongoClients.create(settings), config);
    }
}
