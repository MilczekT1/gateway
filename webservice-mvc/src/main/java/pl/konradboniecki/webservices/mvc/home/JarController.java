package pl.konradboniecki.webservices.mvc.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.models.account.Account;
import pl.konradboniecki.models.account.AccountRepository;
import pl.konradboniecki.models.budget.Budget;
import pl.konradboniecki.models.budget.BudgetRepository;
import pl.konradboniecki.models.family.FamilyRepository;
import pl.konradboniecki.models.frontendforms.JarCreationForm;
import pl.konradboniecki.models.jar.Jar;
import pl.konradboniecki.models.jar.JarRepository;

import java.util.List;
import java.util.Optional;

import static pl.konradboniecki.templates.ViewTemplate.BUDGET_HOME_PAGE;
import static pl.konradboniecki.templates.ViewTemplate.JAR_CREATION_PAGE;

@Controller
@RequestMapping(value = "home/budget")
public class JarController {

    @Autowired private AccountRepository accountRepository;
    @Autowired private FamilyRepository familyRepository;
    @Autowired private JarRepository jarRepository;
    @Autowired private BudgetRepository budgetRepository;

    @GetMapping("/create-jar")
    public ModelAndView createJar(ModelMap modelMap){
        modelMap.put("newJarCreationForm", new JarCreationForm());
        return new ModelAndView(JAR_CREATION_PAGE, modelMap);
    }

    @PostMapping("/create-jar")
    public ModelAndView createJar(
            @ModelAttribute("newJarCreationForm") JarCreationForm jarCreationForm, BindingResult bindingResult, ModelMap modelMap){
        if (bindingResult.hasErrors()) {
            return new ModelAndView(BUDGET_HOME_PAGE);
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Account> acc = accountRepository.findByEmail(email);
        //Optional<Family> family = familyRepository.findById(acc.get().getFamilyId());


        Optional<Budget> budget = budgetRepository.findByFamilyId(acc.get().getFamilyId());
        List<Jar> jarList = jarRepository.findAllByBudgetId(budget.get().getId());
        if (jarList.size() < budget.get().getMaxJars()) {
            Jar jar = new Jar(jarCreationForm);
            jar.setBudgetId(budget.get().getId());
            jarRepository.save(jar);

            jarList = jarRepository.findAllByBudgetId(budget.get().getId());
            modelMap.addAttribute("jarList", jarList);
            return new ModelAndView(BUDGET_HOME_PAGE, modelMap);
        } else {
            return new ModelAndView(BUDGET_HOME_PAGE);
        }
    }

}
