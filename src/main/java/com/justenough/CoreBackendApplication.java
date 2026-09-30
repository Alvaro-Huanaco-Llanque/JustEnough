package com.justenough;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoreBackendApplication {

    public static void main(String[] args) {
        // Carga el archivo .env antes de que arranque Spring Boot
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(CoreBackendApplication.class, args);
    }
}
