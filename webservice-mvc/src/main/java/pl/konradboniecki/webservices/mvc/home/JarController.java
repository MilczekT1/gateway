package pl.konradboniecki.webservices.mvc.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.ServiceManager;
import pl.konradboniecki.models.Account;
import pl.konradboniecki.models.Budget;
import pl.konradboniecki.models.frontendforms.JarCreationForm;
import pl.konradboniecki.models.jar.Jar;
import pl.konradboniecki.models.jar.JarRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static pl.konradboniecki.templates.ViewTemplate.*;

@Controller
@RequestMapping(value = "home/budget")
public class JarController {

    @Autowired private ServiceManager serviceManager;
    @Autowired private JarRepository jarRepository;

    @GetMapping("/create-jar")
    public ModelAndView createJar(ModelMap modelMap){
        modelMap.put("newJarCreationForm", new JarCreationForm());
        return new ModelAndView(JAR_CREATION_PAGE, modelMap);
    }

    @PostMapping("/create-jar")
    public ModelAndView createJar(
            @ModelAttribute("newJarCreationForm") @Valid JarCreationForm jarCreationForm, BindingResult bindingResult, ModelMap modelMap){
        if (bindingResult.hasErrors()) {
            return new ModelAndView(JAR_CREATION_PAGE);
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Account> accOpt = serviceManager.findAccountByEmail(email);
        if (!accOpt.isPresent()){
            throw new RuntimeException("Account doesn't exist");
        }
        Account acc = accOpt.get();

        Optional<Budget> budgetOpt = serviceManager.findBudgetByFamilyId(acc.getFamilyId());
        if (!budgetOpt.isPresent()){
            throw new RuntimeException("Budget doesn't exist");
        }
        Budget budget = budgetOpt.get();

        List<Jar> jarList = jarRepository.findAllByBudgetId(budget.getId());
        if (jarList.size() < budget.getMaxJars()) {
            Jar jar = new Jar(jarCreationForm);
            jar.setBudgetId(budget.getId());
            jarRepository.save(jar);
            jarList = jarRepository.findAllByBudgetId(budget.getId());
            modelMap.addAttribute("jarList", jarList);
            return new ModelAndView(BUDGET_HOME_PAGE, modelMap);
        } else {
            modelMap.put("maxJarsAmountExceeded", true);
            modelMap.put("jarList", jarList);
            return new ModelAndView(BUDGET_HOME_PAGE, modelMap);
        }
    }

    @PostMapping("/remove-jar")
    public ModelAndView removeJarFromBudget(@RequestParam("jarId") Long jarId, ModelMap modelMap){
        jarRepository.deleteById(jarId);
        return new ModelAndView("redirect:/" + BUDGET_HOME_PAGE, modelMap);
    }

    @PostMapping("/change-current-amount")
    public ModelAndView changeCurrentAmountInJarWithId(
            @RequestParam("jarId") Long jarId,
            @RequestParam("amount") Long amount,
            ModelMap modelMap){

        Optional<Jar> jarOpt = jarRepository.findById(jarId);
        if (jarOpt.isPresent()){
            Jar jar = jarOpt.get();
            jar.setCurrentAmount(jar.getCurrentAmount() + amount);
            jarRepository.save(jar);
//            Long newAmount = jar.getCurrentAmount() + amount;
//            jarRepository.setCurrentAmount(newAmount, jar.getId());
            return new ModelAndView("redirect:/" + BUDGET_HOME_PAGE, modelMap);
        } else {
            return new ModelAndView(ERROR_PAGE, modelMap);
        }

    }
}