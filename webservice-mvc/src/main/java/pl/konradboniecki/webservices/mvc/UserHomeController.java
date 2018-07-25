package pl.konradboniecki.webservices.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.templates.ViewTemplate;

@Controller
public class UserHomeController {
    
    @GetMapping("/home")
    public ModelAndView showUserHomePage() {
        return new ModelAndView(ViewTemplate.HOME_PAGE.getFilename());
    }
    
    @PostMapping ("/home")
    public ModelAndView showUserHomePageAfterCorrectLogin() {
        return new ModelAndView("redirect:/home");
    }
    
}
