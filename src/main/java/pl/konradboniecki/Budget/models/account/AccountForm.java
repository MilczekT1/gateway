package pl.konradboniecki.Budget.models.account;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class AccountForm {
    
    @NotEmpty (message = "{register.firstNameRequired}")
    @Pattern (regexp = "[a-zA-Z]{3,}", message = "{register.firstNameRegex}")
    private String firstName;
    
    @NotEmpty(message = "{register.lastNameRequired}")
    @Pattern(regexp = "[a-zA-Z]{2,}", message = "{register.lastNameRegex}")
    private String lastName;
    
    @NotEmpty(message = "{register.emailRequired}")
    @Pattern(regexp = "(\\w||\\.)+@\\w+.[a-zA-Z]+", message = "{register.emailRegex}")
    private String email;
    
    @NotEmpty(message = "{register.passwordRequired}")
    @Size (min=6, max = 200, message = "{register.passwordSize}")
    private String password;
    
    @NotEmpty(message = "{register.repeatedPasswordRequired}")
    @Size (min=6, max = 200, message = "{register.repeatedPasswordSize}")
    private String repeatedPassword;
    
    @Pattern(regexp = "\\+?\\d{6,}", message = "{register.phoneNumberRegex}")
    private String phoneNumber;
    
    public AccountForm(){}
    
    public boolean checkRepeatedPassword(){
        return password.equals(repeatedPassword);
    }
}