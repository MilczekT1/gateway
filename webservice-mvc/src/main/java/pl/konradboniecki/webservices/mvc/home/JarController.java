package pl.konradboniecki.webservices.mvc.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.models.account.Account;
import pl.konradboniecki.models.account.AccountRepository;
import pl.konradboniecki.models.family.Family;
import pl.konradboniecki.models.family.FamilyRepository;
import pl.konradboniecki.models.frontendforms.JarCreationForm;
import pl.konradboniecki.models.jar.Jar;
import pl.konradboniecki.models.jar.JarRepository;

import java.util.Optional;

import static pl.konradboniecki.templates.ViewTemplate.BUDGET_HOME_PAGE;

@Controller
@RequestMapping(value = "home/budget")
public class JarController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private JarRepository jarRepository;


    @PostMapping("/create-jar")
    public ModelAndView createJar(
            @ModelAttribute("newJarCreationForm") JarCreationForm jarCreationForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new ModelAndView(BUDGET_HOME_PAGE);
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Account> acc = accountRepository.findByEmail(email);
        Optional<Family> family = familyRepository.findById(acc.get().getFamilyId());

        Jar jar = new Jar(jarCreationForm);
        jar.setBudgetId(family.get().getBudgetId());
        jarRepository.save(jar);

        return new ModelAndView(BUDGET_HOME_PAGE);
    }

}
