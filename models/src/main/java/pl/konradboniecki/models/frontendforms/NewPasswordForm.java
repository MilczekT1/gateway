package pl.konradboniecki.models.frontendforms;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class NewPasswordForm {

    @NotEmpty (message = "{lostPasswordForm.emailRequired}")
    @Pattern (regexp = "(\\w||\\.)+@\\w+.[a-zA-Z]+", message = "{lostPasswordForm.emailRegex}")
    private String email;
    
    @Getter
    @NotEmpty (message = "{lostPasswordForm.passwordRequired}")
    @Size (min=6, max = 200, message = "{lostPasswordForm.passwordSize}")
    private String password;
    
    @NotEmpty (message = "{lostPasswordForm.repeatedPasswordRequired}")
    @Size (min=6, max = 200, message = "{lostPasswordForm.repeatedPasswordSize}")
    private String repeatedPassword;
    
    public boolean isRepeatedPasswordTheSame(){
        if (password == null)
            throw new NullPointerException();
        if (repeatedPassword == null)
            throw new NullPointerException();
        return password.equals(repeatedPassword);
    }
}
