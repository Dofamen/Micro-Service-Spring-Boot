package org.sid.billingservice;

import org.sid.billingservice.entities.Bill;
import org.sid.billingservice.entities.ProductItem;
import org.sid.billingservice.feign.CustomerRestClient;
import org.sid.billingservice.feign.ProductRestClient;
import org.sid.billingservice.model.Customer;
import org.sid.billingservice.model.Product;
import org.sid.billingservice.repository.BillRepository;
import org.sid.billingservice.repository.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.PagedModel;

import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }


    @Bean
    CommandLineRunner runner(BillRepository billRepository,
                             ProductItemRepository productItemRepository, CustomerRestClient customerRestClient,
                             ProductRestClient productRestClient) {
        return args -> {
            Customer customer = customerRestClient.getCutomerById(1L);
            Bill bill = billRepository.save(new Bill(null, customer.getId(), customer, new Date(), null));
            PagedModel<Product> products = productRestClient.allProducts(0, 100);

            products.forEach(product -> {
                ProductItem item = new ProductItem();
                item.setPrice(product.getPrice());
                item.setProductId(product.getId());
                item.setQty((long) new Random().nextInt(100) + 1);
                item.setBill(bill);

                productItemRepository.save(item);

            });
        };
    }
}
