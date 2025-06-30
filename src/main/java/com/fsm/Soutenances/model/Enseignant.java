package com.fsm.Soutenances.model;

import jakarta.persistence.Entity;

@Entity
public class Enseignant extends Personne {
    private String specialite;

    // Getters/setters
    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
}