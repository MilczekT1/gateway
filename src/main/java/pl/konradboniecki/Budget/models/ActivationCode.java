package pl.konradboniecki.Budget.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "activation_codes")
@Entity
public class ActivationCode {
    
    @Id
    @Column(name = "account_id")
    private Long accountId;
    
    @Column(name = "activation_code")
    private String activationCode;
    
    public ActivationCode(String activationCode, Long accountId) {
        this.activationCode = activationCode;
        this.accountId = accountId;
    }
}
