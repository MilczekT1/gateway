package pl.konradboniecki.models.useractivationcode;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "activation_code")
public class UserActivationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "thisNameSuxInHibernate5")
    @GenericGenerator(name = "thisNameSuxInHibernate5", strategy = "increment")
    @Column(name="activation_code_id")
    private Long id;
    @Column(name="account_id")
    private Long accountId;
    @Column(name = "activation_code")
    private String activationCode;
    @Column(name="creation_time")
    private ZonedDateTime applyTime;

    public UserActivationCode() {
        ;
    }

    public UserActivationCode(Long id, Long accountId, String activationCode, ZonedDateTime applyTime) {
        this.id = id;
        this.accountId = accountId;
        this.activationCode = activationCode;
        this.applyTime = applyTime;
    }

    public UserActivationCode(Long accountId, String activationCode) {
        this.accountId = accountId;
        this.activationCode = activationCode;
        this.applyTime = ZonedDateTime.now();
    }
}
