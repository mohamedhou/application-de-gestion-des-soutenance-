package com.fsm.Soutenances.dto;

import java.time.LocalDateTime;

// DTO pour communiquer avec FullCalendar
public class EventDTO {

    private Long id;         // Pour identifier l'événement (ID du CreneauIndisponible)
    private String title;      // Description de l'événement (ex: "Indisponible", "Réunion")
    private LocalDateTime start;  // Début de l'événement
    private LocalDateTime end;    // Fin de l'événement

    // Constructeur vide (important pour la désérialisation JSON)
    public EventDTO() {
    }

    // Constructeur pour faciliter la création à partir d'un CreneauIndisponible
    public EventDTO(Long id, String title, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
    }

    // Getters et Setters (très importants pour la sérialisation/désérialisation)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}