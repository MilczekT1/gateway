package pl.konradboniecki.webservices.mvc.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Throwables;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.models.account.Account;
import pl.konradboniecki.models.account.AccountRepository;
import pl.konradboniecki.models.frontendforms.AccountForm;
import pl.konradboniecki.models.useractivationcode.UserActivationCode;
import pl.konradboniecki.models.useractivationcode.UserActivationCodeRepository;
import pl.konradboniecki.utils.BudgetAdress;
import pl.konradboniecki.utils.RestCall;
import pl.konradboniecki.utils.TokenGenerator;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

import static pl.konradboniecki.templates.ViewTemplate.*;
import static pl.konradboniecki.utils.enums.ErrorType.PROCESSING_EXCEPTION;

@Log
@Controller
public class RegisterController {
    
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserActivationCodeRepository userActivationCodeRepository;
    @Autowired
    private RestCall restCall;

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute("accountFormObject") @Valid AccountForm newAccountForm,
                                 BindingResult bindingResult, Model model) {
        
        if (bindingResult.hasErrors()) {
            return new ModelAndView(REGISTRATION_PAGE);
        } else if (!newAccountForm.checkRepeatedPassword()){
            return new ModelAndView(REGISTRATION_PAGE, "repeatedPasswordFailure",true);
        }
        
        Account acc = new Account(newAccountForm);
        if (!accountRepository.existsByEmail(acc.getEmail())){
            accountRepository.save(acc);
            acc = accountRepository.findByEmail(acc.getEmail()).get();
            String token = new TokenGenerator().createUUIDToken();
            userActivationCodeRepository.save(new UserActivationCode(acc.getId(), token));
            try {
                Map<String, Object> jsonObjects = new LinkedHashMap<>();
                jsonObjects.put("Account", acc);
                jsonObjects.put("ActivationCode", token);
                String url = BudgetAdress.getURI() + ":3002/api/mail/activate-account";
                restCall.performPostWithJSON(url, jsonObjects);
            } catch (JsonProcessingException | UnirestException  e) {
                log.severe(Throwables.getStackTraceAsString(e));
                return new ModelAndView(ERROR_PAGE, "errorType",
                        PROCESSING_EXCEPTION.getErrorTypeVarName());
            }

            return new ModelAndView(REGISTRATION_SUCCESS_MSG);
        } else {
            return new ModelAndView(REGISTRATION_PAGE,"emailAlreadyExists", true);
        }
    }

    @GetMapping("/register")
    public ModelAndView showRegisterPane(){
        return new ModelAndView(REGISTRATION_PAGE,"accountFormObject", new AccountForm());
    }
}