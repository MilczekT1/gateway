package pl.konradboniecki.webservices.mail.impl;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public Boolean sendMailToUserUsingTemplate(String title, String templateName, String destination, Map<String,String> contextVariables){
        String message = processHtml(templateName,contextVariables);
        return sendEmail(title, message, destination);
    }

    private String processHtml(String template, Map<String,String> contextVariables){
        Context context = new Context();
        for (String key: contextVariables.keySet()){
            context.setVariable(key,contextVariables.get(key));
        }
        return templateEngine.process(template,context);
    }

    private Boolean sendEmail(String title, String message, String destination){
        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail);
        try {
            helper.setTo(destination);
            helper.setFrom("foxkingdom.budget@gmail.com");
            helper.setSubject(title);
            helper.setText(message, true);
            helper.setReplyTo("konrad_boniecki@hotmail.com");
        } catch (MessagingException e) {
            log.error(Throwables.getStackTraceAsString(e));
            return false;
        }

        javaMailSender.send(mail);
        return true;
    }
}