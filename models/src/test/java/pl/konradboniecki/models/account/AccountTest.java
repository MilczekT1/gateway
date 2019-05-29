package pl.konradboniecki.models.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import pl.konradboniecki.models.Account;
import pl.konradboniecki.models.frontendforms.AccountForm;
import pl.konradboniecki.utils.TokenGenerator;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static pl.konradboniecki.utils.enums.UserType.USER;

@TestInstance(Lifecycle.PER_CLASS)
public class AccountTest {

    private String accountActivationJsonFileAsString;
    private String accountActivationWithEmptyPropsAsString;

    @BeforeAll
    void setup() throws IOException {
        File accountActivationJsonFile = new File(getClass().getClassLoader().getResource("AccountActivation.json").getFile());
        assertTrue(accountActivationJsonFile.exists());

        StringBuilder result = new StringBuilder();
        try (Scanner scanner = new Scanner(accountActivationJsonFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
        }
        accountActivationJsonFileAsString = result.toString();

        File accountActivationWithEmptyPropsJsonFile = new File(getClass().getClassLoader().getResource("AccountActivationWithEmptyProperties.json").getFile());
        assertTrue(accountActivationWithEmptyPropsJsonFile.exists());
        result = new StringBuilder();
        try (Scanner scanner = new Scanner(accountActivationWithEmptyPropsJsonFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
        }
        accountActivationWithEmptyPropsAsString = result.toString();
    }

    @Test
    void testIfHasFamily() {
        Account acc = new Account();
        assertFalse(acc.hasFamily());
        acc.setFamilyId(1L);
        assertTrue(acc.hasFamily());
    }

    @Test
    void testHashPasswordInSetter(){
        String password = "P@ssw0rd";
        String hashedPassword = new TokenGenerator().hashPassword(password);
        Account acc = new Account();
        acc.setPassword(password);
        assertEquals(acc.getPassword(), hashedPassword);
    }

    @Test
    void testLowerCaseEmail(){
        Account acc = new Account();
        acc.setEmail("TEST@MAIL.com");
        assertEquals("test@mail.com", acc.getEmail());
    }

    @Test
    void testCreateAccountFromJsonResponse() throws IOException {
        ObjectNode json = new ObjectMapper().readValue(accountActivationJsonFileAsString, ObjectNode.class);
        Account account = new Account("Account", json);
        assertAll(
                () -> assertEquals(2, account.getId().longValue()),
                () -> assertEquals(0, account.getFamilyId().longValue()),
                () -> assertEquals("kon", account.getFirstName()),
                () -> assertEquals( "bon", account.getLastName()),
                () -> assertEquals("test@mail.com", account.getEmail()),
                () -> assertEquals("123123123", account.getPhoneNumber()),
                () -> assertEquals(USER.getRoleName(), account.getRole()),
                () -> assertFalse(account.isEnabled())
        );

        json = new ObjectMapper().readValue(accountActivationWithEmptyPropsAsString, ObjectNode.class);
        Account emptyAcc = new Account("Account", json);
        assertAll(
                () -> assertNull(emptyAcc.getId()),
                () -> assertNull(emptyAcc.getFamilyId()),
                () -> assertNull(emptyAcc.getFirstName()),
                () -> assertNull( emptyAcc.getLastName()),
                () -> assertNull(emptyAcc.getEmail()),
                () -> assertNull(emptyAcc.getPhoneNumber()),
                () -> assertNull(emptyAcc.getRole()),
                () -> assertFalse(emptyAcc.isEnabled())
        );

        ObjectNode finalJson = json;
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new Account("", finalJson)),
                () -> assertThrows(NullPointerException.class, () -> new Account(null, finalJson))

        );
    }

    @Test
    void testInitFromAccountForm(){
        AccountForm accForm = new AccountForm();
        accForm.setPassword("password");
        accForm.setEmail("TEST@mail.com");
        accForm.setRepeatedPassword("password");
        accForm.setFirstName("kon");
        accForm.setLastName("bon");
        accForm.setPhoneNumber("123123123");

        Account acc = new Account(accForm);
        String hashedPassword = new TokenGenerator().hashPassword(accForm.getPassword());
        assertAll(
                () -> assertEquals(accForm.getEmail().toLowerCase(), acc.getEmail()),
                () -> assertEquals(accForm.getFirstName(), acc.getFirstName()),
                () -> assertEquals(hashedPassword, acc.getPassword()),
                () -> assertEquals(accForm.getPhoneNumber(), acc.getPhoneNumber()),
                () -> assertNotNull(acc.getRegisterDate()),
                () -> assertEquals(USER.getRoleName(), acc.getRole()),
                () -> assertFalse(acc.isEnabled()),
                () -> assertNull(acc.getId()),
                () -> assertNull(acc.getFamilyId())
        );
    }
}