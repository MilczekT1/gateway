package pl.konradboniecki.models.jar;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import pl.konradboniecki.models.frontendforms.JarCreationForm;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Jar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "thisNameSuxInHibernate5")
    @GenericGenerator(name = "thisNameSuxInHibernate5", strategy = "increment")
    @Column(name = "jar_id")
    private Long id;

    @Column(name = "budget_id")
    private Long budgetId;
    @Column(name = "jar_name")
    private String jarName;
    @Column(name = "current_amount")
    private Long currentAmount;
    @Column(name = "capacity")
    private Long capacity;

    public Jar(JarCreationForm jarCreationForm) {
        setJarName(jarCreationForm.getJarName());
        setCurrentAmount(0L);
        setCapacity(jarCreationForm.getCapacity());
    }
}
