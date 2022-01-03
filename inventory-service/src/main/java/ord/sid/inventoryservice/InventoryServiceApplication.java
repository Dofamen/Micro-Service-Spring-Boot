package ord.sid.inventoryservice;

import ord.sid.inventoryservice.entities.Product;
import ord.sid.inventoryservice.repository.ProductRepostory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(ProductRepostory productRepostory, RepositoryRestConfiguration configuration){
        configuration.exposeIdsFor(Product.class);
        return args -> {
            productRepostory.save(new Product(null, "Laptop", Math.random()*7000, Math.random()*12));
            productRepostory.save(new Product(null, "Smart Phone", Math.random()*7000, Math.random()*12));
            productRepostory.save(new Product(null, "Printer", Math.random()*7000, Math.random()*12));

            productRepostory.findAll().forEach(System.out::println);
        };
    }

}
