package pl.konradboniecki;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.konradboniecki.models.family.Family;
import pl.konradboniecki.utils.BudgetAdress;

import java.util.Optional;

import static java.util.Collections.singletonList;

@Slf4j
@Service
public class ServiceManager {

    @Autowired
    private RestTemplate restTemplate;

    public Optional<Family> findFamilyById(Long id){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON_UTF8));
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(
                BudgetAdress.getURI() + ":3006/api/family/" + id,
                HttpMethod.GET,
                httpEntity, JsonNode.class);
        return Optional.of(new Family(responseEntity.getBody()));
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
}
