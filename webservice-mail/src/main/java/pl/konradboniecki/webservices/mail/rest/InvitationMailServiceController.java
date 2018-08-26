package pl.konradboniecki.webservices.mail.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.konradboniecki.webservices.mail.impl.InvitationMailService;

import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Log
@RestController
@RequestMapping("/services/mail/invitation")
public class InvitationMailServiceController {

    @Autowired
    private InvitationMailService invitationMailService;

    @PostMapping(value = "/existing-user",
                consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity sendFamilyInvitationToExistingUser(@RequestBody ObjectNode json){
        if (invitationMailService.sendFamilyInvitationToExistingUser(json)){
            log.info("Mail has been sent");
            return ResponseEntity.status(OK).build();
        } else {
            log.severe("Mail has not been sent");
            return ResponseEntity.status(EXPECTATION_FAILED).build();
        }
    }

    @PostMapping(value = "/new-user", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity sendFamilyInvitationToNewUser(@RequestBody ObjectNode json){
        if (invitationMailService.sendFamilyInvitationToNewUser(json)){
            log.info("Mail has been sent");
            return ResponseEntity.status(OK).build();
        } else {
            log.severe("Mail has not been sent");
            return ResponseEntity.status(EXPECTATION_FAILED).build();
        }
    }
}