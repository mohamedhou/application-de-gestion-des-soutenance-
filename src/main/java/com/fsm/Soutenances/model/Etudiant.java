package com.fsm.Soutenances.model;

import jakarta.persistence.*; 

@Entity
public class Etudiant extends Personne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String filiere;
    private String niveau;
    private String massar; 
    
    // HADA HOWA L'CODE L'CORRIGÉ L'S7I7
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sujet_id", unique = true, referencedColumnName = "id")
    private Sujet sujet;

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFiliere() { return filiere; }
    public void setFiliere(String filiere) { this.filiere = filiere; }
    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }
    public String getMassar() { return massar; }
    public void setMassar(String massar) { this.massar = massar; }
    public Sujet getSujet() { return sujet; }
    public void setSujet(Sujet sujet) { this.sujet = sujet; }
}