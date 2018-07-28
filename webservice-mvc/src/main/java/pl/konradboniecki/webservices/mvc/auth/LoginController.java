package pl.konradboniecki.webservices.mvc.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.models.frontendforms.LoginForm;

import static pl.konradboniecki.templates.ViewTemplate.LOGIN_PAGE;

@Controller
public class LoginController {
    
    @GetMapping(value = "/login")
    public ModelAndView showLoginPage(){
        return new ModelAndView(LOGIN_PAGE, "loginForm", new LoginForm());
    }
}
