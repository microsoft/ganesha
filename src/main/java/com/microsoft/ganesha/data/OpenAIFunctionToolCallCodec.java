package com.microsoft.ganesha.data;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.*;

import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIFunctionToolCall;

public class OpenAIFunctionToolCallCodec implements Codec<OpenAIFunctionToolCall> {
    @Override
    public void encode(BsonWriter writer, OpenAIFunctionToolCall value, EncoderContext encoderContext) {
        /*
    private final KernelFunctionArguments arguments; */
        writer.writeStartDocument();
        writer.writeString("id", value.getId());
        writer.writeString("pluginName", value.getPluginName());
        writer.writeString("functionName", value.getFunctionName());
        writer.writeEndDocument();
    }

    @Override
    public OpenAIFunctionToolCall decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        String id = reader.readString("id");
        String pluginName = reader.readString("pluginName");
        String functionName = reader.readString("functionName");
        reader.readEndDocument();
        return new OpenAIFunctionToolCall(id, pluginName, functionName, null);
    }

    @Override
    public Class<OpenAIFunctionToolCall> getEncoderClass() {
        return OpenAIFunctionToolCall.class;
    }
    
}
