package com.microsoft.ganesha.data;

import org.bson.*;
import org.bson.codecs.*;

import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatMessageContent;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;

public class OpenAIChatMessageContentCodec implements Codec<OpenAIChatMessageContent> {
    @Override
    public void encode(BsonWriter writer, OpenAIChatMessageContent value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeString("authorRole", value.getAuthorRole().toString());
        writer.writeString("content", value.getContent() != null ? value.getContent() : "");
        writer.writeString("encoding", value.getEncoding().toString());
        writer.writeEndDocument();
    }

    @Override
    public OpenAIChatMessageContent decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        AuthorRole role = AuthorRole.valueOf(reader.readString("role"));
        String content = reader.readString("content");
        reader.readEndDocument();
        return new OpenAIChatMessageContent<>(role, content, null, null, null, null, null);
    }

    @Override
    public Class<OpenAIChatMessageContent> getEncoderClass() {        
        return OpenAIChatMessageContent.class;
    }
}
