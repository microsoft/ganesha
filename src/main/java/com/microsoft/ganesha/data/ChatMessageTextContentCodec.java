package com.microsoft.ganesha.data;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.*;

import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.message.ChatMessageTextContent;

public class ChatMessageTextContentCodec implements Codec<ChatMessageTextContent> {
    @Override
    public void encode(BsonWriter writer, ChatMessageTextContent value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeString("authorRole", value.getAuthorRole().toString());
        writer.writeString("content", value.getContent());
        writer.writeString("encoding", value.getEncoding().toString());
        writer.writeEndDocument();
    }

    @Override
    public ChatMessageTextContent decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        AuthorRole role = AuthorRole.valueOf(reader.readString("role"));
        String content = reader.readString("content");
        reader.readEndDocument();
        
        return new ChatMessageTextContent(role, content, content, null, null);
    }

    @Override
    public Class<ChatMessageTextContent> getEncoderClass() {
        return ChatMessageTextContent.class;
    }
    
}
