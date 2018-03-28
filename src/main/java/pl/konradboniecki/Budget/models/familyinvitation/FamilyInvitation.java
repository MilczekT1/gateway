package pl.konradboniecki.Budget.models.familyinvitation;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table
@Data
public class FamilyInvitation {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO, generator = "thisNameSuxInHibernate5")
    @GenericGenerator (name = "thisNameSuxInHibernate5", strategy = "increment")
    @Column(name="invitation_id")
    private Long id;
    
    @Column(name = "family_id")
    private Long familyId;
    @Column(name="email")
    private String email;
    @Column(name="apply_time")
    private ZonedDateTime applyTime;

    public FamilyInvitation() {
        ;
    }

    public FamilyInvitation(String email, Long familyId) {
        setEmail(email);
        setApplyTime(ZonedDateTime.now());
        setFamilyId(familyId);
    }
}
