package pl.konradboniecki.templates;

import lombok.extern.java.Log;

/**
  * ViewTemplate provides names for html templates.
 **/

@Log
public class ViewTemplate {
    public static final String LOGIN_PAGE = "auth/login";
    public static final String REGISTRATION_PAGE = "auth/registration";
    public static final String REGISTRATION_SUCCESS_MSG = "auth/registrationSuccessInfo";
    public static final String FAMILY_HOME_PAGE = "home/family";
    public static final String FAMILY_CREATION_PAGE = "home/familyCreationForm";
    public static final String HOME_PAGE = "home/home";
    public static final String ERROR_PAGE = "error";
    public static final String INDEX = "index";
    public static final String LOST_PASSWD_PAGE = "lostPasswordForm";
    public static final String BUDGET_HOME_PAGE = "home/budget";
    public static final String JAR_CREATION_PAGE="home/jarCreationForm";

    private ViewTemplate(){
        log.severe("this class shouldn't be instantiated!");
    }
}
