package com.microsoft.ganesha.data;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatMessageContent;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIFunctionToolCall;
import com.microsoft.semantickernel.services.chatcompletion.message.ChatMessageTextContent;

public class CustomCodecProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == OpenAIChatMessageContent.class) {
            return (Codec<T>) new OpenAIChatMessageContentCodec(registry.get(OpenAIFunctionToolCall.class));
        } else if (clazz == ChatMessageTextContent.class) {
            return (Codec<T>) new ChatMessageTextContentCodec();
        } else if (clazz == OpenAIFunctionToolCall.class) {
            return (Codec<T>) new OpenAIFunctionToolCallCodec();
        }

        return null;
    }    
    
}
