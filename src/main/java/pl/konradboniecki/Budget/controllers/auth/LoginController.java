package pl.konradboniecki.Budget.controllers.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.Budget.models.LoginForm;

@Controller
public class LoginController {
    
    @GetMapping(value = "/login")
    public ModelAndView showLoginPage(){
        return new ModelAndView("auth/login","loginForm", new LoginForm());
    }
}
