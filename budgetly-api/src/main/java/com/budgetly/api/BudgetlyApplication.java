package com.budgetly.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.budgetly")
@EntityScan(basePackages = "com.budgetly.persistence.entity")
@EnableJpaRepositories(basePackages = "com.budgetly.persistence.repository")
public class BudgetlyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudgetlyApplication.class, args);
    }
}
