package com.microsoft.ganesha.data;

import java.util.UUID;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

import com.microsoft.ganesha.models.Conversation;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;

public class ConversationCodec implements Codec<Conversation> {

    private final Codec<ChatHistory> chatHistoryCodec;

    public ConversationCodec(CodecRegistry registry) {
        this.chatHistoryCodec = registry.get(ChatHistory.class);
    }

    @Override
    public void encode(BsonWriter writer, Conversation value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeString("conversationId", value.getConversationId().toString());
        chatHistoryCodec.encode(writer, value.getChatHistory(), encoderContext);
        writer.writeEndDocument();
    }

    @Override
    public Class<Conversation> getEncoderClass() {
        return Conversation.class;
    }

    @Override
    public Conversation decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        reader.readObjectId("_id");
        String conversationId = reader.readString("conversationId");
        ChatHistory chatHistory = chatHistoryCodec.decode(reader, decoderContext);
        reader.readEndDocument();
        return new Conversation(UUID.fromString(conversationId), chatHistory);
    }
    
}
