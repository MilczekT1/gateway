package pl.konradboniecki.Budget.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.Budget.core.Utils;
import pl.konradboniecki.Budget.services.MailService;
import pl.konradboniecki.Budget.models.account.AccountForm;
import pl.konradboniecki.Budget.models.account.Account;
import pl.konradboniecki.Budget.services.AccountRepository;

import javax.validation.Valid;

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
            return new ModelAndView("auth/registration", "repeatedPasswordFailure",true);
        }
        
        Account acc = new Account(newAccountForm);
        if (!accountRepository.existsByEmail(acc.getEmail())){
            accountRepository.save(acc);
            acc = accountRepository.findByEmail(acc.getEmail()).get();
            
            String generatedActivationCode = Utils.createActivationCode(acc.getEmail());
            mailService.sendSignUpConfirmation(acc, generatedActivationCode);
            return new ModelAndView("auth/registrationSuccessInfo");
        } else {
            return new ModelAndView("auth/registration","emailAlreadyExists", true);
        }
    }
    
    @GetMapping("/register")
    public ModelAndView showRegisterPane(){
        return new ModelAndView("auth/registration","accountFormObject", new AccountForm());
    }
}