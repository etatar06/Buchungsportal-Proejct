package de.saascom.probeaufgabe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableJpaRepositories("de.saascom.probeaufgabe.repository")
@EntityScan("de.saascom.probeaufgabe.models")
public class Application {

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() throws Exception {
        System.out.println("Application is ready!");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

