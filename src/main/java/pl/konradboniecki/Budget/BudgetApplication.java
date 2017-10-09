package pl.konradboniecki.Budget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BudgetApplication {
	private static final Logger log = LoggerFactory.getLogger(BudgetApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BudgetApplication.class, args);
	}
	
	/*
	@Bean
	public CommandLineRunner demo(FamilyRepository repository) {
		return (args) -> {
			// save a couple of customers
			repository.save(new Family("Kowalscy"));
			repository.save(new Family("Nowakowie"));
			log.info("-----------------------------------------------------");
			// fetch all customers
			log.info("Famillies found with findAllAccounts():");
			log.info("-------------------------------");
			for (Family family : repository.findAllAccounts()) {
				log.info(family.toString());
			}
			log.info("");
			
			// fetch an individual customer by Title
			Family family = repository.findById(1L);
			log.info("Family found with findOne(1L):");
			log.info("--------------------------------");
			log.info(family.toString());
			log.info("");
			
			
			
			// fetch customers by last name
			log.info("Customer found with findByLastName('Bauer'):");
			log.info("--------------------------------------------");
			for (Family bauer : repository.findByLastName("Bauer")) {
				log.info(bauer.toString());
			}
			log.info("");
		};
	}*/
}
