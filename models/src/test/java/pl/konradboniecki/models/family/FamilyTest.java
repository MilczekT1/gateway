package pl.konradboniecki.models.family;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(Lifecycle.PER_CLASS)
public class FamilyTest {

    private Family family;
    private String invitationToFamilyJsonFileAsString;
    private String familyNodeJsonFileAsString;

    @BeforeAll
    public void setup() throws IOException {
        File invitationToFamilyJsonFile = new File(getClass().getClassLoader().getResource("InvitationToFamily.json").getFile());
        assertTrue(invitationToFamilyJsonFile.exists());

        StringBuilder result = new StringBuilder();
        try (Scanner scanner = new Scanner(invitationToFamilyJsonFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
        }
        invitationToFamilyJsonFileAsString = result.toString();


        File familyNodeJsonFile = new File(getClass().getClassLoader().getResource("FamilyNode.json").getFile());
        assertTrue(familyNodeJsonFile.exists());
        result = new StringBuilder();
        try (Scanner scanner = new Scanner(invitationToFamilyJsonFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
        }
        familyNodeJsonFileAsString = result.toString();
    }

    @Test
    public void testEmptyConstructor(){
        family = new Family();
        assertAll(
                () -> assertNull(family.getId()),
                () -> assertNull(family.getOwnerId()),
                () -> assertNull(family.getBudgetId()),
                () -> assertNull(family.getTitle()),
                () -> assertNull(family.getMaxMembers()),
                () -> assertNull(family.getMaxJars()),
                () -> assertFalse(family.isEmailNotificationsEnabled()),
                () -> assertFalse(family.isPhoneNotificationsEnabled())
        );
    }

    @Disabled
    @Test
    public void testCreateFamilyFromFamilyForm(){

    }

    @Test
    public void testCreateFamilyFromJsonResponse() throws IOException {
        String jsonObjectName = null;
        ObjectNode json = new ObjectMapper().readValue(invitationToFamilyJsonFileAsString, ObjectNode.class);

        Family family = new Family("Family", json);
        assertAll(
                () -> assertEquals(10, family.getId().longValue()),
                () -> assertEquals(12, family.getOwnerId().longValue()),
                () -> assertEquals( 0, family.getBudgetId().longValue()),
                () -> assertEquals("test_title", family.getTitle()),
                () -> assertEquals(5, family.getMaxMembers().intValue()),
                () -> assertEquals(6, family.getMaxJars().intValue()),
                () -> assertFalse(family.isEmailNotificationsEnabled()),
                () -> assertTrue(family.isPhoneNotificationsEnabled())
        );

        ObjectNode finalJson = json;
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new Family("", finalJson)),
                () -> assertThrows(NullPointerException.class, () -> new Family(null, finalJson))

        );
    }
}
