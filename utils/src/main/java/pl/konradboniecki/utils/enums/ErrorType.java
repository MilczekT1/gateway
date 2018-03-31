package pl.konradboniecki.utils.enums;


import lombok.Getter;
import lombok.Setter;

// This enum is designed to apply error name for thymeleaf. Use this enum to avoid typos.
public enum ErrorType {
    
    INVALID_ACTIVATION_LINK("invalidActivationLink"),
    INVALID_INVITATION_LINK("invalidInvitationLink"),
    PROCESSING_EXCEPTION("processingException"),
    NOT_ENOUGH_SPACE_IN_FAMILY("notEnoughSpaceInFamily"),
    ALREADY_IN_FAMILY("alreadyInFamily");

    @Getter
    @Setter
    private String modelAttrName;
    
    ErrorType(String modelAttrName){
        setModelAttrName(modelAttrName);
    }
}
