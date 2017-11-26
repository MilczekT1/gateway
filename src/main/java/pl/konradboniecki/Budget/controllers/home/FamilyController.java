package pl.konradboniecki.Budget.controllers.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.Budget.models.account.Account;
import pl.konradboniecki.Budget.models.family.Family;
import pl.konradboniecki.Budget.services.AccountRepository;

import java.util.HashMap;
import java.util.Optional;

@Controller
@RequestMapping(value = "home")
public class FamilyController {
    
    @Autowired
    AccountRepository accountRepository;
    
    @GetMapping ("/family")
    public ModelAndView xxx() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Account> acc = accountRepository.findByEmail(email);
        
        if (acc.isPresent()){
            if (acc.get().hasFamily()){
                //TODO: provide family object for thymeleaf
                return new ModelAndView("home/family");
            } else {
                //TODO: create family somehow
                HashMap<String, Family> modelAttributes = new HashMap<>();
                modelAttributes.put("familyObject", new Family());
                return new ModelAndView("redirect:/family/create", modelAttributes);
            }
        } else {
            return new ModelAndView("error");
        }
    }
}
