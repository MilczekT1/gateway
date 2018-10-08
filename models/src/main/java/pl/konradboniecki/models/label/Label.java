package pl.konradboniecki.models.label;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "thisNameSuxInHibernate5")
    @GenericGenerator(name = "thisNameSuxInHibernate5", strategy = "increment")
    @Column(name = "label_id")
    private Long id;
    @Column(name = "expense_id")
    private Long expenseId;
    @Column(name = "label")
    private String label;
}
