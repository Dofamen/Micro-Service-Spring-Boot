package com.example.demo.controller;

import com.example.demo.entities.Compte;
import com.example.demo.entities.Operation;
import com.example.demo.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Autowired
    public BankService bankService;

    @GetMapping(path = "/compte/{id}")
    public Compte getCompte(@PathVariable Long id) {
        return bankService.afficherCompte(id);
    }

    @PostMapping(path = "/viremant/{montant}/{client1}/{client2}")
    public Operation virement(@PathVariable double montant, @PathVariable Long client1, @PathVariable Long client2) throws Exception {
        return bankService.virement(montant, client1, client2);
    }

}
