package com.microsoft.ganesha.data;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.bson.*;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistry;

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

    @SuppressWarnings("null")
    @Override
    public void encode(BsonWriter writer, OpenAIChatMessageContent value, EncoderContext encoderContext) {
        writer.writeStartDocument();

        writer.writeString("authorRole", value.getAuthorRole().toString());
        writer.writeString("content", value.getContent() != null ? value.getContent() : "");
        writer.writeString("encoding", value.getEncoding() != null ? value.getEncoding().toString() : "");
        
        if (value.getToolCall() != null && value.getToolCall().size() > 0) {
            writer.writeStartArray("toolCall");
            for (var toolCall : value.getToolCall()) {                
                functionToolCallCodec.encode(writer, (OpenAIFunctionToolCall) toolCall, encoderContext);                
            }
            writer.writeEndArray();
        }

        if (value.getMetadata() != null && (value.getMetadata().getCreatedAt() != null || value.getMetadata().getUsage() != null || value.getMetadata().getId() != null)) {
            functionResultMetadataCodec.encode(writer, value.getMetadata(), encoderContext);
        }

        writer.writeEndDocument();
    }

    @Override
    public OpenAIChatMessageContent decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        AuthorRole role = AuthorRole.valueOf(reader.readString("authorRole").toUpperCase());
        String content = reader.readString("content");
        Charset encoding = Charset.forName(reader.readString("encoding"));
        
        
        List<OpenAIFunctionToolCall> toolCalls = new ArrayList<OpenAIFunctionToolCall>();
        List<FunctionResultMetadata> metadata = new ArrayList<FunctionResultMetadata>();

        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            switch (reader.readName()) {
            case "toolCall":
                BsonArray tCalls = bsonArrayCodec.decode(reader, decoderContext);
                for (BsonValue tc : tCalls) {
                    BsonDocumentReader toolReader = new BsonDocumentReader(tc.asDocument());
                    OpenAIFunctionToolCall functionToolCall = functionToolCallCodec.decode(toolReader, decoderContext);
                    toolCalls.add(functionToolCall);
                }
                break;
            case "metadata":
                BsonArray mData = bsonArrayCodec.decode(reader, decoderContext);
                for (BsonValue md : mData) {
                    BsonDocumentReader metaReader = new BsonDocumentReader(md.asDocument());
                    FunctionResultMetadata functionResultMetadata = functionResultMetadataCodec.decode(metaReader, decoderContext);
                    metadata.add(functionResultMetadata);
                }
                break;
            default:
                reader.skipValue();
                break;
            }
        }
        reader.readEndDocument();
        return new OpenAIChatMessageContent<>(role, content, content, null, encoding, (metadata != null && !metadata.isEmpty()) ? metadata.get(0) : null, toolCalls);
    }

    @Override
    public Class<OpenAIChatMessageContent> getEncoderClass() {
        return OpenAIChatMessageContent.class;
    }
}
