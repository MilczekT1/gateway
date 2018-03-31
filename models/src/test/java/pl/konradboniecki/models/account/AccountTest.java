package pl.konradboniecki.models.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import org.junit.Test;
import pl.konradboniecki.models.family.Family;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.System.out;

public class AccountTest {
    private static Gson gson = new Gson();

    @Test
    public void test() throws JsonProcessingException {
        Account acc = new Account();
        acc.setEnabled(true);
        acc.setFamilyId(null);
        acc.setId(1L);
        acc.setFirstName("kon");
        acc.setLastName("bon");
        acc.setEmail("konrad_boniecki@hotmail.com");
        acc.setPhoneNumber("607717684");
        acc.setPassword("123qweasd");
        acc.setRegisterDate(ZonedDateTime.now());
        acc.setRole("USER");

        Family family = new Family();
        family.setId(1L);
        family.setMaxMembers(5);
        family.setTitle("lisia norka");
        family.setOwnerId(1L);

//        out.println(gson.toJson(acc));
////        System.out.println(gson.toJson(acc,Account.class));
////        System.out.println(gson.toJsonTree(acc));
////        System.out.println(gson.toJson(family));
//
//
//        JsonElement el = gson.toJsonTree(acc);
//        JsonObject jo = new JsonObject();
//        jo.add("Account", el);
//        jo.add("Family", gson.toJsonTree(family));
//        out.println(jo.toString());
//
//        AccountWithFamilyJSON accountWithFamily = new AccountWithFamilyJSON();
//        accountWithFamily.setAccount(acc);
//        accountWithFamily.setFamily(family);
//
//        JsonObject accWithFamily = new JsonObject();
//        accWithFamily.add("xxx",gson.toJsonTree(accountWithFamily));
//        out.println(accWithFamily);
//
        out.println("Jackson");
        Map<String, Object> jsonObjects = new LinkedHashMap<>();
        jsonObjects.put("Account", acc);
        jsonObjects.put("Family", family);
        jsonObjects.put("InvitationCode", "123456");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        out.println(ow.writeValueAsString(jsonObjects));

    }
}