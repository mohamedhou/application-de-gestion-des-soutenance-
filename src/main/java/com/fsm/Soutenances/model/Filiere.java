package com.fsm.Soutenances.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Filiere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nom;
    private String description;

    // La classe Filiere N'EST PAS propriétaire de la relation. L'autre côté (Enseignant) s'en occupe.
    @ManyToMany(mappedBy = "filieresEnseignees")
    private List<Enseignant> enseignants = new ArrayList<>();
    
    // Unmapped by, One Filiere has many students
    @OneToMany(mappedBy = "filiere")
    private List<Etudiant> etudiants = new ArrayList<>();

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Enseignant> getEnseignants() {
		return enseignants;
	}

	public void setEnseignants(List<Enseignant> enseignants) {
		this.enseignants = enseignants;
	}

	public List<Etudiant> getEtudiants() {
		return etudiants;
	}

	public void setEtudiants(List<Etudiant> etudiants) {
		this.etudiants = etudiants;
	}
    
    // GETTERS ET SETTERS kamlin...
}