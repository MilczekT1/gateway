package pl.konradboniecki.Budget.services;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;


@Entity
@Data
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "thisNameSucksInHibernate5")
    @GenericGenerator(name = "thisNameSucksInHibernate5", strategy = "increment")
    private Long id;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "max_members")
    private Short maxMembers;
    @Column(name = "email_notifications_enabled")
    private boolean emailNotificationsEnabled;
    @Column(name = "phone_notifications_enabled")
    private boolean phoneNotificationsEnabled;
    
    protected Family() {
        ;
    }
    
    public Family(String title) {
        this.title = title;
        this.maxMembers = 3;
    }
}
