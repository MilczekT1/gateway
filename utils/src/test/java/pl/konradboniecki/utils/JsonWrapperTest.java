package pl.konradboniecki.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

@TestInstance(Lifecycle.PER_CLASS)
public class JsonWrapperTest {

    private JsonWrapper jsonWrapper;

    @BeforeAll
    public void setUp(){
        jsonWrapper = new JsonWrapper();
    }

    @Test
    public void objectMapToJsonStringTest(){

        Map<String,Object> map = new LinkedHashMap<String, Object>(){{
            put("String", "testString");
            put("LocalTime", LocalTime.of(13,0,0));
        }};
        String expectedJsonString = "{\n  \"String\" : \"testString\"," +
                "\n  \"LocalTime\" : {\n" +
                "    \"hour\" : 13,\n" +
                "    \"minute\" : 0,\n" +
                "    \"second\" : 0,\n" +
                "    \"nano\" : 0\n" +
                "  }\n}";

        try {
            String finalString = jsonWrapper.objectMapToJsonString(map);
            Assert.assertEquals(expectedJsonString, finalString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Assert.fail("JsonProcessingException should not be thrown");
        }
    }
}
