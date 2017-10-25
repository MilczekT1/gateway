package pl.konradboniecki.Budget.services;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import pl.konradboniecki.Budget.core.UserType;
import pl.konradboniecki.Budget.core.Utils;
import pl.konradboniecki.Budget.models.AccountForm;
import javax.persistence.*;
import java.time.*;
import java.time.ZonedDateTime;

@Table(name = "Account")
@Entity(name = "Account")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "thisNameSuxInHibernate5")
    @GenericGenerator(name = "thisNameSuxInHibernate5", strategy = "increment")
    @Column(name = "id")
    private Long id;
    
    @Column(name = "family_id")
    private Long familyId;
    
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "password")
    private String password;
    
    @Column(name = "date_of_registration")
    private ZonedDateTime registerDate;
    
    @Column(name = "role")
    private String role;
    
    public Account(){
        ;
    }
    
    public Account(AccountForm accountForm){
        setFirstName(accountForm.getFirstName());
        setLastName(accountForm.getLastName());
        setEmail(accountForm.getEmail());
        setPassword(Utils.hashPassword(accountForm.getPassword()));
        setPhoneNumber(accountForm.getPhoneNumber());
        registerDate = ZonedDateTime.now(ZoneId.of("Europe/Warsaw"));
        setRole(UserType.USER.getRole());
    }
    
    public void setPassword(String password) {
        this.password = Utils.hashPassword(password);
    }
}
