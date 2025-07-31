package com.fsm.Soutenances.controller;

import com.fsm.Soutenances.dto.EnseignantDTO;
import com.fsm.Soutenances.model.Etudiant; // ==> ZID HADA L'IMPORT
import com.fsm.Soutenances.repository.EtudiantRepository; // ==> ZID HADA L'IMPORT
import com.fsm.Soutenances.service.CreneauIndisponibleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    @Autowired
    private CreneauIndisponibleService creneauService;

    // Injecter EtudiantRepository pour récupérer la filière
    @Autowired
    private EtudiantRepository etudiantRepository; 
    
    private static final long DUREE_SOUTENANCE_MINUTES = 90; 

    // ===========> HADI HIYA L'VERSION L'FINALE W L'S7I7A <===========
    
    @GetMapping("/enseignants-disponibles")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getEnseignantsDisponibles(
        @RequestParam("debut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
        @RequestParam("etudiantId") Long etudiantId) {
        
        try {
            // 1. On récupère l'objet étudiant complet pour connaître sa filière
            Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new IllegalArgumentException("Étudiant introuvable avec l'ID: " + etudiantId));

            // 2. On vérifie que l'étudiant a bien une filière
            if (etudiant.getFiliere() == null) {
                // On retourne une liste vide si l'étudiant n'a pas de filière définie
                return ResponseEntity.ok(Collections.emptyList());
            }
            Long filiereId = etudiant.getFiliere().getId();

            // 3. On calcule l'heure de fin
            LocalDateTime fin = debut.plusMinutes(DUREE_SOUTENANCE_MINUTES);
            
            // 4. On appelle le service avec les 3 arguments nécessaires
            List<EnseignantDTO> disponibles = creneauService.findAvailableJuryMembers(debut, fin, filiereId);
            
            // 5. On renvoie la liste
            return ResponseEntity.ok(disponibles);
        
        } catch (Exception e) {
            e.printStackTrace();
            // On renvoie un message d'erreur clair
            return ResponseEntity.status(500).body("Erreur interne du serveur: " + e.getMessage());
        }
    }
}