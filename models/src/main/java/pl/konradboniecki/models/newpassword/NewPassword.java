package pl.konradboniecki.models.newpassword;

import lombok.Data;
import pl.konradboniecki.models.frontendforms.NewPasswordForm;
import pl.konradboniecki.utils.TokenGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "new_password")
@Data
public class NewPassword {
    @Id
    @Column(name = "account_id")
    private Long accountId;
    
    @Column(name = "new_password")
    private String newPasswordProp;
    @Column(name = "apply_time")
    private ZonedDateTime applyTime;
    @Column(name = "reset_code")
    private String resetCode;
    
    public NewPassword(){
        ;
    }

    public NewPassword(NewPasswordForm newPasswordForm){
        setApplyTime(ZonedDateTime.now());
        setNewPasswordProp(newPasswordForm.getPassword());
    }

    public void setNewPasswordProp(String passwd){
        this.newPasswordProp = new TokenGenerator().hashPassword(passwd);
    }
}