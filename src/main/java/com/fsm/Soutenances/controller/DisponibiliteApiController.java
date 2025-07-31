package com.fsm.Soutenances.controller;

import com.fsm.Soutenances.dto.EventDTO;
import com.fsm.Soutenances.service.CreneauIndisponibleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // <-- IMPORTANT: @RestController, pas @Controller
@RequestMapping("/api/enseignant") // On commence par un chemin de base pour l'API
public class DisponibiliteApiController {

    @Autowired
    private CreneauIndisponibleService creneauService;

    /**
     * Endpoint pour récupérer toutes les indisponibilités d'un enseignant
     * Appelée par FullCalendar pour afficher les événements.
     * URL: GET /api/enseignant/{enseignantId}/disponibilites
     */
    @GetMapping("/{enseignantId}/disponibilites")
    public List<EventDTO> getIndisponibilites(@PathVariable Long enseignantId) {
        return creneauService.getCreneauxAsEvents(enseignantId);
    }

    /**
     * Endpoint pour ajouter une nouvelle indisponibilité.
     * Appelée quand l'enseignant sélectionne un créneau sur le calendrier.
     * URL: POST /api/enseignant/{enseignantId}/disponibilites
     */
    @PostMapping("/{enseignantId}/disponibilites")
    public ResponseEntity<EventDTO> addIndisponibilite(@PathVariable Long enseignantId, @RequestBody EventDTO eventDto) {
        try {
            EventDTO savedEvent = creneauService.addCreneauFromEvent(enseignantId, eventDto);
            return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Endpoint pour supprimer une indisponibilité.
     * Appelée quand l'enseignant clique sur un événement existant pour le supprimer.
     * URL: DELETE /api/enseignant/disponibilites/{creneauId}
     */
    @DeleteMapping("/disponibilites/{creneauId}")
    public ResponseEntity<Void> deleteIndisponibilite(@PathVariable Long creneauId) {
        try {
            creneauService.deleteCreneau(creneauId);
            return ResponseEntity.ok().build(); // HTTP 200 OK
        } catch (Exception e) {
            // Gérer le cas où le créneau n'est pas trouvé
            return ResponseEntity.notFound().build(); // HTTP 404 Not Found
        }
    }
}