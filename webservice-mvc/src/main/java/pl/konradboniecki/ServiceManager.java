package pl.konradboniecki;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.konradboniecki.models.account.Account;
import pl.konradboniecki.models.family.Family;
import pl.konradboniecki.utils.BudgetAdress;

import java.util.Optional;

import static java.util.Collections.singletonList;

@Slf4j
@Service
public class ServiceManager {

    @Autowired
    private RestTemplate restTemplate;

    private static int accountPort = 3004;

    /***************************FAMILY****************************/

    public Optional<Family> findFamilyById(Long id){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON_UTF8));
        HttpEntity httpEntity = new HttpEntity(headers);
        try {
            ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(
                    BudgetAdress.getURI() + ":3006/api/family/" + id,
                    HttpMethod.GET,
                    httpEntity, JsonNode.class);
            return Optional.of(new Family(responseEntity.getBody()));
        } catch (HttpClientErrorException | NullPointerException e){
            log.error("Family with id: " + id + " not found.");
            return Optional.empty();
        }
    }

    public boolean deleteFamilyById(Long familyId){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON_UTF8));
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                BudgetAdress.getURI() + ":3006/api/family/" + familyId,
                HttpMethod.DELETE,
                httpEntity, Void.class);
        return responseEntity.getStatusCode().is2xxSuccessful();
    }

    public Long countFreeSlotsInFamilyWithId(Long familyId){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON_UTF8));
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(
                BudgetAdress.getURI() + ":3006/api/family/" + familyId + "/slots",
                HttpMethod.GET,
                httpEntity, JsonNode.class);
        return responseEntity.getBody().path("freeSlots").asLong();
    }

    public Family saveFamily(Family family){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON_UTF8));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(family, headers);

        ResponseEntity<Family> responseEntity = restTemplate.exchange(
                BudgetAdress.getURI() + ":3006/api/family",
                HttpMethod.POST,
                httpEntity, Family.class);
        return responseEntity.getBody();
    }

    public Family updateFamily(Family family){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(family, headers);
        ResponseEntity<Family> responseEntity = restTemplate.exchange(
                BudgetAdress.getURI() + ":3006/api/family",
                HttpMethod.PUT,
                httpEntity, Family.class);
        return responseEntity.getBody();
    }

    /*************************ACCOUNT*****************************/

    public Optional<Account> findAccountById(Long id){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON_UTF8));
        HttpEntity httpEntity = new HttpEntity(headers);
        try{
        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(
                BudgetAdress.getURI() + ":3004/api/account/" + id,
                HttpMethod.GET,
                httpEntity, JsonNode.class);
            return Optional.of(new Account(responseEntity.getBody()));
        } catch (HttpClientErrorException | NullPointerException e){
            log.error("Account with id: " + id + " not found.");
            return Optional.empty();
        }
    }

    public Optional<Account> findAccountByEmail(String email){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON_UTF8));
        HttpEntity httpEntity = new HttpEntity(headers);
        try {
            ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(
                BudgetAdress.getURI() + ":3004/api/account/" + email + "?findBy=email",
                HttpMethod.GET,
                httpEntity, JsonNode.class);
            return Optional.of(new Account(responseEntity.getBody()));
        } catch (HttpClientErrorException | NullPointerException e){
            log.error("Account with email: " + email + " not found.");
            return Optional.empty();
        }
    }

    public Account saveAccount(Account accountToSave){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON_UTF8));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(accountToSave, headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(
                BudgetAdress.getURI() + ":3004/api/account",
                HttpMethod.POST,
                httpEntity, JsonNode.class);
        return new Account(responseEntity.getBody());
    }

    public Boolean isPasswordCorrectForAccount(Long accountId, String hashedPassword){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON_UTF8));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("accountId", accountId.toString());
        headers.set("password", hashedPassword);
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(
                BudgetAdress.getURI() + ":3004/api/account/credentials",
                HttpMethod.GET,
                httpEntity, JsonNode.class);

        return responseEntity.getStatusCode() == HttpStatus.OK;
    }

    public Boolean setFamilyIdInAccountWithId(Long familyId, Long accountId){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON_UTF8));
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                BudgetAdress.getURI() + ":3004/api/account/" + accountId + "/family/" + familyId,
                HttpMethod.PUT,
                httpEntity, String.class);
        return responseEntity.getStatusCode() == HttpStatus.OK;
    }
}
