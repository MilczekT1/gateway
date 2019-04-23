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
    
    public Family(){
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
    }

    public Family(JsonNode jsonNode){
        if (jsonNode.has("id")) {
            Long id = jsonNode.path("id").asLong();
            if (id != 0L) setId(id);
        }
        if (jsonNode.has("ownerId")) {
            Long ownerId = jsonNode.path("ownerId").asLong();
            if (ownerId != 0L) setOwnerId(ownerId);
        }
        if (jsonNode.has("budgetId")) {
            Long budgetId = jsonNode.path("budgetId").asLong();
            if (budgetId != 0L) setId(budgetId);
        }
        if (jsonNode.has("maxMembers")) {
            Integer maxMembers = jsonNode.path("maxMembers").asInt();
            if (maxMembers != 0) setMaxMembers(maxMembers);
        }
        if (jsonNode.has("title")) setTitle(jsonNode.path("title").asText());
    }
}