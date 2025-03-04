package com.microsoft.ganesha.data;

import java.time.OffsetDateTime;
import java.io.StringReader;
import java.io.IOException;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.BsonType;
import org.bson.codecs.*;

import com.microsoft.semantickernel.orchestration.FunctionResultMetadata;
import com.azure.ai.openai.models.CompletionsUsage;
import com.azure.json.JsonReader;
import com.azure.json.JsonOptions;
import com.azure.json.JsonProviders;

@SuppressWarnings("rawtypes")
public class FunctionResultMetadataContentCodec implements Codec<FunctionResultMetadata> {
    @SuppressWarnings("null")
    @Override
    public void encode(BsonWriter writer, FunctionResultMetadata value, EncoderContext encoderContext) {
        CompletionsUsage usage = null;
        writer.writeStartArray("metadata");
        writer.writeStartDocument();
        if (value.getCreatedAt() != null) {
            writer.writeString("createdAt", value.getCreatedAt().toString());
        }
        if (value.getId() != null) {
            writer.writeString("id", value.getId());
        }
        if (value.getUsage() != null) {
            usage = (CompletionsUsage) value.getUsage();
            writer.writeInt32("completionTokens", usage.getCompletionTokens());
            writer.writeInt32("promptTokens", usage.getPromptTokens());
            writer.writeInt32("totalTokens", usage.getTotalTokens());
        }
        writer.writeEndDocument();
        writer.writeEndArray();
    }

    @Override
    public FunctionResultMetadata decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        CompletionsUsage usage = null;
        OffsetDateTime createdAt = null;
        String id = "";
        int completionTokens = 0;
        int promptTokens = 0;
        int totalTokens = 0;
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            switch(reader.readName()) {
                case "completionTokens":
                    completionTokens = reader.readInt32();
                    break;
                case "promptTokens":
                    promptTokens = reader.readInt32();
                    break;
                case "totalTokens":
                    totalTokens = reader.readInt32();
                    break;
                case "createdAt":
                    createdAt = OffsetDateTime.parse(reader.readString());
                    break;
                case "id":
                    id = reader.readString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        
        reader.readEndDocument();

        String json = String.format("{\"completion_tokens\": %d, \"prompt_tokens\": %d, \"total_tokens\": %d}",
                completionTokens, promptTokens, totalTokens);
        JsonReader jsonReader = null;
        try {
            jsonReader = JsonProviders.createReader(new StringReader(json), new JsonOptions());
            usage = CompletionsUsage.fromJson(jsonReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return FunctionResultMetadata.build(id, usage, createdAt);
    }

    @Override
    public Class<FunctionResultMetadata> getEncoderClass() {
        return FunctionResultMetadata.class;
    }

}
