package pl.konradboniecki.Budget.services;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import pl.konradboniecki.Budget.core.Utils;
import pl.konradboniecki.Budget.models.AccountForm;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Table(name = "Account")
@Entity(name = "Account")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "thisNameSucksInHibernate5")
    @GenericGenerator(name = "thisNameSucksInHibernate5", strategy = "increment")
    private Long id;
    @Column(name = "family_id")
    private Long familyId;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column (name = "nick")
    private String nick;
    
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Transient
    //@Column(name = "")
    private String dateOfBirth;
    @Column(name = "date_of_registration")
    private LocalDateTime dateOfRegistration;
    
    public Account(){
        ;
    }
    
    public Account(AccountForm accountForm){
        setFirstName(accountForm.getFirstName());
        setLastName(accountForm.getLastName());
        setNick(accountForm.getNick());
        setEmail(accountForm.getEmail());
        setPassword(accountForm.getPassword());
        setPhoneNumber(accountForm.getPhoneNumber());
        setDateOfBirth(accountForm.getDateOfBirth());
        setDateOfRegistration(LocalDateTime.now());
    }
    
    public void setPassword(String password) {
        this.password = Utils.hashPassword(password);
    }
}
