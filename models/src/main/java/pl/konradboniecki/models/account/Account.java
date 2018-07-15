package pl.konradboniecki.models.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import pl.konradboniecki.utils.TokenGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static pl.konradboniecki.utils.enums.UserType.USER;

@Entity
@Data
@NoArgsConstructor
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
    
    public Account(AccountForm accountForm){
        setFirstName(accountForm.getFirstName());
        setLastName(accountForm.getLastName());
        setEmail(accountForm.getEmail().toLowerCase());
        setPassword(accountForm.getPassword());
        setPhoneNumber(accountForm.getPhoneNumber());
        registerDate = ZonedDateTime.now(ZoneId.of("Europe/Warsaw"));
        setRole(USER.getRoleName());
        setEnabled(false);
    }

    public Account(String jsonObjectName, ObjectNode json){
        if (jsonObjectName == null)
            throw new NullPointerException("Account has been initialized with null");
        if (jsonObjectName.equals(""))
            throw new IllegalArgumentException("Account has been initialized with either \"\"");

        // no password and register date
        JsonNode accNode = json.get(jsonObjectName);

        if (accNode.has("id")) setId(accNode.get("id").asLong());
        if (accNode.has("familyId")) setFamilyId(accNode.get("familyId").asLong());
        if (accNode.has("firstName")) setFirstName(accNode.get("firstName").asText());
        if (accNode.has("lastName")) setLastName(accNode.get("lastName").asText());
        if (accNode.has("email")) setEmail(accNode.get("email").asText());
        if (accNode.has("phoneNumber")) setPhoneNumber(accNode.get("phoneNumber").asText());
        if (accNode.has("role")) setRole(accNode.get("role").asText());
        if (accNode.has("enabled")) setEnabled(accNode.get("enabled").asBoolean());
    }
    
    public void setPassword(String password) {
        this.password = new TokenGenerator().hashPassword(password);
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public boolean hasFamily(){
        return familyId != null;
    }
}