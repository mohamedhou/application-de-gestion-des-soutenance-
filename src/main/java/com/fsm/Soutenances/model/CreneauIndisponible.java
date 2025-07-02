package com.fsm.Soutenances.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CreneauIndisponible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enseignant_id", nullable = false)
    private Enseignant enseignant;

    @Column(nullable = false)
    private LocalDateTime debut;

    @Column(nullable = false)
    private LocalDateTime fin;
    
    private String description; // Hada howa l'attribut li nssiti lih l'getters/setters

    // =============================================
    // GETTERS ET SETTERS LI KHASSEK TAZID
    // =============================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    public LocalDateTime getDebut() {
        return debut;
    }

    public void setDebut(LocalDateTime debut) {
        this.debut = debut;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    // >> ZID HADO B'DAT <<
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}