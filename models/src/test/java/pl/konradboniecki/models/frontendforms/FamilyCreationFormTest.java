package pl.konradboniecki.models.frontendforms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(Lifecycle.PER_CLASS)
public class FamilyCreationFormTest {

    @Test
    void initTest(){
        FamilyCreationForm familyCreationForm = new FamilyCreationForm();
        assertNull(familyCreationForm.getTitle());

        familyCreationForm.setTitle("test_title");
        assertEquals("test_title", familyCreationForm.getTitle());
    }
}
