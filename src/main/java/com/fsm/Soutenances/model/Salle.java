package com.fsm.Soutenances.model;

import jakarta.persistence.Entity;
<<<<<<< HEAD
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
=======
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   
    private String nom;
    private int capacite; 

    // Getters et Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }
>>>>>>> develop
}