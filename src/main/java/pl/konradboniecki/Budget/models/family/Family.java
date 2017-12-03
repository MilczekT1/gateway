package pl.konradboniecki.Budget.models.family;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
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
    private Long budget_id;
    
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
    
    
    public Family(){;}
    public Family(FamilyCreationForm familyCreationForm){
        setTitle(familyCreationForm.getTitle());
        setEmailNotificationsEnabled(false);
        setPhoneNotificationsEnabled(false);
        setMaxJars(6);
        setMaxMembers(5);
    }
    
    public Family(FamilyCreationForm familyCreationForm, Long owner_id){
        this(familyCreationForm);
        setOwnerId(owner_id);
    }
}
