package pl.konradboniecki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BudgetApplication {
	private static final Logger log = LoggerFactory.getLogger(BudgetApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BudgetApplication.class, args);
	}
}
