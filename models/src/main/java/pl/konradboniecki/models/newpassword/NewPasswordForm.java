package pl.konradboniecki.models.newpassword;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
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
    
    public boolean checkRepeatedPassword(){
        return password.equals(repeatedPassword);
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    public void setRepeatedPassword(String repeatedPassword){
        if (repeatedPassword == null || !repeatedPassword.equals(this.password)){
            throw new IllegalArgumentException("Repeated password is not the same");
        }
        this.repeatedPassword = repeatedPassword;
    }
}
