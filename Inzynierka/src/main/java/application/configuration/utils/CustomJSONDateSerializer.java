package application.configuration.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * Created by DZONI on 20.12.2016.
 */
public class CustomJSONDateSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2) throws
            IOException, JsonProcessingException {

        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //String formattedDate = formatter.format(value);

        gen.writeString(value.toString());

    }
}
