package pl.konradboniecki.Budget.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserHomeController {
    
    @GetMapping("/home")
    public ModelAndView xxx() {
        return new ModelAndView("home");
    }
    
}