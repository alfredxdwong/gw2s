package info.mornlight.gw2s.android.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class Json {
    private static final ObjectMapper mapper;
    private static final TypeFactory typeFactory;

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static TypeFactory getTypeFactory() {
        return typeFactory;
    }

    static {
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

        //SimpleModule resourceModule = new SimpleModule("Resource", new Version(1, 0, 0, null, "svipht", "resource"));
        //resourceModule.addDeserializer(Resource.class, new ResourceDeserializer<Resource>(def, Resource.class));
        //resourceModule.addDeserializer(Document.class, new ResourceDeserializer<Document>(def, Document.class));
        //resourceModule.addDeserializer(Space.class, new ResourceDeserializer<Space>(def, Space.class));
        //mapper.registerModule(resourceModule);

        typeFactory = mapper.getTypeFactory();
    }
}
