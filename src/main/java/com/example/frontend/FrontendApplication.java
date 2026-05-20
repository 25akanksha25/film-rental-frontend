package com.example.frontend;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FrontendApplication {
    private static final Logger log = LoggerFactory.getLogger(FrontendApplication.class);

    public static void main(String[] args) {

        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory(".")
                    .ignoreIfMissing()
                    .load();
            dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        } catch (Exception e) {
            log.warn("Failed to load .env file: {}", e.getMessage());
        }

        SpringApplication.run(FrontendApplication.class, args);
    }

}
