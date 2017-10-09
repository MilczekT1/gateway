package pl.konradboniecki.Budget.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    
    @GetMapping("/")
    public String mainPage(Model model){
        model.addAttribute("Greeting","Witaj w aplikacjji Budget!");
        return "index";
    }
}
