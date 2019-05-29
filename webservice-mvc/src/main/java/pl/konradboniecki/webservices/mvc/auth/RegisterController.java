package pl.konradboniecki.webservices.mvc.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Throwables;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.ServiceManager;
import pl.konradboniecki.models.Account;
import pl.konradboniecki.models.frontendforms.AccountForm;
import pl.konradboniecki.utils.BudgetAdress;
import pl.konradboniecki.utils.RestCall;

import javax.validation.Valid;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static pl.konradboniecki.templates.ViewTemplate.*;
import static pl.konradboniecki.utils.enums.ErrorType.PROCESSING_EXCEPTION;

@Log
@Controller
public class RegisterController {

    @Autowired
    private RestCall restCall;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ServiceManager serviceManager;

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute("accountFormObject") @Valid AccountForm newAccountForm,
                                 BindingResult bindingResult, Model model) {
        
        if (bindingResult.hasErrors()) {
            return new ModelAndView(REGISTRATION_PAGE);
        } else if (!newAccountForm.checkRepeatedPassword()){
            return new ModelAndView(REGISTRATION_PAGE, "repeatedPasswordFailure",true);
        }

        Optional<Account> accOpt = serviceManager.findAccountByEmail(newAccountForm.getEmail());
        if (!accOpt.isPresent()){
            Account accFromForm = new Account(newAccountForm);
            serviceManager.saveAccount(accFromForm);
            accFromForm = serviceManager.findAccountByEmail(accFromForm.getEmail()).get();

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(singletonList(MediaType.APPLICATION_JSON_UTF8));
            headers.set("id", accFromForm.getId().toString());
            HttpEntity httpEntity = new HttpEntity(headers);
            ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(
                    BudgetAdress.getURI() + ":3004/api/account/activationCode",
                    HttpMethod.POST,
                    httpEntity, JsonNode.class);

            try {
                String activationCode = responseEntity.getBody()
                        .path("activationCode").asText();
                Map<String, Object> jsonObjects = new LinkedHashMap<>();
                jsonObjects.put("Account", accFromForm);
                jsonObjects.put("ActivationCode", activationCode);
                String url = BudgetAdress.getURI() + ":3002/api/mail/activate-account";
                restCall.performPostWithJSON(url, jsonObjects);
            } catch (UnirestException | IOException e) {
                log.severe(Throwables.getStackTraceAsString(e));
                return new ModelAndView(ERROR_PAGE, "errorType",
                        PROCESSING_EXCEPTION.getErrorTypeVarName());
            }

            return new ModelAndView(REGISTRATION_SUCCESS_MSG);
        } else {
            return new ModelAndView(REGISTRATION_PAGE,"emailAlreadyExists", true);
        }
    }

    @GetMapping("/register")
    public ModelAndView showRegisterPane(){
        return new ModelAndView(REGISTRATION_PAGE,"accountFormObject", new AccountForm());
    }
}