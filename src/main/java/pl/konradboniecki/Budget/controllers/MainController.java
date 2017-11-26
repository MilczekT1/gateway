package pl.konradboniecki.Budget.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.Budget.services.MailService;

import java.util.HashMap;

@Controller
public class MainController {
    
    @Autowired
    private MailService mailService;
    
    @GetMapping("/")
    public ModelAndView mainPage(){
        HashMap<String,String> modelAttributes = new HashMap<>();
        modelAttributes.put("Greeting","Witaj w aplikacjji Budget!");
        
        return new ModelAndView("index",modelAttributes);
    }
}
