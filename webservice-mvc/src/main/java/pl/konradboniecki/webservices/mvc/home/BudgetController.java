package pl.konradboniecki.webservices.mvc.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.models.account.Account;
import pl.konradboniecki.models.account.AccountRepository;
import pl.konradboniecki.models.expense.ExpenseRepository;
import pl.konradboniecki.models.family.Family;
import pl.konradboniecki.models.family.FamilyRepository;
import pl.konradboniecki.models.frontendforms.JarCreationForm;
import pl.konradboniecki.models.jar.Jar;
import pl.konradboniecki.models.jar.JarRepository;
import pl.konradboniecki.templates.ViewTemplate;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "home/budget")
public class BudgetController {

    @Autowired private AccountRepository accountRepository;
    @Autowired private FamilyRepository familyRepository;
    @Autowired private JarRepository jarRepository;
    @Autowired private ExpenseRepository expenseRepository;

    @GetMapping
    public ModelAndView showBudget(ModelMap modelMap) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Account> acc = accountRepository.findByEmail(email);
        Optional<Family> family = familyRepository.findById(acc.get().getFamilyId());
        List<Jar> jarList = jarRepository.findAllByBudgetId(family.get().getBudgetId());
        if (!jarList.isEmpty()){
            modelMap.addAttribute("jarList", jarList);
            modelMap.addAttribute("budgetId", family.get().getBudgetId());
            modelMap.addAttribute("expenseList", expenseRepository.findAllByBudgetId(family.get().getBudgetId()));
            return new ModelAndView(ViewTemplate.BUDGET_HOME_PAGE, modelMap);
        } else {
            modelMap.addAttribute("newJarCreationForm", new JarCreationForm());
            return new ModelAndView(ViewTemplate.JAR_CREATION_PAGE, modelMap);
        }
    }
}
