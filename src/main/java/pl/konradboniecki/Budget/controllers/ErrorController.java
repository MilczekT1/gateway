package pl.konradboniecki.Budget.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

    @GetMapping(value="/error")
    public ModelAndView customError(){
        return new ModelAndView("error");
    }
}
