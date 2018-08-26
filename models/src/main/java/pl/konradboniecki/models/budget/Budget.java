package pl.konradboniecki.models.budget;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "thisNameSuxInHibernate5")
    @GenericGenerator(name = "thisNameSuxInHibernate5", strategy = "increment")
    @Column(name = "budget_id")
    private Long id;

    @Column(name = "family_id")
    private Long familyId;
    @Column(name = "max_jars")
    private Long maxJars;

    public Budget(){
        setMaxJars(6L);
    }
}


