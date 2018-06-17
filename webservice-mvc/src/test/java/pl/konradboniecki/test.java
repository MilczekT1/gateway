package pl.konradboniecki;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import pl.konradboniecki.models.account.Account;
import pl.konradboniecki.models.family.Family;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.System.out;

public class test {


    public void test1(){
        Account acc = new Account();
        acc.setEnabled(false);
        acc.setFamilyId(null);
        acc.setId(1L);
        acc.setFirstName("kon");
        acc.setLastName("bon");
        acc.setEmail("konrad_boniecki@hotmail.com");
        acc.setPhoneNumber("123123123");
        acc.setPassword("123qweasd");
        acc.setRegisterDate(ZonedDateTime.now());
        acc.setRole("USER");

        Family family = new Family();
        family.setId(1L);
        family.setMaxMembers(5);
        family.setTitle("lisia norka");
        family.setOwnerId(1L);

        Map<String, Object> jsonObjects = new LinkedHashMap<>();
        jsonObjects.put("Account", acc);
//        jsonObjects.put("Family", family);
        jsonObjects.put("ActivationCode", "25a4ac42-ba58-4f11-bae3-a5fe308ecb1f");

        String json = "{ \"Account\" : { \"id\" : 1,\"familyId\" : null, \"firstName\" : \"kon\", \"lastName\" : \"bon\", \"email\" : \"konrad_boniecki@hotmail.com\", \"phoneNumber\" : \"123123123\", \"role\" : \"USER\", \"enabled\" : false }, \"ActivationCode\" : \"25a4ac42-ba58-4f11-bae3-a5fe308ecb1f\"}";
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String finalJsonAsString=null;
        try {
            out.println(ow.writeValueAsString(jsonObjects));
            finalJsonAsString = ow.writeValueAsString(jsonObjects);
            out.println("----");
//            JsonNode node = new JsonNode(ow.writeValueAsString(jsonObjects));
//            out.println(node.getObject());
//            out.println("----");
//            out.println(node.getArray());
//            out.println("----");
//            out.println(node.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            Unirest.clearDefaultHeaders();
//            out.println(Unirest.post("http://77.55.237.245:3002/services/mail/activation/new-account")
//                    .header("Content-Type", "application/json")
//                    .fields(jsonObjects).asJson());

            Unirest.post("http://77.55.237.245:3002/services/mail/activation/new-account")
                    .header("Content-Type", "application/json")
                    .body(finalJsonAsString)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

    }
}
