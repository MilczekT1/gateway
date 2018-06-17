package pl.konradboniecki.webservices.mail.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.konradboniecki.models.account.Account;
import pl.konradboniecki.models.family.Family;
import pl.konradboniecki.webservices.mail.spi.MailService;

import java.util.HashMap;
import java.util.Map;

import static pl.konradboniecki.utils.template.MailTemplate.INVITE_FAMILY_NEW_USER;
import static pl.konradboniecki.utils.template.MailTemplate.INVITE_FAMILY_OLD_USER;

@Service
public class InvitationMailService {

    @Autowired
    private MailService mailService;

    public Boolean sendFamilyInvitationToExistingUser(Account account, Account owner,
                                                      Family family, String invitationCode) {
        Map<String,String> contextVariables = new HashMap<String,String>() {{
            put("recipient", account.getFirstName() + " " + account.getLastName());
            put("familyTitle", family.getTitle());
            put("ownersFirstName", owner.getFirstName());
            put("ownersLastName", owner.getLastName());
            put("ownersEmail", owner.getEmail());
            put("invitationLink", "http://localhost:8080/home/family/" + family.getId() + "/addMember/" + account.getId() + "/" + invitationCode);
        }};
        return mailService.sendMailToUserUsingTemplate("Budget - Invitation to the new family",
                INVITE_FAMILY_OLD_USER, account.getEmail(), contextVariables);
    }

    public Boolean sendFamilyInvitationToNewUser(Account owner, Family family, String newMemberMail){
        Map<String,String> contextVariables = new HashMap<String,String>() {{
            put("recipient",newMemberMail);
            put("familyTitle",family.getTitle());
            put("ownersFirstName",owner.getFirstName());
            put("ownersLastName",owner.getLastName());
            put("ownersEmail",owner.getEmail());
            put("registerLink","http://localhost:8080/register");
        }};
        return mailService.sendMailToUserUsingTemplate("Budget - Invitation to family",
                INVITE_FAMILY_NEW_USER, newMemberMail, contextVariables);
    }
}
