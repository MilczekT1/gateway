package pl.konradboniecki.utils.enums;

import lombok.Getter;
import lombok.Setter;

/**
  * UserType provides user roles and properties.
 **/

public enum UserType {
    
    ADMIN("ADMIN"),
    SUBSCRIBER("SUBSCRIBER"),
    USER("USER");

    @Getter
    @Setter
    private String roleName;
    
    UserType(String roleName){
        setRoleName(roleName);
    }
}
