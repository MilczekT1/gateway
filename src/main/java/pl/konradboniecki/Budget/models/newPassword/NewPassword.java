package pl.konradboniecki.Budget.models.newPassword;

import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table
@Data
public class NewPassword {
    @Id
    @Column(name="account_id")
    private Long id;
    
    @Column(name="new_password")
    private String nextPassword;
    @Column(name="apply_time")
    private ZonedDateTime ApplyTime;
    
    public NewPassword(){;}
    
    public NewPassword(NewPasswordForm newPasswordForm){
        setId(newPasswordForm.getAccount_id());
        setApplyTime(ZonedDateTime.now());
        setNextPassword(newPasswordForm.getPassword());
    }
}
