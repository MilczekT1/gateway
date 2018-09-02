package pl.konradboniecki.models.jar;

import lombok.Getter;
import lombok.Setter;

public enum JarStatus {
    COMPLETED("COMPLETED"), IN_PROGRESS("IN PROGRESS");

    JarStatus(String status){
        setStatus(status);
    }

    @Getter @Setter
    private String status;
}
