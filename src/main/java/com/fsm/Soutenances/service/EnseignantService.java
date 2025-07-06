package com.fsm.Soutenances.service;

import com.fsm.Soutenances.model.Enseignant;
import com.fsm.Soutenances.model.Soutenance;
import com.fsm.Soutenances.model.Sujet;
import com.fsm.Soutenances.repository.EnseignantRepository;
import com.fsm.Soutenances.repository.SoutenanceRepository;
import com.fsm.Soutenances.repository.SujetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional // C'est une bonne pratique de rendre le service transactionnel
public class EnseignantService {

    private final SujetRepository sujetRepository;
    private final SoutenanceRepository soutenanceRepository;
    private final EnseignantRepository enseignantRepository;

    @Autowired
    public EnseignantService(
            SujetRepository sujetRepository,
            SoutenanceRepository soutenanceRepository,
            EnseignantRepository enseignantRepository) {
        this.sujetRepository = sujetRepository;
        this.soutenanceRepository = soutenanceRepository;
        this.enseignantRepository = enseignantRepository;
    }

    public Enseignant findById(Long id) {
        return enseignantRepository.findByIdWithFilieres(id)
            .orElseThrow(() -> new RuntimeException("Enseignant introuvable"));
    }
    
    // ===> ZID HADI L'METHODE JDIDA <===
    public Enseignant findByIdWithFilieresAndSujets(Long id) {
        return enseignantRepository.findByIdWithFilieresAndSujets(id).orElse(null);
    }
    
    // ========== Méthodes de recherche ==========

   

    public Enseignant findByIdWithFilieres(Long id) {
        return enseignantRepository.findByIdWithFilieres(id).orElse(null);
    }
    
    public Soutenance getSoutenanceDetails(Long id) {
        return soutenanceRepository.findById(id).orElse(null);
    }

    // ========== Logique métier pour les Sujets ==========

    public List<Sujet> getSujetsEncadres(Enseignant enseignant) {
        // La meilleure façon est d'utiliser la relation directe dans l'objet Enseignant
        // mais cela nécessite de s'assurer que la collection est chargée (LAZY).
        // findByEncadrant est aussi une bonne option.
        return sujetRepository.findByEncadrant(enseignant);
    }
    
    // ========== Logique métier pour les Soutenances ==========

    public List<Soutenance> getSoutenancesJure(Enseignant enseignant) {
        return soutenanceRepository.findByJuryEnseignants(enseignant);
    }

    public List<Soutenance> getSoutenancesEncadrees(Enseignant enseignant) {
        // Utilise la méthode du repository qui cherche par ID d'encadrant.
        return soutenanceRepository.findByEncadrantId(enseignant.getId());
    }

    // ========== Gestion des Notes ==========

    public void soumettreNote(Long soutenanceId, double note, Enseignant enseignant) {
        Soutenance soutenance = soutenanceRepository.findById(soutenanceId)
            .orElseThrow(() -> new IllegalArgumentException("Soutenance introuvable"));
            
        if (soutenance.getJuryEnseignants() != null && 
            soutenance.getJuryEnseignants().contains(enseignant)) {
            
            soutenance.getNotes().put(enseignant, note);
            
            // Calculer la moyenne
            double moyenne = soutenance.getNotes().values().stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            
            soutenance.setNoteFinale(moyenne);
            soutenanceRepository.save(soutenance);	
        } else {
            throw new IllegalArgumentException("L'enseignant ne fait pas partie du jury de cette soutenance.");
        }
    }
    
    // ========== Gestion des Rapports ==========

    public List<Soutenance> getRapportsEtudiants(Enseignant enseignant) {
        return soutenanceRepository.findByEncadrantIdAndRapportIsNotNull(enseignant.getId());
    }

}