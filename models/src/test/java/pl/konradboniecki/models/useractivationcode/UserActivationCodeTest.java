package pl.konradboniecki.models.useractivationcode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserActivationCodeTest {

    @Test
    void testUserActivationCodeInit(){
        Long accountId = 3L;
        String activationCode = "234regdf";
        UserActivationCode userActivationCode = new UserActivationCode(accountId, activationCode);

        assertAll(
                () -> assertNull(userActivationCode.getId()),
                () -> assertEquals(accountId, userActivationCode.getAccountId()),
                () -> assertEquals(activationCode, userActivationCode.getActivationCode()),
                () -> assertNotNull(userActivationCode.getApplyTime())
        );
    }
}
