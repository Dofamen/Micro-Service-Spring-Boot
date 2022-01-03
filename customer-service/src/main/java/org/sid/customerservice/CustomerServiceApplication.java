package org.sid.customerservice;

import org.sid.customerservice.entities.Customer;
import org.sid.customerservice.repository.CustomerRepostory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepostory customerRepostory, RepositoryRestConfiguration configuration) {
        configuration.exposeIdsFor(Customer.class);
        return args -> {
            customerRepostory.save(new Customer(null, "Person1", "p1@gmail.com"));
            customerRepostory.save(new Customer(null, "Person2", "p2@gmail.com"));
            customerRepostory.save(new Customer(null, "Person3", "p3@gmail.com"));

            customerRepostory.findAll().forEach(System.out::println);
        };
    }

}
