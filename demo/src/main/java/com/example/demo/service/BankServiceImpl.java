package com.example.demo.service;

import com.example.demo.entities.Compte;
import com.example.demo.entities.Operation;
import com.example.demo.entities.OperationType;
import com.example.demo.feign.ClientRest;
import com.example.demo.respositories.CompteRepository;
import com.example.demo.respositories.OperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class BankServiceImpl implements BankService{

    private ClientRest clientRest;
    private OperationRepository operationRepository;
    private CompteRepository compteRepository;

    public BankServiceImpl(ClientRest clientRest, OperationRepository operationRepository, CompteRepository compteRepository) {
        this.clientRest = clientRest;
        this.operationRepository = operationRepository;
        this.compteRepository = compteRepository;
    }

    @Override
    public Compte afficherCompte(Long id) {
        return  compteRepository.findById(id).get();
    }

    @Override
    public Operation virement(double montant, Long idCompte1, Long idCompte2) throws Exception {
        Compte compte1 = compteRepository.findById(idCompte1).get();
        Compte compte2 = compteRepository.findById(idCompte2).get();

        if (!compte1.isIsactivated() || compte1.getSolde() < montant){
            throw new Exception("Erreur: montant insufisant !");
        }else
        {
            compte1.setSolde(compte1.getSolde()-montant);
            compte2.setSolde(compte2.getSolde()+montant);
            compteRepository.save(compte1);
            compteRepository.save(compte2);
            operationRepository.save(new Operation(1L,new Date(),montant, OperationType.CREDIT, compte2));
            return operationRepository.save(new Operation(1L,new Date(),montant, OperationType.DEBIT, compte1));
        }

    }
}
