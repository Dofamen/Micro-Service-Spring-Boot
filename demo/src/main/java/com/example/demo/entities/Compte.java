package com.example.demo.entities;

import com.example.demo.models.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;
    private double solde;
    private Date dateCreation;
    private boolean isactivated;
    private Long clientId;
    @Transient
    private Client client;

    @OneToMany
    private Collection<Operation> operations = new ArrayList<>();
}
