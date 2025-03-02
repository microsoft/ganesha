package com.microsoft.ganesha.data;

import java.time.OffsetDateTime;
import java.lang.reflect.Constructor;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.BsonType;
import org.bson.codecs.*;

import com.microsoft.semantickernel.orchestration.FunctionResultMetadata;
import com.azure.ai.openai.models.CompletionsUsage;

public class FunctionResultMetadataContentCodec implements Codec<FunctionResultMetadata> {
    @Override
    public void encode(BsonWriter writer, FunctionResultMetadata value, EncoderContext encoderContext) {
        CompletionsUsage usage = null;

        writer.writeStartDocument();
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
    public FunctionResultMetadata<?> decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        OffsetDateTime createdAt = OffsetDateTime.parse(reader.readString("createdAt"));        
        CompletionsUsage usage = null;        
        if (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            int completionTokens = reader.readInt32("completionTokens");
            int promptTokens = reader.readInt32("promptTokens");
            int totalTokens = reader.readInt32("totalTokens");

            try {
                Constructor<CompletionsUsage> constructor = CompletionsUsage.class.getDeclaredConstructor(int.class, int.class, int.class);
                constructor.setAccessible(true);
                usage = constructor.newInstance(completionTokens, promptTokens, totalTokens);

            } catch (Exception e) {
                throw new RuntimeException(e);    
            }

        }
        reader.readEndDocument();
        return FunctionResultMetadata.build("metadata", usage, createdAt);
                
    }

    @Override
    public Class<FunctionResultMetadata> getEncoderClass() {
        return FunctionResultMetadata.class;
    }
    
}
