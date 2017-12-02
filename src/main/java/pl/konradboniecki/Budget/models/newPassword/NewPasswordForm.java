package pl.konradboniecki.Budget.models.newPassword;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import pl.konradboniecki.Budget.core.Utils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class NewPasswordForm {
    
    @Getter
    @Setter
    @NotEmpty (message = "{lostPasswordForm.emailRequired}")
    @Pattern (regexp = "(\\w||\\.)+@\\w+.[a-zA-Z]+", message = "{lostPasswordForm.emailRegex}")
    private String email;
    
    @Getter
    @NotEmpty(message = "{lostPasswordForm.passwordRequired}")
    @Size (min=6, max = 200, message = "{lostPasswordForm.passwordSize}")
    private String password;
    
    @NotEmpty(message = "{lostPasswordForm.repeatedPasswordRequired}")
    @Size (min=6, max = 200, message = "{lostPasswordForm.repeatedPasswordSize}")
    private String repeatedPassword;
    
    private Long account_id;
    
    public boolean checkRepeatedPassword(){
        return password.equals(repeatedPassword);
    }
    
    public void setPassword(String password){
        this.password = Utils.hashPassword(password);
    }
    public void setRepeatedPassword(String password){
        this.repeatedPassword = Utils.hashPassword(password);
    }
}
