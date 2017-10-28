package pl.konradboniecki.Budget.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import pl.konradboniecki.Budget.models.Account;

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
    
    public void sendSignUpConfirmation(Account account){
        String title = "Budget - Sign up completed";
        String destination = account.getEmail();
    
        Map<String,String> contextVariables = new HashMap<>();
        contextVariables.put("recipient", account.getFirstName() + " " + account.getLastName());
        
        String message = templateEngineService.processHtml("signUpConfirmationMail",contextVariables);
        sendEmail(title,message,destination);
    }
}
