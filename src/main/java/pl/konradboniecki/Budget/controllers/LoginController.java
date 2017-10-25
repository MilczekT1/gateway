package pl.konradboniecki.Budget.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.konradboniecki.Budget.core.Utils;
import pl.konradboniecki.Budget.models.LoginForm;
import pl.konradboniecki.Budget.services.Account;
import pl.konradboniecki.Budget.services.AccountRepository;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class LoginController {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @PostMapping(value = "/login")
    public String login(@ModelAttribute ("loginForm") @Valid LoginForm loginForm,
                        BindingResult bindingResult,
                        Model model){
        if (bindingResult.hasErrors()) {
            return "login";
        } else {
            Optional<Account> account = accountRepository.findByEmail(loginForm.getEmail());
            if (account.isPresent()){
                String hashedTypedPassword = Utils.hashPassword(loginForm.getTypedPassword());
                if (hashedTypedPassword.equalsIgnoreCase(account.get().getPassword())){
                    model.addAttribute("userMainPaigeTitle", account.get().getFirstName() + " " + account.get().getLastName());
                    return "userMainPage";
                }
            }
            return null;
        }
    }
    @GetMapping(value = "/login")
    public String showLoginPage(Model model){
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }
}
