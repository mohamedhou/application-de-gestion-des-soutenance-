package com.fsm.Soutenances.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Salle {
    @Id
    private Long id;
    private String nom;
    private String numero;
    private int capacite;
}