package com.fsm.Soutenances.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fsm.Soutenances.model.Enseignant;
import com.fsm.Soutenances.service.CreneauIndisponibleService;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    @Autowired
    private CreneauIndisponibleService creneauService;
    
    // La durée de la soutenance (ex: 90 minutes)
    private static final long DUREE_SOUTENANCE_MINUTES = 90; 

    @GetMapping("/enseignants-disponibles")
    public List<Enseignant> getEnseignantsDisponibles(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut) {
        
        LocalDateTime fin = debut.plusMinutes(DUREE_SOUTENANCE_MINUTES);
        return creneauService.findAvailableJuryMembers(debut, fin);
    }
}