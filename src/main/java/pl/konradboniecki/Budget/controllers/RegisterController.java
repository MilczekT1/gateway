package pl.konradboniecki.Budget.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.konradboniecki.Budget.services.MailService;
import pl.konradboniecki.Budget.models.AccountForm;
import pl.konradboniecki.Budget.models.LoginForm;
import pl.konradboniecki.Budget.models.Account;
import pl.konradboniecki.Budget.services.AccountRepository;


import javax.validation.Valid;

@Controller
public class RegisterController {
    
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MailService mailService;
    
    // dzieki modelAttribute, do nastepnej templatki zostanie wyslany obiekt wypelniony poprzednimi danymi
    @PostMapping("/register")
    public String register(@RequestParam (value = "repeatedPassword") String repeatedPassword,
                           @ModelAttribute("accountFormObject") @Valid AccountForm newAccountForm,
                           BindingResult bindingResult,
                           Model model){
        
        if (bindingResult.hasErrors()) {
            return "registration";
        }
    
        if (!newAccountForm.checkRepeatedPassword()){
            model.addAttribute("repeatedPasswordFailure","confirmed password is not the same!");
            return "registration";
        }
        
        Account acc = new Account(newAccountForm);
        if (!accountRepository.existsByEmail(acc.getEmail())){
            accountRepository.save(acc);
            mailService.sendSignUpConfirmation(acc);
            model.addAttribute("loginForm", new LoginForm());
            return "login";
        }
        
        return "error";
    }
    
    @GetMapping("/register")
    public String showRegisterPane(Model model){
        model.addAttribute("accountFormObject", new AccountForm());
        return "registration";
    }
}