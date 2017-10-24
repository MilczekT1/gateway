package pl.konradboniecki.Budget.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.konradboniecki.Budget.services.MailService;

@Controller
public class MainController {
    
    @Autowired
    MailService mailService;
    
    @GetMapping("/")
    public String mainPage(Model model){
        model.addAttribute("Greeting","Witaj w aplikacjji Budget!");
        return "index";
    }
}
