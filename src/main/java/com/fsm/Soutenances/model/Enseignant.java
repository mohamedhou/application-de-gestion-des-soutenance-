package com.fsm.Soutenances.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;	

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Enseignant extends Personne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String specialite;
  
    private String disponibilites;;

    // Getters et setters pour l'ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getSpecialite() {
		return specialite;
	}

	public void setSpecialite(String specialite) {
		this.specialite = specialite;
	}

	public String getDisponibilites() {
		return disponibilites;
	}

	public void setDisponibilites(String disponibilites) {
		this.disponibilites = disponibilites;
	}
    
    // ... autres getters/setters ...
}