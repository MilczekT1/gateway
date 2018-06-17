package pl.konradboniecki.webservices.mvc.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.models.frontendforms.LoginForm;
import pl.konradboniecki.utils.template.ViewTemplate;

@Controller
public class LoginController {
    
    @GetMapping(value = "/login")
    public ModelAndView showLoginPage(){
        return new ModelAndView(ViewTemplate.LOGIN_PAGE, "loginForm", new LoginForm());
    }
}
