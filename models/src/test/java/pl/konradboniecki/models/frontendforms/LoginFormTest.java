package pl.konradboniecki.models.frontendforms;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class LoginFormTest {

    @Disabled
    @Test
    void test(){
        LoginForm loginForm = new LoginForm();
        //TODO: validate input against regex
        loginForm.setPassword("passwd");
        loginForm.setEmail("test@mail.com");
    }
}
