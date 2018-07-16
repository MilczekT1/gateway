package pl.konradboniecki.models.frontendforms;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

class NewPasswordFormTest {

    @Test
    void testInit() {
        NewPasswordForm newPasswordForm = new NewPasswordForm();
        assertAll(
                () -> assertNull(newPasswordForm.getEmail()),
                () -> assertNull(newPasswordForm.getPassword()),
                () -> assertNull(newPasswordForm.getRepeatedPassword())
        );
    }

    @Test
    void testIsRepeatedPasswordTheSame(){
        NewPasswordForm newPasswordForm = new NewPasswordForm();

        newPasswordForm.setPassword("pass");
        newPasswordForm.setRepeatedPassword("pass");
        assertTrue(newPasswordForm.isRepeatedPasswordTheSame());

        newPasswordForm.setPassword("something different");
        assertFalse(newPasswordForm.isRepeatedPasswordTheSame());

        newPasswordForm.setPassword(null);
        assertThrows(NullPointerException.class, newPasswordForm::isRepeatedPasswordTheSame);

        newPasswordForm.setPassword("pass");
        newPasswordForm.setRepeatedPassword(null);
        assertThrows(NullPointerException.class, newPasswordForm::isRepeatedPasswordTheSame);
    }

    @Test
    @Disabled
    void testEmailRegex(){}

    @Test
    @Disabled
    void testPasswordRegex(){}

    @Test
    @Disabled
    void testRepeatedPasswordRegex(){}
}