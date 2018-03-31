package pl.konradboniecki.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;

public class RestCall {

    public static void performPostWithJSON(String url, Map<String, Object> jsonObjects) throws JsonProcessingException, UnirestException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String finalJsonAsString = ow.writeValueAsString(jsonObjects);
        System.out.println(finalJsonAsString);
        Unirest.clearDefaultHeaders();
        Unirest.post(url)
                .header("Content-Type", "application/json")
                .body(finalJsonAsString)
                .asJson();
    }
}
