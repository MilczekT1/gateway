package pl.konradboniecki.Budget.controllers.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.Budget.core.Utils;
import pl.konradboniecki.Budget.models.account.Account;
import pl.konradboniecki.Budget.models.newPassword.NewPassword;
import pl.konradboniecki.Budget.models.newPassword.NewPasswordForm;
import pl.konradboniecki.Budget.services.AccountRepository;
import pl.konradboniecki.Budget.services.MailService;
import pl.konradboniecki.Budget.services.NewPasswordRepository;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class ResetPasswordController {
    
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private NewPasswordRepository newPasswordRepository;
    @Autowired
    private MailService mailService;
    
    @GetMapping (value = "/reset/{id}/{resetCode}")
    public ModelAndView changeForgottenPassword(@PathVariable (name="id") Long id,
                                                @PathVariable(name = "resetCode") String resetCodeFromUrl){
        
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent()){
            String correctResetCode = Utils.createNewPasswordConfirmationCode(account.get().getPassword());
            if (resetCodeFromUrl.equals(correctResetCode)){
                Optional<NewPassword> newPasswordOptional = newPasswordRepository.findById(id);
                if (newPasswordOptional.isPresent()){
                    String newPassword = newPasswordOptional.get().getNextPassword();
                    accountRepository.changePassword(newPassword,id);
                    newPasswordRepository.deleteById(id);
                }
            }
        }
        return new ModelAndView("redirect:/login");
    }
    
    @GetMapping(value = "/reset/changePassword")
    public ModelAndView showLostPasswordForm(){
        return new ModelAndView("lostPasswordForm","newPasswordForm", new NewPasswordForm());
    }
    
    @PostMapping (value = "/reset/changePassword")
    public ModelAndView processLostPasswordForm(@ModelAttribute ("newPasswordForm")
                                                @Valid NewPasswordForm newPasswordForm,
                                                BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new ModelAndView("lostPasswordForm");
        } else if (!newPasswordForm.checkRepeatedPassword()){
            return new ModelAndView("lostPasswordForm","repeatedPasswordFailure",true);
        }
        Optional<Account> account = accountRepository.findByEmail(newPasswordForm.getEmail());
        if (account.isPresent()){
            newPasswordForm.setAccount_id(account.get().getId());
            
            NewPassword newPassword = new NewPassword(newPasswordForm);
            newPasswordRepository.save(newPassword);
            
            String code = Utils.createNewPasswordConfirmationCode(account.get().getPassword());
            mailService.sendNewPasswordActivationMail(account.get(), code);
        }
        
        return new ModelAndView("redirect:/");
    }
}
