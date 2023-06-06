package org.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.challenge"})
public class ArithmeticCalculator {
    public static void main(String[] args) {
        SpringApplication.run(ArithmeticCalculator.class, args);
    }
}