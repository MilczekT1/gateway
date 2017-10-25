package pl.konradboniecki.Budget.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class TemplateEngineService {
    @Autowired
    private TemplateEngine templateEngine;
    
    public String processHtml(String template, Map<String,String> contextVariables){
        Context context = new Context();
        for (String key: contextVariables.keySet()){
            context.setVariable(key,contextVariables.get(key));
        }
        return templateEngine.process(template,context);
    }
}
