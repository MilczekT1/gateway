package pl.konradboniecki.webservices.mvc.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Throwables;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.models.account.Account;
import pl.konradboniecki.models.account.AccountRepository;
import pl.konradboniecki.models.newpassword.NewPassword;
import pl.konradboniecki.models.newpassword.NewPasswordForm;
import pl.konradboniecki.models.newpassword.NewPasswordRepository;
import pl.konradboniecki.utils.BudgetAdress;
import pl.konradboniecki.utils.TokenGenerator;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static pl.konradboniecki.utils.enums.ErrorType.PROCESSING_EXCEPTION;
import static pl.konradboniecki.utils.RestCall.performPostWithJSON;
import static pl.konradboniecki.utils.template.ViewTemplate.ERROR_PAGE;
import static pl.konradboniecki.utils.template.ViewTemplate.LOST_PASSWD_PAGE;

@Controller
public class ResetPasswordController {
    private static final Logger log = LoggerFactory.getLogger(ResetPasswordController.class);

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private NewPasswordRepository newPasswordRepository;

    @GetMapping (value = "/reset/{id}/{resetCode}")
    public ModelAndView changeLostPassword(@PathVariable(name="id") Long id,
                                           @PathVariable(name="resetCode") String resetCodeFromUrl){
        
        Optional<Account> accountOpt = accountRepository.findById(id);
        Optional<NewPassword> newPasswordOpt = newPasswordRepository.findById(id);
        if (accountOpt.isPresent() && newPasswordOpt.isPresent()){
            String correctResetCode = newPasswordOpt.get().getResetCode();

            if (resetCodeFromUrl.equals(correctResetCode)){
                String newPassword = newPasswordOpt.get().getNewPassword();
                accountRepository.changePassword(newPassword, id);
                newPasswordRepository.deleteById(id);
            }
        }
        return new ModelAndView("redirect:/login");
    }

    @GetMapping(value = "/reset/changePassword")
    public ModelAndView showLostPasswordForm(){
        return new ModelAndView(LOST_PASSWD_PAGE,"newPasswordForm", new NewPasswordForm());
    }

    @PostMapping (value = "/reset/changePassword")
    public ModelAndView processLostPasswordForm(@ModelAttribute ("newPasswordForm")
                                                @Valid NewPasswordForm newPasswordForm,
                                                BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return new ModelAndView(LOST_PASSWD_PAGE);
        else if (!newPasswordForm.checkRepeatedPassword())
            return new ModelAndView(LOST_PASSWD_PAGE,"repeatedPasswordFailure",true);

        Optional<Account> account = accountRepository.findByEmail(newPasswordForm.getEmail());
        if (account.isPresent()){
            String resetCode = TokenGenerator.createUUIDToken();

            NewPassword newPassword = new NewPassword(newPasswordForm);
            newPassword.setResetCode(resetCode);
            newPassword.setAccountId(account.get().getId());


            if (newPasswordRepository.existsById(newPassword.getAccountId())) {
                newPasswordRepository.deleteById(newPassword.getAccountId());
            }
            newPasswordRepository.save(newPassword);

            try {
                Map<String, Object> jsonObjects = new LinkedHashMap<>();
                jsonObjects.put("Account", account.get());
                jsonObjects.put("ResetCode", resetCode);
                String URL = BudgetAdress.getURI() + ":3002/services/mail/activation/new-password";
                performPostWithJSON(URL, jsonObjects);
            } catch (JsonProcessingException | UnirestException e) {
                System.out.println(Throwables.getStackTraceAsString(e));
                log.error(Throwables.getStackTraceAsString(e));
                return new ModelAndView(ERROR_PAGE, "errorType",
                        PROCESSING_EXCEPTION.getModelAttrName());
            }
        }
        
        return new ModelAndView("redirect:/");
    }
}