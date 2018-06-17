package pl.konradboniecki.webservices.mail.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.konradboniecki.models.account.Account;
import pl.konradboniecki.models.family.Family;
import pl.konradboniecki.webservices.mail.impl.InvitationMailService;

import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/services/mail/invitation")
public class InvitationMailServiceREST {
    private static final Logger log = LoggerFactory.getLogger(InvitationMailServiceREST.class);

    @Autowired
    private InvitationMailService invitationMailService;

    @PostMapping(value = "/existing-user",
                consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity sendFamilyInvitationToExistingUser(@RequestBody ObjectNode json){
        log.info("______________sendFamilyInvitationToExistingUser______________");
        Family family = new Family("Family", json);
        Account account = new Account("Account", json);
        Account owner = new Account("Owner", json);
        String invitationCode = json.get("InvitationCode").asText();
        log.info("Attempting to send mail to " + account.getId() + " with With invitation to family with id: " + family.getId());

        if (invitationMailService.sendFamilyInvitationToExistingUser(account, owner, family, invitationCode)){
            return ResponseEntity.status(OK).build();
        } else {
            return ResponseEntity.status(EXPECTATION_FAILED).build();
        }
    }

    @PostMapping(value = "/new-user", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity sendFamilyInvitationToNewUser(@RequestBody ObjectNode json){
        log.info("______________sendFamilyInvitationToNewUser______________");
        Account owner = new Account("Owner", json);
        Family family = new Family("Family", json);
        String newMemberMail = json.get("NewMemberEmail").asText();
        log.info("Attempting to send mail to " + newMemberMail + " with With invitation to family with id: " + family.getId());

        if (invitationMailService.sendFamilyInvitationToNewUser(owner, family, newMemberMail)){
            log.info("Mail has been sent");
            return ResponseEntity.status(OK).build();
        } else {
            log.error("Mail has not been sent");
            return ResponseEntity.status(EXPECTATION_FAILED).build();
        }
    }
}