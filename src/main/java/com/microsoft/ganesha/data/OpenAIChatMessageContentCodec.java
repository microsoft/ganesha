package com.microsoft.ganesha.data;

import java.time.OffsetDateTime;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.bson.*;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistry;

import com.azure.ai.openai.models.CompletionsUsage;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatMessageContent;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIFunctionToolCall;
import com.microsoft.semantickernel.orchestration.FunctionResultMetadata;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;


@SuppressWarnings("rawtypes")
public class OpenAIChatMessageContentCodec implements Codec<OpenAIChatMessageContent> {
    private final Codec<OpenAIFunctionToolCall> functionToolCallCodec;
    private final Codec<FunctionResultMetadata> functionResultMetadataCodec;
    private final Codec<BsonArray> bsonArrayCodec = new BsonArrayCodec();

    public OpenAIChatMessageContentCodec(CodecRegistry registry) {
        this.functionToolCallCodec = registry.get(OpenAIFunctionToolCall.class);
        this.functionResultMetadataCodec = registry.get(FunctionResultMetadata.class);
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
            functionToolCallCodec.encode(writer, (OpenAIFunctionToolCall) value.getToolCall().get(0), encoderContext);
            writer.writeEndArray();
        }

        writer.writeEndDocument();
    }

    @Override
    public OpenAIChatMessageContent<?> decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        AuthorRole role = AuthorRole.valueOf(reader.readString("authorRole").toUpperCase());
        String content = reader.readString("content");
        Charset encoding = Charset.forName(reader.readString("encoding"));
        OffsetDateTime createdAt = OffsetDateTime.parse(reader.readString("createdAt"));
        
        Object usageObj = (Object)reader.readString("usage");
        CompletionsUsage usage = (CompletionsUsage) usageObj;
                     
        FunctionResultMetadata<?> metadata = FunctionResultMetadata.build(content, usage, createdAt);

        
        List<OpenAIFunctionToolCall> toolCalls = new ArrayList<OpenAIFunctionToolCall>();

        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            if (reader.readName().equals("toolCall")) {
                BsonArray tCalls = bsonArrayCodec.decode(reader, decoderContext);
                for (BsonValue tc : tCalls) {
                    BsonDocumentReader toolReader = new BsonDocumentReader(tc.asDocument());
                    OpenAIFunctionToolCall functionToolCall = functionToolCallCodec.decode(toolReader, decoderContext);
                    toolCalls.add(functionToolCall);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.readEndDocument();
        return new OpenAIChatMessageContent<>(role, content, null, null, encoding, metadata, toolCalls);
    }

    @Override
    public Class<OpenAIChatMessageContent> getEncoderClass() {
        return OpenAIChatMessageContent.class;
    }
}
