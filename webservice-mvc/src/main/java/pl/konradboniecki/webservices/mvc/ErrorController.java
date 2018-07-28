package pl.konradboniecki.webservices.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.templates.ViewTemplate;

@Controller
public class ErrorController {

    @GetMapping(value = "/error")
    public ModelAndView customError(){
        return new ModelAndView(ViewTemplate.ERROR_PAGE);
    }
}
