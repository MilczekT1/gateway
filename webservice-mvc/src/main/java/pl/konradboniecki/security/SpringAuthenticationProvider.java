package pl.konradboniecki.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.konradboniecki.models.account.Account;
import pl.konradboniecki.models.account.AccountRepository;
import pl.konradboniecki.utils.TokenGenerator;

import java.util.Optional;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Component
@Scope(scopeName = SCOPE_SINGLETON)
public class SpringAuthenticationProvider implements AuthenticationProvider {
    
    @Autowired
    private AccountRepository accountRepository;
    
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
        //TODO: extract account repository
        Optional<Account> account = accountRepository.findByEmail(email);
        return (account.isPresent()
                && passwordHash.equals(account.get().getPassword())
                && account.get().isEnabled());
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
