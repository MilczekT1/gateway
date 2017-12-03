package pl.konradboniecki.Budget.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserHomeController {
    
    @GetMapping("/home")
    public ModelAndView showUserHomePage() {
        return new ModelAndView("home/home");
    }
    
    @PostMapping ("/home")
    public ModelAndView showUserHomePageAfterCorrectLogin() {
        return new ModelAndView("redirect:/home");
    }
    
}
