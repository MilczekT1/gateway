package pl.konradboniecki.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.konradboniecki.ServiceManager;
import pl.konradboniecki.models.Account;
import pl.konradboniecki.utils.TokenGenerator;

import java.util.Optional;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Slf4j
@Component
@Scope(scopeName = SCOPE_SINGLETON)
public class SpringAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private ServiceManager serviceManager;
    
    @Override
    public Authentication authenticate(Authentication authentication) {
        String email = authentication.getName();
        email = email.toLowerCase();
        String password = authentication.getCredentials().toString();
        String hashedTypedPassword = new TokenGenerator().hashPassword(password);
        
        if(authenticationIsCorrect(email, hashedTypedPassword)){
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new UsernamePasswordAuthenticationToken(email, hashedTypedPassword, authentication.getAuthorities());
        } else {
            return null;
        }
    }
    
    private boolean authenticationIsCorrect(String email, String passwordHash){
        Optional<Account> account = serviceManager.findAccountByEmail(email);
        if (account.isPresent() && account.get().isEnabled()){
            return serviceManager.isPasswordCorrectForAccount(account.get().getId(), passwordHash);
        } else {
            return false;
        }
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
