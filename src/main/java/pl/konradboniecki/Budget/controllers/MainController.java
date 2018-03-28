package pl.konradboniecki.Budget.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Controller
public class MainController {
    
    @GetMapping("/")
    public ModelAndView mainPage(){
        HashMap<String,String> modelAttributes = new HashMap<>();
        modelAttributes.put("Greeting","Witaj w aplikacjji Budget!");
        
        return new ModelAndView("index",modelAttributes);
    }
}
