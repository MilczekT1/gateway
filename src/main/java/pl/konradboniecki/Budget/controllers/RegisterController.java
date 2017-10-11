package pl.konradboniecki.Budget.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.konradboniecki.Budget.models.AccountForm;
import pl.konradboniecki.Budget.services.Account;
import pl.konradboniecki.Budget.services.AccountRepository;


import javax.validation.Valid;

@Controller
public class RegisterController {
    
    @Autowired
    private AccountRepository accountRepository;
    
    // dzieki modelAttribute, do nastepnej templatki zostanie wyslany obiekt wypelniony poprzednimi danymi
    @PostMapping("/register")
    public String register(@RequestParam (value = "repeatedPassword") String repeatedPassword,
                           @ModelAttribute("accountFormObject") @Valid AccountForm newAccountForm,
                           BindingResult bindingResult , Model model){
        
        if (bindingResult.hasErrors()) {
            if (!newAccountForm.checkRepeatedPassword()){
                //TODO: repair this custom info as international string
                model.addAttribute("repeatedPasswordFailure","confirmed password is not the same!");
            }
            return "registration";
        }
        
        accountRepository.save(new Account(newAccountForm));
        return "index";
    }
    
    @GetMapping("/register")
    public String showRegisterPane(Model model){
        model.addAttribute("accountFormObject", new AccountForm());
        model.addAttribute("firstNameDescription", "Imię:");
        model.addAttribute("lastNameDescription", "Nazwisko:");
        model.addAttribute("birthdayDescription", "Data Urodzenia:");
        model.addAttribute("emailDescription", "Adres email:");
        model.addAttribute("passwordDescription", "Hasło (min 6 znaków):");
        model.addAttribute("repeatedPasswordDescription", "Powtórz hasło:");
        model.addAttribute("phoneNumberDescription", "Numer telefonu:");
        return "registration";
    }
}
