package pl.konradboniecki.Budget.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "home")
public class FamilyController {
    
    @GetMapping ("/family")
    public ModelAndView xxx() {
        return new ModelAndView("home/family");
    }
}
