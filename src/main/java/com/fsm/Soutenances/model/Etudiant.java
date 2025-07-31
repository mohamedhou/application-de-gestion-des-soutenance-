package com.fsm.Soutenances.model;

<<<<<<< HEAD

import jakarta.persistence.Entity;
import java.util.Date;

@Entity
public class Etudiant extends Personne {
    private Date dateInscription;
    private String massar;

    // Getters/setters
    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getMassar() {
        return massar;
    }

    public void setMassar(String massar) {
        this.massar = massar;
    }
=======
import jakarta.persistence.*;

@Entity
public class Etudiant extends Personne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // On garde niveau en String, c'est suffisant
    private String niveau;
    private String massar; 
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filiere_id") // La clé étrangère vers la table Filiere
    private Filiere filiere;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sujet_id", unique = true)
    private Sujet sujet;

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }
    public String getMassar() { return massar; }
    public void setMassar(String massar) { this.massar = massar; }
    public Sujet getSujet() { return sujet; }
    public void setSujet(Sujet sujet) { this.sujet = sujet; }
    
    // *** GETTERS & SETTERS CORRIGÉS POUR FILIERE ***
    public Filiere getFiliere() { return filiere; }
    public void setFiliere(Filiere filiere) { this.filiere = filiere; }
>>>>>>> develop
}