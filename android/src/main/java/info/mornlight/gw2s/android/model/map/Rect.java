package info.mornlight.gw2s.android.model.map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;

@JsonDeserialize(using = Rect.RectDeserializer.class)
public class Rect {
    private Coord topLeft;
    private Coord bottomRight;

    public Rect(Coord topLeft, Coord bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Coord getTopLeft() {
        return topLeft;
    }

    public Coord getBottomRight() {
        return bottomRight;
    }

    static public class RectDeserializer extends JsonDeserializer<Rect> {

        @Override
        public Rect deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
            parser.nextToken();
            Coord topLeft = parser.readValueAs(Coord.class);
            Coord bottomRight = parser.readValueAs(Coord.class);
            parser.nextToken();

            return new Rect(topLeft, bottomRight);
        }
    }
}
