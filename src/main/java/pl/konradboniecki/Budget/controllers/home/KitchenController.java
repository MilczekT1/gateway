package pl.konradboniecki.Budget.controllers.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "home")
public class KitchenController {
    
    @GetMapping ("/kitchen")
    public ModelAndView xxx() {
        return new ModelAndView("home/kitchen");
    }
}
