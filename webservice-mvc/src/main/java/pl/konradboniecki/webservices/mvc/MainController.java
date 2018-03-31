package pl.konradboniecki.webservices.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.utils.template.ViewTemplate;

@Controller
public class MainController {
    
    @GetMapping("/")
    public ModelAndView mainPage(){
        return new ModelAndView(ViewTemplate.INDEX);
    }
}
