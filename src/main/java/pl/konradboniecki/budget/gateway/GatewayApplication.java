package pl.konradboniecki.budget.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.konradboniecki.chassis.configuration.HttpLoggingAutoConfiguration;
import pl.konradboniecki.chassis.configuration.WebServerAutoConfiguration;

@SpringBootApplication(exclude = {WebServerAutoConfiguration.class, HttpLoggingAutoConfiguration.class})
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
