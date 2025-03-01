package com.microsoft.ganesha.data;

import java.time.OffsetDateTime;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.*;

import com.microsoft.semantickernel.orchestration.FunctionResultMetadata;

public class FunctionResultMetadataContentCodec implements Codec<FunctionResultMetadata> {
    @Override
    public void encode(BsonWriter writer, FunctionResultMetadata value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeString("createdAt", value.getCreatedAt().toString());
        writer.writeString("usage", value.getUsage().toString());        
        writer.writeEndDocument();
    }

    @Override
    public FunctionResultMetadata<?> decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        OffsetDateTime createdAt = OffsetDateTime.parse(reader.readString("createdAt"));
        Object usage = reader.readString("usage");
        reader.readEndDocument();
        return FunctionResultMetadata.build("metadata", usage, createdAt);
                
        // return new FunctionResultMetadata() {
        //     @Override
        //     public OffsetDateTime getCreatedAt() {
        //         return createdAt;
        //     }

        //     @Override
        //     public Object getUsage() {
        //         return usage;
        //     }
        // };
    }

    @Override
    public Class<FunctionResultMetadata> getEncoderClass() {
        return FunctionResultMetadata.class;
    }
    
}
