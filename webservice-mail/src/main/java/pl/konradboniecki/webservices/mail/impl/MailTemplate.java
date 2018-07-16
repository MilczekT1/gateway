package pl.konradboniecki.webservices.mail.impl;

import java.io.Serializable;

/**
  * MailTemplate provides names for mail html templates.
 **/

class MailTemplate implements Serializable {
    static final String INVITE_FAMILY_OLD_USER = "familyInvitationForExistingUser";
    static final String INVITE_FAMILY_NEW_USER = "familyInvitationForNewUser";
    static final String CONFIRMATION_SIGN_UP = "signUpConfirmationMail";
    static final String CONFIRMATION_NEW_PASSWORD = "newPasswordConfirmationMail";
}
