package pl.konradboniecki.webservices.mail.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.konradboniecki.models.account.Account;
import pl.konradboniecki.models.family.Family;

import java.util.HashMap;
import java.util.Map;

import static pl.konradboniecki.webservices.mail.impl.MailTemplate.INVITE_FAMILY_NEW_USER;
import static pl.konradboniecki.webservices.mail.impl.MailTemplate.INVITE_FAMILY_OLD_USER;

@Service
public class InvitationMailService {

    @Autowired
    private MailService mailService;

    public Boolean sendFamilyInvitationToExistingUser(Account account, Account owner,
                                                      Family family, String invitationCode) {
        Map<String,String> ctxVariables = new HashMap<>();
        ctxVariables.put("recipient", account.getFirstName() + " " + account.getLastName());
        ctxVariables.put("familyTitle", family.getTitle());
        ctxVariables.put("ownersFirstName", owner.getFirstName());
        ctxVariables.put("ownersLastName", owner.getLastName());
        ctxVariables.put("ownersEmail", owner.getEmail());
        ctxVariables.put("invitationLink", "http://localhost:8080/home/family/" + family.getId() + "/addMember/" + account.getId() + "/" + invitationCode);
        return mailService.sendMailToUserUsingTemplate("Budget - Invitation to the new family",
                INVITE_FAMILY_OLD_USER, account.getEmail(), ctxVariables);
    }

    public Boolean sendFamilyInvitationToNewUser(Account owner, Family family, String newMemberMail){
        Map<String,String> ctxtVariables = new HashMap<String,String>();
        ctxtVariables.put("recipient",newMemberMail);
        ctxtVariables.put("familyTitle",family.getTitle());
        ctxtVariables.put("ownersFirstName",owner.getFirstName());
        ctxtVariables.put("ownersLastName",owner.getLastName());
        ctxtVariables.put("ownersEmail",owner.getEmail());
        ctxtVariables.put("registerLink","http://localhost:8080/register");
        return mailService.sendMailToUserUsingTemplate("Budget - Invitation to family",
                INVITE_FAMILY_NEW_USER, newMemberMail, ctxtVariables);
    }
}
