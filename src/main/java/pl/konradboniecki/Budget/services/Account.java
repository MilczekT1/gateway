package pl.konradboniecki.Budget.services;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import pl.konradboniecki.Budget.models.AccountForm;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Table (name = "Account")
@Entity
@Data
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    
    private Long familyId;
    private String firstName;
    private String lastName;
    private String age;
    private String email;
    private String phoneNumber;
    private String password;
    
    public Account(){
        ;
    }
    
    public Account(AccountForm accountForm){
        setFirstName(accountForm.getFirstName());
        setLastName(accountForm.getLastName());
        setEmail(accountForm.getEmail());
        setPassword(accountForm.getPassword());
        setPhoneNumber(accountForm.getPhoneNumber());
        setAge(accountForm.getAge());
    }
}
