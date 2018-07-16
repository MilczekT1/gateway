package pl.konradboniecki.models.familyinvitation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(Lifecycle.PER_CLASS)
public class FamilyInvitationTest {

    @Test
    void test(){
        String testMail = "test@mail.com";
        String testInvitCode = "abcdef";
        Long testFamilyId = 10L;
        ZonedDateTime zonedDateTimeBefore = ZonedDateTime.now();

        FamilyInvitation familyInvitation1 = new FamilyInvitation();
        assertAll(
                () -> assertNull(familyInvitation1.getId()),
                () -> assertNull(familyInvitation1.getFamilyId()),
                () -> assertNull(familyInvitation1.getEmail()),
                () -> assertNull(familyInvitation1.getInvitationCode()),
                () -> assertNull(familyInvitation1.getApplyTime()),
                () -> assertNull(familyInvitation1.getRegisteredStatus())
        );
        FamilyInvitation familyInvitation2 = new FamilyInvitation(testMail, testFamilyId);
        assertAll(
                () -> assertNull(familyInvitation2.getId()),
                () -> assertEquals(testFamilyId, familyInvitation2.getFamilyId()),
                () -> assertEquals(testMail,familyInvitation2.getEmail()),
                () -> assertNull(familyInvitation2.getInvitationCode()),
                () -> assertNotNull(familyInvitation2.getApplyTime()),
                () -> assertTrue(familyInvitation2.getApplyTime().isAfter(zonedDateTimeBefore)),
                () -> assertNull(familyInvitation2.getRegisteredStatus())
        );
        FamilyInvitation familyInvitation3 = new FamilyInvitation(testMail,testFamilyId,testInvitCode,false);
        assertAll(
                () -> assertNull(familyInvitation3.getId()),
                () -> assertEquals(testFamilyId, familyInvitation3.getFamilyId()),
                () -> assertEquals(testMail, familyInvitation3.getEmail()),
                () -> assertEquals(testInvitCode, familyInvitation3.getInvitationCode()),
                () -> assertNotNull(familyInvitation3.getApplyTime()),
                () -> assertTrue(familyInvitation3.getApplyTime().isAfter(zonedDateTimeBefore)),
                () -> assertFalse(familyInvitation3.getRegisteredStatus())
        );
    }
}
