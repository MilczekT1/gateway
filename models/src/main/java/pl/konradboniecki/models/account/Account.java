package pl.konradboniecki.models.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import pl.konradboniecki.utils.enums.UserType;
import pl.konradboniecki.utils.TokenGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Data
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "thisNameSuxInHibernate5")
    @GenericGenerator(name = "thisNameSuxInHibernate5", strategy = "increment")
    @Column(name = "account_id")
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
    @JsonIgnore
    @Column(name = "password")
    private String password;
    @JsonIgnore
    @Column(name = "date_of_registration")
    private ZonedDateTime registerDate;
    
    @Column(name = "role")
    private String role;
    @Column(name = "enabled")
    private boolean enabled;
    
    public Account(){
        ;
    }
    
    public Account(AccountForm accountForm){
        setFirstName(accountForm.getFirstName());
        setLastName(accountForm.getLastName());
        setEmail(accountForm.getEmail().toLowerCase());
        setPassword(accountForm.getPassword());
        setPhoneNumber(accountForm.getPhoneNumber());
        registerDate = ZonedDateTime.now(ZoneId.of("Europe/Warsaw"));
        setRole(UserType.USER.getRole());
        setEnabled(false);
    }

    public Account(String jsonObjectName, ObjectNode json){
        if (jsonObjectName == null)
            jsonObjectName = "Account";

        // no password and register date
        JsonNode accountNode = json.get(jsonObjectName);
        if (accountNode.has("id")) setId(accountNode.get("id").asLong());
        if (accountNode.has("familyId")) setFamilyId(accountNode.get("familyId").asLong());
        if (accountNode.has("firstName")) setFirstName(accountNode.get("firstName").asText());
        if (accountNode.has("lastName")) setLastName(accountNode.get("lastName").asText());
        if (accountNode.has("email")) setEmail(accountNode.get("email").asText());
        if (accountNode.has("role")) setRole(accountNode.get("role").asText());
        if (accountNode.has("enabled")) setEnabled(accountNode.get("enabled").asBoolean());
    }
    
    public void setPassword(String password) {
        this.password = TokenGenerator.hashPassword(password);
    }
    
    public boolean hasFamily(){
        return familyId != null ? true : false;
    }
}