package pl.konradboniecki.models.family;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import pl.konradboniecki.models.Family;
import pl.konradboniecki.models.frontendforms.FamilyCreationForm;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(Lifecycle.PER_CLASS)
public class FamilyTest {

    private String invitationToFamilyJsonFileAsString;
    private String invitationToFamilyWithEmptyPropsAsString;

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

        File invitationToFamilyWithoutProps = new File(getClass().getClassLoader().getResource("InvitationToFamilyWithEmptyProperties.json").getFile());
        assertTrue(invitationToFamilyWithoutProps.exists());
        result = new StringBuilder();
        try (Scanner scanner = new Scanner(invitationToFamilyWithoutProps)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
        }
        invitationToFamilyWithEmptyPropsAsString = result.toString();
    }

    @Test
    void testConstructors(){
        Family family1 = new Family();
        assertAll(
                () -> assertNull(family1.getId()),
                () -> assertNull(family1.getOwnerId()),
                () -> assertNull(family1.getBudgetId()),
                () -> assertNull(family1.getTitle()),
                () -> assertEquals(5, family1.getMaxMembers().intValue())
        );

        FamilyCreationForm familyForm = new FamilyCreationForm();
        familyForm.setTitle("test_title");

        Family family2 = new Family(familyForm);

        assertAll(
                () -> assertNull(family2.getId()),
                () -> assertNull(family2.getOwnerId()),
                () -> assertNull(family2.getBudgetId()),
                () -> assertEquals("test_title", family2.getTitle()),
                () -> assertEquals(5, family2.getMaxMembers().intValue())
        );
        Family family3 = new Family(familyForm, 100L);
        assertAll(
                () -> assertNull(family3.getId()),
                () -> assertEquals(100L, family3.getOwnerId().intValue()),
                () -> assertNull(family3.getBudgetId()),
                () -> assertEquals("test_title", family3.getTitle()),
                () -> assertEquals(5, family3.getMaxMembers().intValue())
        );
    }

    @Test
    public void testCreateFamilyFromJsonResponse() throws IOException {
        ObjectNode json = new ObjectMapper().readValue(invitationToFamilyJsonFileAsString, ObjectNode.class);
        Family family = new Family("Family", json);
        assertAll(
                () -> assertEquals(10, family.getId().longValue()),
                () -> assertEquals(12, family.getOwnerId().longValue()),
                () -> assertEquals( 0, family.getBudgetId().longValue()),
                () -> assertEquals("test_title", family.getTitle()),
                () -> assertEquals(5, family.getMaxMembers().intValue())
        );

        json = new ObjectMapper().readValue(invitationToFamilyWithEmptyPropsAsString, ObjectNode.class);
        Family emptyFamily = new Family("Family", json);
        assertAll(
                () -> assertNull(emptyFamily.getId()),
                () -> assertNull(emptyFamily.getOwnerId()),
                () -> assertNull(emptyFamily.getBudgetId()),
                () -> assertNull(emptyFamily.getTitle()),
                () -> assertEquals(5, emptyFamily.getMaxMembers().intValue())
        );

        ObjectNode finalJson = json;
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new Family("", finalJson)),
                () -> assertThrows(NullPointerException.class, () -> new Family(null, finalJson))

        );
    }
}