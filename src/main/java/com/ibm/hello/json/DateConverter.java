package com.ibm.hello.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@JsonComponent
public class DateConverter {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    public static class Serialize extends JsonSerializer<Date> {

        @Override
        public void serialize(Date value, JsonGenerator jsonGen, SerializerProvider provider) {
            try {
                if (value == null) {
                    jsonGen.writeNull();
                }
                else {
                    jsonGen.writeString(dateFormat.format(value));
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static class Deserialize extends JsonDeserializer<Date> {

        @Override
        public Date deserialize(com.fasterxml.jackson.core.JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
            try {
                String dateAsString = jsonParser.getText();
                if (dateAsString == null) {
                    return null;
                } else {
                    return dateFormat.parse(dateAsString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
