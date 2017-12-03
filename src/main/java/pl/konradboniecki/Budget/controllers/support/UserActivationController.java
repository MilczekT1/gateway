package pl.konradboniecki.Budget.controllers.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.Budget.core.ErrorType;
import pl.konradboniecki.Budget.core.Utils;
import pl.konradboniecki.Budget.models.account.Account;
import pl.konradboniecki.Budget.services.AccountRepository;

import java.util.Optional;

@Controller
public class UserActivationController {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @GetMapping (value = "/activate/{id}/{activationCode}")
    public ModelAndView activateUser(@PathVariable (name = "id") Long id,
                                     @PathVariable(name = "activationCode") String activationCodeFromUrl){
        
        Optional<Account> acc = accountRepository.findById(id);
        if (acc.isPresent()){
            if(acc.get().isEnabled()){
                return new ModelAndView("redirect:/login");
            } else {
                if (Utils.isActivationCodeValid(acc.get(),activationCodeFromUrl)) {
                    accountRepository.setEnabled(id);
                    return new ModelAndView("redirect:/login");
                }
                else {
                    return new ModelAndView("error", "errorType", ErrorType.INVALID_ACTIVATION_LINK.getModelAttributeName());
                }
            }
        } else {
            return new ModelAndView("redirect:/register");
        }
    }
}
