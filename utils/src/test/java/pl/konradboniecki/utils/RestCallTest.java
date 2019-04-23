package pl.konradboniecki.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class RestCallTest {

    private RestCall restCall;
    private static Map<String, Object> jsonObjects;

    @BeforeAll
    public void setUp() {
        restCall = new RestCall(new JsonWrapper());
        Map<String, Object> templates = new HashMap<>();
        templates.put("TestMapProperty1", "error");
        templates.put("TestMapProperty2", "home/familyCreationForm");
        templates.put("TestMapProperty3", "auth/registrationSuccessInfo");
        jsonObjects = new HashMap<>();
        jsonObjects.put("TestKey1","TestValue1");
        jsonObjects.put("TestKey2","TestValue2");
        jsonObjects.put("TestKey3", templates);
    }

    @Test
    @Disabled("testing endpoint not provided")
    public void performPostWithJsonTest(){

        try {
            HttpResponse<JsonNode> response = restCall.performPostWithJSON("http://www.google.com", jsonObjects);
        } catch (JsonProcessingException | UnirestException e) {
            e.printStackTrace();
            fail("Exception should not be thrown!");
        }

        try {
            restCall.performPostWithJSON("htgkfdslhg", jsonObjects);
        } catch (JsonProcessingException | UnirestException e) {
            ;
        }

        try {
            restCall.performPostWithJSON("http://www.google.com", null);
            //TODO: compare json
        } catch (JsonProcessingException | UnirestException e) {
            e.printStackTrace();
            fail("Exception different than NullPointerException should not be thrown!");
        } catch (NullPointerException e){
            ;
        }
    }
}