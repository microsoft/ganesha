package com.microsoft.ganesha.data;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import com.microsoft.ganesha.models.Conversation;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatMessageContent;
import com.microsoft.semantickernel.orchestration.FunctionResultMetadata;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;


public class CustomCodecProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == OpenAIChatMessageContent.class) {
            return (Codec<T>) new OpenAIChatMessageContentCodec(registry);
        } else if (clazz == Conversation.class) {
            return (Codec<T>) new ConversationCodec(registry);
        } else if (clazz == ChatHistory.class) {
            return (Codec<T>) new ChatHistoryCodec(registry);
        } else if (clazz == FunctionResultMetadata.class) {
            return (Codec<T>) new FunctionResultMetadataContentCodec();
        }

        return null;
    }    
    
}
