package pl.konradboniecki;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.konradboniecki.models.family.Family;
import pl.konradboniecki.utils.BudgetAdress;

import java.util.Optional;

import static java.util.Collections.singletonList;

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
}
