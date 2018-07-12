package pl.konradboniecki.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
  * This class provides support for simple restcalls.f
 **/

@Component
public class RestCall {

//    @Autowired
    private JsonWrapper jsonWrapper;

    @Autowired
    public RestCall(JsonWrapper newJsonWrapper){
        this.jsonWrapper = newJsonWrapper;
    }

    public HttpResponse<JsonNode> performPostWithJSON(String url, Map<String, Object> jsonObjects)
            throws JsonProcessingException, UnirestException, NullPointerException {

        if (jsonObjects == null)
            throw new NullPointerException("Map<String, Object> jsonObjects should not be null!");

        String finalJsonAsString =  jsonWrapper.objectMapToJsonString(jsonObjects);

        Unirest.clearDefaultHeaders();
        return Unirest.post(url)
                .header("Content-Type", "application/json")
                .body(finalJsonAsString)
                .asJson();
    }
}