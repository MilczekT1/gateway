package pl.konradboniecki.utils.enums;

import lombok.Getter;
import lombok.Setter;

public enum UserType {
    
    ADMIN("ADMIN"), SUBSCRIBER("SUBSCRIBER"), USER("USER");

    @Getter
    @Setter
    private String role;
    
    UserType(String role){
        setRole(role);
    }
}
