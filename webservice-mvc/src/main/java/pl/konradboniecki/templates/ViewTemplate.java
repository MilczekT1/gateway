package pl.konradboniecki.templates;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
  * ViewTemplate provides names for html templates.
 **/

public enum ViewTemplate {

    LOGIN_PAGE("auth/login"),
    REGISTRATION_PAGE("auth/registration"),
    REGISTRATION_SUCCESS_MSG("auth/registrationSuccessInfo"),
    FAMILY_HOME_PAGE("home/family"),
    FAMILY_CREATION_PAGE("home/familyCreationForm"),
    HOME_PAGE("home/home"),
    ERROR_PAGE("error"),
    INDEX("index"),
    LOST_PASSWD_PAGE("lostPasswordForm");
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String filename;

    ViewTemplate(String fileName) {
        setFilename(fileName);
    }
}
