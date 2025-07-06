package com.fsm.Soutenances.service;

import com.fsm.Soutenances.dto.EnseignantDTO;
import com.fsm.Soutenances.dto.EventDTO;
import com.fsm.Soutenances.model.CreneauIndisponible;
import com.fsm.Soutenances.model.Enseignant;
import com.fsm.Soutenances.repository.CreneauIndisponibleRepository;
import com.fsm.Soutenances.repository.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CreneauIndisponibleService {

    @Autowired
    private CreneauIndisponibleRepository creneauRepo;

    @Autowired
    private EnseignantRepository enseignantRepo;

    // =========== 1. MÉTHODES POUR LE CALENDRIER DE L'ENSEIGNANT ===========

    /**
     * Récupère et convertit les indisponibilités en EventDTO pour FullCalendar.
     */
    public List<EventDTO> getCreneauxAsEvents(Long enseignantId) {
        return creneauRepo.findByEnseignantId(enseignantId).stream()
                .map(creneau -> new EventDTO(
                        creneau.getId(),
                        "Indisponible" + (creneau.getDescription() != null ? ": " + creneau.getDescription() : ""),
                        creneau.getDebut(),
                        creneau.getFin()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Crée une nouvelle indisponibilité à partir d'un EventDTO envoyé par le frontend.
     */
    public EventDTO addCreneauFromEvent(Long enseignantId, EventDTO eventDto) {
        Enseignant enseignant = enseignantRepo.findById(enseignantId)
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé pour l'ID: " + enseignantId));
        
        CreneauIndisponible creneau = new CreneauIndisponible();
        creneau.setEnseignant(enseignant);
        creneau.setDebut(eventDto.getStart());
        creneau.setFin(eventDto.getEnd());
        creneau.setDescription(eventDto.getTitle());

        CreneauIndisponible savedCreneau = creneauRepo.save(creneau);

        // Convertir l'entité sauvegardée en DTO pour la renvoyer
        return new EventDTO(
            savedCreneau.getId(),
            savedCreneau.getDescription(),
            savedCreneau.getDebut(),
            savedCreneau.getFin()
        );
    }

    /**
     * Supprime une indisponibilité par son ID.
     */
    public void deleteCreneau(Long creneauId) {
        if (!creneauRepo.existsById(creneauId)) {
            throw new RuntimeException("Créneau d'indisponibilité non trouvé pour l'ID: " + creneauId);
        }
        creneauRepo.deleteById(creneauId);
    }
    
    // =========== 2. MÉTHODE POUR LA PLANIFICATION PAR L'ADMIN ===========
    
    /**
     * Trouve les enseignants disponibles pour un créneau et une filière donnés.
     */
    @Transactional(readOnly = true)
    public List<EnseignantDTO> findAvailableJuryMembers(LocalDateTime debutSoutenance, LocalDateTime finSoutenance, Long filiereId) {
        
        // 1. Récupérer les IDs des enseignants occupés dans ce créneau
        List<Long> indisponibleIds = creneauRepo.findIndisponibleEnseignantIdsForPeriod(debutSoutenance, finSoutenance);

        // 2. Récupérer les enseignants de la filière spécifiée
        List<Enseignant> enseignantsDeLaFiliere = enseignantRepo.findByFiliereId(filiereId);

        // 3. Filtrer pour ne garder que les disponibles et les convertir en DTO
        return enseignantsDeLaFiliere.stream()
            .filter(enseignant -> !indisponibleIds.contains(enseignant.getId()))
            .map(enseignant -> new EnseignantDTO(enseignant.getId(), enseignant.getPrenom(), enseignant.getNom()))
            .collect(Collectors.toList());
    }
}