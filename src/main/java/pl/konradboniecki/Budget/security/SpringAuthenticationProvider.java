package pl.konradboniecki.Budget.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.konradboniecki.Budget.core.Utils;
import pl.konradboniecki.Budget.models.account.Account;
import pl.konradboniecki.Budget.services.AccountRepository;

import java.util.Optional;

@Component
public class SpringAuthenticationProvider implements AuthenticationProvider {
    
    @Autowired
    AccountRepository accountRepository;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        email = email.toLowerCase();
        String password = authentication.getCredentials().toString();
        String hashedTypedPassword = Utils.hashPassword(password);
    
        
        if(authenticationIsCorrect(email,hashedTypedPassword)){
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new UsernamePasswordAuthenticationToken(email, hashedTypedPassword, authentication.getAuthorities());
        } else {
            return null;
        }
    }
    
    private boolean authenticationIsCorrect(String email, String passwordHash){
        Optional<Account> account = accountRepository.findByEmail(email);
        if (account.isPresent() &&
                    passwordHash.equals(account.get().getPassword()) &&
                    account.get().isEnabled()){
                return true;
        }
        else {
            return false;
        }
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
