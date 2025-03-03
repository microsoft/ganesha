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
import java.util.Map;
import java.util.HashMap;


@SuppressWarnings("rawtypes")
public class FunctionResultMetadataContentCodec implements Codec<FunctionResultMetadata> {
    @Override
    public void encode(BsonWriter writer, FunctionResultMetadata value, EncoderContext encoderContext) {
        CompletionsUsage usage = null;
        
        writer.writeString("createdAt", value.getCreatedAt().toString());
        if (value.getUsage() != null) {
            usage = (CompletionsUsage)value.getUsage();
            writer.writeInt32("completionTokens", usage.getCompletionTokens());
            writer.writeInt32("promptTokens", usage.getPromptTokens());
            writer.writeInt32("totalTokens", usage.getTotalTokens());
        }                
        writer.writeEndDocument();
    }

    @Override
    public FunctionResultMetadata decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        OffsetDateTime createdAt = OffsetDateTime.parse(reader.readString("createdAt"));        
        CompletionsUsage usage = null;
        if (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            int completionTokens = reader.readInt32("completionTokens");
            int promptTokens = reader.readInt32("promptTokens");
            int totalTokens = reader.readInt32("totalTokens");
       
            String json = String.format("{\"completionTokens\": %d, \"promptTokens\": %d, \"totalTokens\": %d}", completionTokens, promptTokens, totalTokens);
            
            JsonReader jsonReader = null;
            try {
                jsonReader = JsonProviders.createReader(new StringReader(json), new JsonOptions());
                usage = CompletionsUsage.fromJson(jsonReader);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("usage", usage);                    
        }
        reader.readEndDocument();
        return FunctionResultMetadata.build("metadata", usage, createdAt);
                
    }

    @Override
    public Class<FunctionResultMetadata> getEncoderClass() {
        return FunctionResultMetadata.class;
    }
    
}
