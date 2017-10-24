package pl.konradboniecki.Budget.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/mainPage")
public class UserMainPageController {
    
    @GetMapping
    public String xxx(){
        return null;
    }
    
}
