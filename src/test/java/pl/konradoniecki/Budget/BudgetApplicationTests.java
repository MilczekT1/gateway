package pl.konradoniecki.Budget;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.konradoniecki.Budget.models.AccountTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AccountTest.class})
public class BudgetApplicationTests {

	@Test
	public void contextLoads() {
	}

}
