package pl.konradboniecki.Budget.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.Budget.core.ErrorType;
import pl.konradboniecki.Budget.core.Utils;
import pl.konradboniecki.Budget.services.MailService;
import pl.konradboniecki.Budget.models.account.AccountForm;
import pl.konradboniecki.Budget.models.account.Account;
import pl.konradboniecki.Budget.services.AccountRepository;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class RegisterController {
    
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MailService mailService;
    
    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute("accountFormObject") @Valid AccountForm newAccountForm,
                                 BindingResult bindingResult){
        
        if (bindingResult.hasErrors()) {
            return new ModelAndView("auth/registration");
        } else if (!newAccountForm.checkRepeatedPassword()){
            return new ModelAndView("auth/registration", "repeatedPasswordFailure","confirmed password is not the same!");
        }
        
        Account acc = new Account(newAccountForm);
        if (!accountRepository.existsByEmail(acc.getEmail())){
            accountRepository.save(acc);
            acc = accountRepository.findByEmail(acc.getEmail()).get();
            
            String generatedActivationCode = Utils.createActivationCode(acc.getEmail());
            mailService.sendSignUpConfirmation(acc, acc.getId(), generatedActivationCode);
            return new ModelAndView("auth/registrationSuccessInfo");
        } else {
            return new ModelAndView("auth/registration","emailAlreadyExists", true);
        }
    }
    
    @GetMapping(value = "/activate/{id}/{activationCode}")
    public ModelAndView activateUser(@PathVariable(name = "id") Long id,
                                     @PathVariable(name = "activationCode") String activationCodeFromUrl){
        
        Optional<Account> acc = accountRepository.findById(id);
        if (acc.isPresent()){
            if(acc.get().isEnabled()){
                return new ModelAndView("redirect:/login");
            } else {
                if (isActivationCodeValid(acc.get(),activationCodeFromUrl)) {
                    accountRepository.setEnabled(id);
                    return new ModelAndView("redirect:/login");
                }
                else {
                    return new ModelAndView("error", "errorType",ErrorType.INVALID_ACTIVATION_LINK.getModelAttributeName());
                }
            }
        } else {
            return new ModelAndView("redirect:/register");
        }
    }
    
    @GetMapping("/register")
    public ModelAndView showRegisterPane(){
        return new ModelAndView("auth/registration","accountFormObject", new AccountForm());
    }
    
    private boolean isActivationCodeValid(Account acc, String activationCodeFromUrl){
        return Utils.createActivationCode(acc.getEmail()).equals(activationCodeFromUrl) ? true : false;
    }
}