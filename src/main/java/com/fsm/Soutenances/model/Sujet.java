package com.fsm.Soutenances.model;

import jakarta.persistence.*;

@Entity
public class Sujet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titre;
    private String description;
    private boolean valide;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encadrant_id")
    private Enseignant encadrant;
    
    @OneToOne(mappedBy = "sujet", fetch = FetchType.LAZY)
    private Etudiant etudiant;

    // ===========> HADI HIYA L'IḌAFA L'MOHIMMA <===========
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filiere_id", nullable = false) // Foreign key
    private Filiere filiere;

    // ========== GETTERS ET SETTERS L'KAMLİN ==========
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isValide() { return valide; }
    public void setValide(boolean valide) { this.valide = valide; }
    public Enseignant getEncadrant() { return encadrant; }
    public void setEncadrant(Enseignant encadrant) { this.encadrant = encadrant; }
    public Etudiant getEtudiant() { return etudiant; }
    public void setEtudiant(Etudiant etudiant) { this.etudiant = etudiant; }
    
    // --> Zid les getters/setters dyal la filière
    public Filiere getFiliere() { return filiere; }
    public void setFiliere(Filiere filiere) { this.filiere = filiere; }
}