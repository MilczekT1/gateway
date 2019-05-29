package pl.konradboniecki.models.budget;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import pl.konradboniecki.models.Budget;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(Lifecycle.PER_CLASS)
public class BudgetTest {

    @Test
    public void testContructor(){
        Budget budget = new Budget();
        assertAll(
                () -> assertEquals(6L, budget.getMaxJars().longValue()),
                () -> assertNull(budget.getId()),
                () -> assertNull(budget.getFamilyId())
        );
    }
}
