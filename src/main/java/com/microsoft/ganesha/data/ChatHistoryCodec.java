package com.microsoft.ganesha.data;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.BsonReader;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.codecs.BsonArrayCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatMessageContent;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.message.ChatMessageTextContent;

@SuppressWarnings("rawtypes")
public class ChatHistoryCodec implements Codec<ChatHistory> {

    private final Codec<BsonArray> bsonArrayCodec = new BsonArrayCodec();
    private final Codec<ChatMessageTextContent> chatMessageTextContentCodec;

    private final Codec<OpenAIChatMessageContent> openAIChatMessageContentCodec;

    public ChatHistoryCodec(CodecRegistry registry) {
        this.chatMessageTextContentCodec = registry.get(ChatMessageTextContent.class);
        this.openAIChatMessageContentCodec = registry.get(OpenAIChatMessageContent.class);
    }

    @Override
    public void encode(BsonWriter writer, ChatHistory value, EncoderContext encoderContext) {
        writer.writeStartArray("chatHistory");
        for (var message : value.getMessages()) {
            if (message instanceof ChatMessageTextContent) {
                chatMessageTextContentCodec.encode(writer, (ChatMessageTextContent) message, encoderContext);
            } else if (message instanceof OpenAIChatMessageContent) {
                openAIChatMessageContentCodec.encode(writer, (OpenAIChatMessageContent) message, encoderContext);
            }
        }
        writer.writeEndArray();
    }

    @Override
    public Class<ChatHistory> getEncoderClass() {
        return ChatHistory.class;
    }

    @Override
    public ChatHistory decode(BsonReader reader, DecoderContext decoderContext) {
        BsonArray messages = bsonArrayCodec.decode(reader, decoderContext);
        ChatHistory chatHistory = new ChatHistory();

        for (BsonValue message : messages) {
            BsonDocument m = message.asDocument();
            switch (m.getString("authorRole").getValue().toUpperCase()) {
                case "USER":
                    chatHistory.addUserMessage(m.getString("content").getValue());
                    break;
                case "SYSTEM":
                    chatHistory.addSystemMessage(m.getString("content").getValue());
                    break;
                case "ASSISTANT":
                case "TOOL":
                    var aiMsg = openAIChatMessageContentCodec.decode(new BsonDocumentReader(m), decoderContext);
                    chatHistory.addMessage(aiMsg);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown author role: " + m.getString("authorRole"));
            }
        }

        return chatHistory;
    }

}
