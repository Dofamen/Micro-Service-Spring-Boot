package com.example.demo.feign;


import com.example.demo.models.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CLIENT-SERVICE")
@Component
public interface ClientRest {
    @GetMapping("/clients/{id}")
    public Client getClientById(@PathVariable Long id);
}
