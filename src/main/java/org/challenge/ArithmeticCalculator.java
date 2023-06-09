package org.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;


@SpringBootApplication(scanBasePackages = {"org.challenge"})
@EnableJdbcHttpSession
public class ArithmeticCalculator {
    public static void main(String[] args) {
        SpringApplication.run(ArithmeticCalculator.class, args);
    }
}