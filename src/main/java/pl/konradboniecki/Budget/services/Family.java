package pl.konradboniecki.Budget.services;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;


@Entity
@ToString
@Getter
public class Family {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private Short maxSize;
    
    protected Family() {
        ;
    }
    
    public Family(String title) {
        this.title = title;
        this.maxSize = 3;
    }
}
