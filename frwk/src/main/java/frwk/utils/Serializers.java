package frwk.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class Serializers extends JsonSerializer<Object> {
    public static final JsonSerializer<Object> EMPTY_STRING_SERIALIZER_INSTANCE = new EmptyStringSerializer();

    public Serializers() {
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException,
                                                                                                               JsonProcessingException {
        jsonGenerator.writeString("");
    }

    private static class EmptyStringSerializer extends JsonSerializer<Object> {
        public EmptyStringSerializer() {
        }

        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException,
                                                                                                                   JsonProcessingException {
            jsonGenerator.writeString("");
        }
    }
}
