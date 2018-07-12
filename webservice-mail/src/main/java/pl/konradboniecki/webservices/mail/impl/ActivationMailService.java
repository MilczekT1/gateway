package pl.konradboniecki.webservices.mail.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.konradboniecki.models.account.Account;
import pl.konradboniecki.utils.BudgetAdress;

import java.util.HashMap;
import java.util.Map;

import static pl.konradboniecki.webservices.mail.impl.MailTemplate.CONFIRMATION_NEW_PASSWORD;
import static pl.konradboniecki.webservices.mail.impl.MailTemplate.CONFIRMATION_SIGN_UP;

@Service
public class ActivationMailService {

    @Autowired
    private MailService mailService;

    public Boolean sendSignUpConfirmation(Account account, String activationCode){
        Map<String,String> contextVariables = new HashMap<>();
        contextVariables.put("recipient", account.getFirstName() + " " + account.getLastName());
        contextVariables.put("activationLink",
                BudgetAdress.getLOCALHOST() + ":8080/activate/" + account.getId() + "/" + activationCode);

        return mailService.sendMailToUserUsingTemplate("Budget - Sign up completed",
                CONFIRMATION_SIGN_UP,account.getEmail(), contextVariables);
    }

    public Boolean sendNewPasswordActivationLink(Account account, String resetCode){
        Map<String,String> contextVariables = new HashMap<>();
        contextVariables.put("recipient", account.getFirstName() + " " + account.getLastName());
        contextVariables.put("resetLink",
                BudgetAdress.getLOCALHOST() + ":8080/reset/" + account.getId() + "/" + resetCode);

        return mailService.sendMailToUserUsingTemplate("Budget - New Password Activation",
                CONFIRMATION_NEW_PASSWORD,account.getEmail(), contextVariables);
    }
}