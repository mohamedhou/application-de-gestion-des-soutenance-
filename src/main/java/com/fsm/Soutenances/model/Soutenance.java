package com.fsm.Soutenances.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Soutenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;
    private Date heure;
    private float noteFinal;

    @ManyToOne
    private Enseignant encadrant;

    @ManyToOne
    private Salle salle;

    @ManyToOne
    private Jury jury;

    @ManyToMany
    private List<Enseignant> jurys;

    @ManyToOne
    private Etudiant etudiant;

    @ManyToOne
    private Sujet sujet;

    public Long getId() {
        return id;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public void setSujet(Sujet sujet) {
        this.sujet = sujet;
    }
}
