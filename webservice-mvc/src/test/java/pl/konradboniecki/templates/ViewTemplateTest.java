package pl.konradboniecki.templates;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ViewTemplateTest {

    @Test
    void test() {
        assertNotNull(ViewTemplate.ERROR_PAGE.getFilename());
    }
}
