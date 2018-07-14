package pl.konradboniecki.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(Lifecycle.PER_CLASS)
public class TokenGeneratorTest {

    private TokenGenerator tokenGenerator;
    private String testString;
    private String testStringAfterSHA256;

    @BeforeEach
    void before(){
        tokenGenerator = new TokenGenerator();
    }

    @BeforeAll
    void setup(){
        testString = "test string";
        testStringAfterSHA256 = "D5579C46DFCC7F18207013E65B44E4CB4E2C2298F4AC457BA8F82743F31E930B";
    }

    @Test
    void testDefaultProperties(){
        assertAll(
                () -> assertEquals("SHA-256", tokenGenerator.getAlgorithm()),
                () -> assertEquals("UTF-8", tokenGenerator.getCharsetName())
        );
    }

    @Test
    void createUUIDTokenTest(){
        String generatedToken = tokenGenerator.createUUIDToken();
        boolean matchesPattern = Pattern.matches("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-4[a-fA-F0-9]{3}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}",
                generatedToken);
        assertTrue(matchesPattern, "Generated token is not a valid UUID token");
    }

    @Test
    public void hashStringTest() throws NoSuchMethodException {
        Method method = TokenGenerator.class.getDeclaredMethod("hashString", String.class);
        method.setAccessible(true);

        assertAll(
            () -> assertEquals(testStringAfterSHA256, tokenGenerator.hashPassword(testString), "hash is different than expected"),
            () -> assertEquals(testStringAfterSHA256, method.invoke(tokenGenerator,testString), "hash is different than expected"),
            () -> assertEquals(tokenGenerator.hashPassword(testString), method.invoke(tokenGenerator,testString))
        );
        String previousAlgorithm = tokenGenerator.getAlgorithm();
        tokenGenerator.setAlgorithm("Invalid algorithm name");
        assertNull(tokenGenerator.hashPassword(testString));

        tokenGenerator.setAlgorithm(previousAlgorithm);
        tokenGenerator.setCharsetName("Invalid charset name");
        assertNull(tokenGenerator.hashPassword(testString));
    }
}
