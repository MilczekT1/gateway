package pl.konradboniecki.models.useractivationcode;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;

//TODO: remove this class
@Data
@NoArgsConstructor
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

    public UserActivationCode(Long accountId, String activationCode) {
        setAccountId(accountId);
        setActivationCode(activationCode);
        setApplyTime(ZonedDateTime.now());
    }
}
