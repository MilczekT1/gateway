package pl.konradboniecki.Budget.models.family;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

@Data
@Table(name = "Family")
@Entity
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "thisNameSucksInHibernate5")
    @GenericGenerator(name = "thisNameSucksInHibernate5", strategy = "increment")
    private Long id;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "max_members")
    private Integer maxMembers;
    @Column(name = "max_jars")
    private Integer maxJars;
    @Column(name = "email_notifications_enabled")
    private boolean emailNotificationsEnabled;
    @Column(name = "phone_notifications_enabled")
    private boolean phoneNotificationsEnabled;
    
    public Family() {
    }
    
    public Family(String title) {
        this.title = title;
        this.maxMembers = 3;
    }
}
