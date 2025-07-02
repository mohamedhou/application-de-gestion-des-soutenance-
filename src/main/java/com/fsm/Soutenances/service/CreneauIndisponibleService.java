package com.fsm.Soutenances.service;

import com.fsm.Soutenances.dto.EventDTO;
import com.fsm.Soutenances.model.CreneauIndisponible;
import com.fsm.Soutenances.model.Enseignant;
import com.fsm.Soutenances.repository.CreneauIndisponibleRepository;
import com.fsm.Soutenances.repository.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional // C'est une bonne pratique pour les services qui modifient la DB
public class CreneauIndisponibleService {

    @Autowired
    private CreneauIndisponibleRepository creneauRepo;

    @Autowired
    private EnseignantRepository enseignantRepo;

    /**
     * Convertit une liste de CreneauIndisponible en une liste de EventDTO pour FullCalendar.
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
     * Crée et sauvegarde un nouveau CreneauIndisponible à partir des données d'un EventDTO.
     */
    public EventDTO addCreneauFromEvent(Long enseignantId, EventDTO eventDto) { // <-- L'type de retour ghaywelli EventDTO
        // On récupère l'enseignant
        Enseignant enseignant = enseignantRepo.findById(enseignantId)
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé pour l'ID: " + enseignantId));
        
        CreneauIndisponible creneau = new CreneauIndisponible();
        creneau.setEnseignant(enseignant);
        creneau.setDebut(eventDto.getStart());
        creneau.setFin(eventDto.getEnd());
        // L'mochkil tani ghaykoun hna, 7ellito f l'point jjay
        creneau.setDescription(eventDto.getTitle());

        // N'enregistriw l'entité f la base de données
        CreneauIndisponible savedCreneau = creneauRepo.save(creneau);

        // Daba, nconvertiw l'entité li tsawvgardat l DTO 3ad nrej3oha
        return new EventDTO(
            savedCreneau.getId(),
            savedCreneau.getDescription(),
            savedCreneau.getDebut(),
            savedCreneau.getFin()
        );
    }

    /**
     * Supprime un créneau d'indisponibilité par son ID.
     */
    public void deleteCreneau(Long creneauId) {
        if (!creneauRepo.existsById(creneauId)) {
            throw new RuntimeException("Créneau d'indisponibilité non trouvé pour l'ID: " + creneauId);
        }
        creneauRepo.deleteById(creneauId);
    }
}