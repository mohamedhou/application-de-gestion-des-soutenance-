package com.fsm.Soutenances.model;

import jakarta.persistence.*;
<<<<<<< HEAD
import lombok.Data;
import java.util.Date;
import java.util.List;

@Entity
@Data
=======
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
>>>>>>> develop
public class Soutenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
<<<<<<< HEAD

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
=======
    
    private LocalDateTime date;
    
    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sujet_id")
    private Sujet sujet;
    
    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;
    
    @ManyToMany
    @JoinTable(
        name = "soutenance_jury",
        joinColumns = @JoinColumn(name = "soutenance_id"),
        inverseJoinColumns = @JoinColumn(name = "enseignant_id")
    )
    private List<Enseignant> juryEnseignants;
    
    @OneToOne
    @JoinColumn(name = "rapport_id")
    private Rapport rapport;
    
    @OneToOne
    @JoinColumn(name = "convocation_id")
    private Rapport convocation;

    // AJOUT DE L'ATTRIBUT MANQUANT
    @Column(name = "note_finale")
    private Double noteFinale;

    @ElementCollection
    @CollectionTable(name = "soutenance_notes", 
                     joinColumns = @JoinColumn(name = "soutenance_id"))
    @MapKeyJoinColumn(name = "enseignant_id")
    @Column(name = "note")
    private Map<Enseignant, Double> notes = new HashMap<>();

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public Salle getSalle() { return salle; }
    public void setSalle(Salle salle) { this.salle = salle; }
    public Sujet getSujet() { return sujet; }
    public void setSujet(Sujet sujet) { this.sujet = sujet; }
    public Etudiant getEtudiant() { return etudiant; }
    public void setEtudiant(Etudiant etudiant) { this.etudiant = etudiant; }
    public List<Enseignant> getJuryEnseignants() { return juryEnseignants; }
    public void setJuryEnseignants(List<Enseignant> juryEnseignants) { this.juryEnseignants = juryEnseignants; }
    public Rapport getRapport() { return rapport; }
    public void setRapport(Rapport rapport) { this.rapport = rapport; }
    public Rapport getConvocation() { return convocation; }
    public void setConvocation(Rapport convocation) { this.convocation = convocation; }
    public Double getNoteFinale() { return noteFinale; }
    public void setNoteFinale(Double noteFinale) { this.noteFinale = noteFinale; }
    public Map<Enseignant, Double> getNotes() { return notes; }
    public void setNotes(Map<Enseignant, Double> notes) { this.notes = notes; }
}
>>>>>>> develop
