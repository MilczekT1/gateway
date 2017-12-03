package pl.konradboniecki.Budget.controllers.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.Budget.models.account.Account;
import pl.konradboniecki.Budget.models.family.Family;
import pl.konradboniecki.Budget.models.family.FamilyCreationForm;
import pl.konradboniecki.Budget.services.AccountRepository;
import pl.konradboniecki.Budget.services.FamilyRepository;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Optional;

@Controller
@RequestMapping(value = "home")
public class FamilyController {
    
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    FamilyRepository familyRepository;
    
    @GetMapping ("/family")
    public ModelAndView showFamily() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Account> acc = accountRepository.findByEmail(email);
        
        if (acc.isPresent()){
            if (acc.get().hasFamily()){
                Optional<Family> familyOptional = familyRepository.findByOwnerId(acc.get().getId());
                return new ModelAndView("home/family", "familyObject",familyOptional.get());
            } else {
                HashMap<String, Object> modelAttributes = new HashMap<>();
                modelAttributes.put("newFamilyCreationForm", new FamilyCreationForm());
                return new ModelAndView("home/familyCreationForm", modelAttributes);
            }
        } else {
            return new ModelAndView("error");
        }
    }
    
    @PostMapping("/family/create")
    public ModelAndView createFamilyForm(@ModelAttribute("newFamilyCreationForm")
                                             @Valid FamilyCreationForm familyCreationForm,
                                         BindingResult bindingResult){
    
        if (bindingResult.hasErrors()) {
            return new ModelAndView("home/familyCreationForm");
        }
        
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Account> acc = accountRepository.findByEmail(email);
        
        Family family = new Family(familyCreationForm,acc.get().getId());
        family = familyRepository.save(family);
        
        accountRepository.setFamilyId(family.getId(),acc.get().getId());
        
        return new ModelAndView("redirect:/home/family","familyObject", family);
    }
}
