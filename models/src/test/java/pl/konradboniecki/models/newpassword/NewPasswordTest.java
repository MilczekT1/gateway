package pl.konradboniecki.models.newpassword;

import org.junit.jupiter.api.Test;
import pl.konradboniecki.models.frontendforms.NewPasswordForm;
import pl.konradboniecki.utils.TokenGenerator;

import static org.junit.jupiter.api.Assertions.*;

public class NewPasswordTest {

    @Test
    void testNewPAsswordInit(){
        NewPassword newPassword = new NewPassword();
        assertAll(
                () -> assertNull(newPassword.getAccountId()),
                () -> assertNull(newPassword.getApplyTime()),
                () -> assertNull(newPassword.getNewPasswordProp()),
                () -> assertNull(newPassword.getResetCode())
        );
    }

    @Test
    void testSetNewPasswordProp(){
        NewPassword newPassword = new NewPassword();
        newPassword.setNewPasswordProp("pass");
        String hashedPassword = new TokenGenerator().hashPassword("pass");

        assertEquals(hashedPassword, newPassword.getNewPasswordProp());
    }

    @Test
    void testInitFromNewPasswordForm(){
        NewPasswordForm newPasswordForm = new NewPasswordForm();
        newPasswordForm.setEmail("test@mail.com");
        newPasswordForm.setPassword("pass");
        newPasswordForm.setRepeatedPassword("pass");

        NewPassword newPassword = new NewPassword(newPasswordForm);
        String hashedPassword = new TokenGenerator().hashPassword("pass");
        assertAll(
                () -> assertEquals(hashedPassword, newPassword.getNewPasswordProp()),
                () -> assertNotNull(newPassword.getApplyTime()),
                () -> assertNull(newPassword.getResetCode()),
                () -> assertNull(newPassword.getAccountId())
        );
    }
}
