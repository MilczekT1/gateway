package pl.konradboniecki.webservices.mail.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.konradboniecki.models.account.Account;
import pl.konradboniecki.webservices.mail.impl.ActivationMailService;

import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Log
@RestController
@RequestMapping("/services/mail/activation")
public class ActivationMailServiceREST {

    @Autowired
    private ActivationMailService activationMailService;

    @PostMapping(value = "/new-account", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity sendSignUpConfirmation(@RequestBody ObjectNode json){
        log.info("______________sendSignUpConfirmation______________");
        Account account = new Account("Account", json);
        String activationCode = json.get("ActivationCode").asText();
        log.info(account.toString());

        if (activationMailService.sendSignUpConfirmation(account, activationCode)){
            log.info("Mail has been sent");
            return ResponseEntity.status(OK).build();
        } else {
            log.severe("Mail has not been sent");
            return ResponseEntity.status(EXPECTATION_FAILED).build();
        }
    }

    @PostMapping(value = "/new-password", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity sendNewPasswordActivationLink(@RequestBody ObjectNode json){
        log.info("______________sendNewPasswordActivationLink______________");
        Account account = new Account("Account", json);
        String resetCode = json.get("ResetCode").asText();
        log.info(account.toString());


        if (activationMailService.sendNewPasswordActivationLink(account, resetCode)){
            log.info("Mail has been sent");
            return ResponseEntity.status(OK).build();
        } else {
            log.severe("Mail has not been sent");
            return ResponseEntity.status(EXPECTATION_FAILED).build();
        }
    }
}