package com.fsm.Soutenances.dto;

import java.time.LocalDateTime;

public class EventDTO {
    private Long id;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;

    // Constructeur vide + Constructeur avec paramètres
    public EventDTO() {}
    public EventDTO(Long id, String title, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
    }

    // Getters et Setters kamlin
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public LocalDateTime getStart() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; }
    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }
}