package pl.konradboniecki.services.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import pl.konradboniecki.Budget.models.account.Account;
import pl.konradboniecki.Budget.models.family.Family;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MailService {
    
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private TemplateEngineService templateEngineService;
    
    private void sendMailToUserUsingTemplate(String title, String templateName, Account account, Map<String,String> contextVariables){
        String destination = account.getEmail();
        String message = templateEngineService.processHtml(templateName,contextVariables);
        sendEmail(title,message,destination);
    }
    public void sendEmail(String title, String message, String destination){
        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail);
        try {
            helper.setTo(destination);
            helper.setFrom("foxkingdom.budget@gmail.com");
            helper.setSubject(title);
            helper.setText(message, true);
            helper.setReplyTo("konrad_boniecki@hotmail.com");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(()->{
            javaMailSender.send(mail);
        });
        executorService.shutdown();
    }
    
    public void sendSignUpConfirmation(Account account, String activationCode){
        Map<String,String> contextVariables = new HashMap<>();
        contextVariables.put("recipient", account.getFirstName() + " " + account.getLastName());
        contextVariables.put("activationLink","http://localhost:8080/activate/" + account.getId() + "/" + activationCode);
        
        sendMailToUserUsingTemplate("Budget - Sign up completed","signUpConfirmationMail",account,contextVariables);
    }
    public void sendNewPasswordActivationMail(Account account, String resetCode){
        Map<String,String> contextVariables = new HashMap<>();
        contextVariables.put("recipient", account.getFirstName() + " " + account.getLastName());
        contextVariables.put("resetLink","http://localhost:8080/reset/" + account.getId() + "/" + resetCode);
        
        sendMailToUserUsingTemplate("Budget - New Password Activation", "newPasswordConfirmationMail",account,contextVariables);
    }
    public void sendFamilyInvitationToUserWithAccount(Account account, Family family, String invitationCode) {
        Map<String,String> contextVariables = new HashMap<>();
        contextVariables.put("recipient", account.getFirstName() + " " + account.getLastName());
        contextVariables.put("invitationLink","http://localhost:8080/home/family/" + family.getId() + "/addMember/" + account.getId() +  "/" + invitationCode);
        
        sendMailToUserUsingTemplate("Budget - Invitation to the new Family","invitationToFamilyMail", account, contextVariables);
    }
    
    public void sendFamilyInvitationToUserWithoutAccount(Account account, String newMemberMail){
        Map<String,String> contextVariables = new HashMap<>();
        contextVariables.put("recipient", newMemberMail);
        contextVariables.put("invitersEmailAdress", account.getEmail());
        contextVariables.put("invitationLink","http://localhost:8080/register");
        
        Account acc = new Account();
        acc.setEmail(newMemberMail);
        
        sendMailToUserUsingTemplate("Budget - Invitation to family","InvitationToFamilyMail", acc, contextVariables);
    }
}
