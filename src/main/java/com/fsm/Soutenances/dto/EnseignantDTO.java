package com.fsm.Soutenances.dto;

// Un simple "Plain Old Java Object" (POJO)
public class EnseignantDTO {
    
    private Long id;
    private String prenom;
    private String nom;

    // Constructeur vide (nécessaire pour certaines librairies)
    public EnseignantDTO() {
    }

    // Constructeur pour faciliter la création
    public EnseignantDTO(Long id, String prenom, String nom) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
    }

    // Getters et Setters (essentiels pour que Jackson puisse lire les valeurs)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
}