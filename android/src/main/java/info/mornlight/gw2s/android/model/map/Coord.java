package info.mornlight.gw2s.android.model.map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;

@JsonSerialize(using = Coord.CoordSerializer.class)
@JsonDeserialize(using = Coord.CoordDeserializer.class)
public class Coord{
    private int x;
    private int y;

    public Coord() {
    }

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    static public class CoordSerializer extends JsonSerializer<Coord> {

        @Override
        public void serialize(Coord coord, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
            generator.writeStartArray();
            generator.writeNumber(coord.getX());
            generator.writeNumber(coord.getY());
            generator.writeEndArray();
        }
    }

    static public class CoordDeserializer extends JsonDeserializer<Coord> {

        @Override
        public Coord deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
            parser.nextToken(); // current token is "[", move next
            int x = parser.getIntValue();
            parser.nextToken();
            int y = parser.getIntValue();
            parser.nextToken();
            //parser.nextToken(); //move ']'  //TODO no need to skip this token?

            return new Coord(x, y);
        }
    }
}
