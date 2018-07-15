package pl.konradboniecki.models.family;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import pl.konradboniecki.models.frontendforms.FamilyCreationForm;

import javax.persistence.*;

@Data
@Table
@Entity
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "thisNameSucksInHibernate5")
    @GenericGenerator(name = "thisNameSucksInHibernate5", strategy = "increment")
    @Column(name="family_id")
    private Long id;
    
    @Column(name="owner_id")
    private Long ownerId;
    @Column(name="budget_id")
    private Long budgetId;
    @Column(name = "title")
    private String title;
    
    @Column(name = "max_members")
    private Integer maxMembers;
    @Column(name = "max_jars")
    private Integer maxJars;
    @Column(name = "email_notif_enabled")
    private boolean emailNotificationsEnabled;
    @Column(name = "phone_notif_enabled")
    private boolean phoneNotificationsEnabled;
    
    
    public Family(){
        setEmailNotificationsEnabled(false);
        setPhoneNotificationsEnabled(false);
        setMaxJars(6);
        setMaxMembers(5);
    }
    public Family(FamilyCreationForm familyCreationForm){
        this();
        setTitle(familyCreationForm.getTitle());
    }
    public Family(FamilyCreationForm familyCreationForm, Long ownerId){
        this(familyCreationForm);
        setOwnerId(ownerId);
    }
    public Family(String jsonObjectName, ObjectNode json) {
        this();
        if (jsonObjectName == null)
            throw new NullPointerException("Family has been initialized with null");
        if (jsonObjectName.equals(""))
            throw new IllegalArgumentException("Family has been initialized with either \"\"");

        JsonNode familyNode = json.get(jsonObjectName);
        if (familyNode.has("id")) setId(familyNode.get("id").asLong());
        if (familyNode.has("ownerId")) setOwnerId(familyNode.get("ownerId").asLong());
        if (familyNode.has("budgetId")) setBudgetId(familyNode.get("budgetId").asLong(0));
        if (familyNode.has("title")) setTitle(familyNode.get("title").asText());
        if (familyNode.has("maxMembers")) setMaxMembers(familyNode.get("maxMembers").asInt());
        if (familyNode.has("maxJars")) setMaxJars(familyNode.get("maxJars").asInt());
        if (familyNode.has("emailNotificationsEnabled"))
            setEmailNotificationsEnabled(familyNode.get("emailNotificationsEnabled").asBoolean());
        if (familyNode.has("phoneNotificationsEnabled"))
            setPhoneNotificationsEnabled(familyNode.get("phoneNotificationsEnabled").asBoolean());
    }
}