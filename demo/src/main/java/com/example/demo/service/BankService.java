package com.example.demo.service;

import com.example.demo.entities.Compte;
import com.example.demo.entities.Operation;

public interface BankService {

    public Compte afficherCompte(Long id);
    public Operation virement(double montant, Long idCompte1, Long idCompte2) throws Exception;

}
