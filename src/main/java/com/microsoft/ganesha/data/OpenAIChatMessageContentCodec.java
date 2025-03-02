package com.microsoft.ganesha.data;

import java.time.OffsetDateTime;

import org.bson.*;
import org.bson.codecs.*;

import com.azure.ai.openai.models.CompletionsUsage;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatMessageContent;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIFunctionToolCall;
import com.microsoft.semantickernel.orchestration.FunctionResultMetadata;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;


public class OpenAIChatMessageContentCodec implements Codec<OpenAIChatMessageContent> {
    private final Codec<OpenAIFunctionToolCall> functionToolCallCodec;
    private final Codec<FunctionResultMetadata> functionResultMetadataCodec;
    
    public OpenAIChatMessageContentCodec(Codec<OpenAIFunctionToolCall> functionToolCallCodec, Codec<FunctionResultMetadata> functionResultMetadataCodec) {
        this.functionToolCallCodec = functionToolCallCodec;
        this.functionResultMetadataCodec = functionResultMetadataCodec;
    }

    @Override
    public void encode(BsonWriter writer, OpenAIChatMessageContent value, EncoderContext encoderContext) {
        writer.writeStartDocument();

        if (value.getMetadata() != null && (value.getMetadata().getCreatedAt() != null || value.getMetadata().getUsage() != null)) {
            FunctionResultMetadata<?> metadata = null;
            CompletionsUsage usage = null;
            OffsetDateTime createdAt = null;

            writer.writeStartArray("metadata");            
            if (value.getMetadata().getCreatedAt() != null){                
                createdAt = (OffsetDateTime)value.getMetadata().getCreatedAt();                
            }                
            if (value.getMetadata().getUsage() != null){
                usage = (CompletionsUsage)value.getMetadata().getUsage();
            }            
            metadata = FunctionResultMetadata.build("metadata", usage, createdAt);
            functionResultMetadataCodec.encode(writer, metadata, encoderContext);
            writer.writeEndArray();
        }
        writer.writeString("authorRole", value.getAuthorRole().toString());
        writer.writeString("content", value.getContent() != null ? value.getContent() : "");
        writer.writeString("encoding", value.getEncoding().toString());
        if (value.getToolCall() != null && value.getToolCall().size() > 0) {
            writer.writeStartArray("toolCall");
            functionToolCallCodec.encode(writer, (OpenAIFunctionToolCall)value.getToolCall().get(0), encoderContext);
            writer.writeEndArray();
        }

        writer.writeEndDocument();
    }

    @Override
    public OpenAIChatMessageContent<?> decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        AuthorRole role = AuthorRole.valueOf(reader.readString("role"));
        String content = reader.readString("content");

        OffsetDateTime createdAt = OffsetDateTime.parse(reader.readString("createdAt"));
        
        Object usageObj = (Object)reader.readString("usage");
        CompletionsUsage usage = (CompletionsUsage) usageObj;
                     
        FunctionResultMetadata<?> metadata = FunctionResultMetadata.build(content, usage, createdAt);

        reader.readEndDocument();
        return new OpenAIChatMessageContent<>(role, content, null, null, null, metadata, null);
    }

    @Override
    public Class<OpenAIChatMessageContent> getEncoderClass() {        
        return OpenAIChatMessageContent.class;
    }
}
