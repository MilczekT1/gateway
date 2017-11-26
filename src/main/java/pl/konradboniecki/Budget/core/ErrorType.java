package pl.konradboniecki.Budget.core;


import lombok.Getter;
import lombok.Setter;
// This enum is designed to apply error name for thymeleaf. Use this enum to avoid typos.
public enum ErrorType {
    
    INVALID_ACTIVATION_LINK("invalidActivationLink");
    
    @Getter
    @Setter
    private String modelAttributeName;
    
    ErrorType(String modelAttributeName){
        setModelAttributeName(modelAttributeName);
    }
}
