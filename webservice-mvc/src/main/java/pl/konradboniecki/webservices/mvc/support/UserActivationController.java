package pl.konradboniecki.webservices.mvc.support;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.models.account.Account;
import pl.konradboniecki.models.account.AccountRepository;
import pl.konradboniecki.models.useractivationcode.UserActivationCode;
import pl.konradboniecki.models.useractivationcode.UserActivationCodeRepository;

import java.util.Optional;

import static pl.konradboniecki.templates.ViewTemplate.ERROR_PAGE;
import static pl.konradboniecki.utils.enums.ErrorType.INVALID_ACTIVATION_LINK;

@Log
@Controller
public class UserActivationController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserActivationCodeRepository userActivationCodeRepository;

    @GetMapping(value = "/activate/{id}/{activationCode}")
    public ModelAndView activateUser(@PathVariable(name = "id") Long id,
                                     @PathVariable(name = "activationCode") String activationCodeFromUrl) {

        Optional<Account> acc = accountRepository.findById(id);
        if (acc.isPresent()) {
            Optional<UserActivationCode> activationCode =
                    userActivationCodeRepository.findByAccountId(acc.get().getId());
            if (acc.get().isEnabled()) {
                return new ModelAndView("redirect:/login");
            } else {
                if (activationCode.isPresent()
                        && activationCode.get().getActivationCode().equals(activationCodeFromUrl)) {
                        accountRepository.setEnabled(id);
                        log.info("User with ID: " + acc.get().getId() + " has been activated");
                        userActivationCodeRepository.deleteById(activationCode.get().getId());
                        return new ModelAndView("redirect:/login");
                }
                return new ModelAndView(ERROR_PAGE.getFilename(), "errorType", INVALID_ACTIVATION_LINK.getErrorTypeVarName());
            }
        } else {
            return new ModelAndView("redirect:/register");
        }
    }
}