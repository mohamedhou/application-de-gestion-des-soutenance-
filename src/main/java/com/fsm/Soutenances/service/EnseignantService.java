package com.fsm.Soutenances.service;

import com.fsm.Soutenances.model.Enseignant;
import com.fsm.Soutenances.model.Soutenance;
import com.fsm.Soutenances.model.Sujet;
import com.fsm.Soutenances.repository.EnseignantRepository;
import com.fsm.Soutenances.repository.SoutenanceRepository;
import com.fsm.Soutenances.repository.SujetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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

 // Dans EnseignantService.java
    public Enseignant findById(Long id) {
        return enseignantRepository.findById(id).orElse(null);
    }

    // Autres méthodes inchangées...
    public void ajouterSujet(Sujet sujet, Enseignant enseignant) {
        sujet.setEncadrant(enseignant);
        sujetRepository.save(sujet);
    }

    public List<Sujet> getSujetsEncadres(Enseignant enseignant) {
        return sujetRepository.findByEncadrant(enseignant);
    }

    public List<Soutenance> getSoutenancesJure(Enseignant enseignant) {
        return soutenanceRepository.findByJuryEnseignants(enseignant);
    }

    public List<Soutenance> getSoutenancesEncadrees(Enseignant enseignant) {
        return soutenanceRepository.findBySujetEncadrant(enseignant);
    }

    public Soutenance getSoutenanceDetails(Long id) {
        return soutenanceRepository.findById(id).orElse(null);
    }

    public void soumettreNote(Long soutenanceId, double note, Enseignant enseignant) {
        Optional<Soutenance> optionalSoutenance = soutenanceRepository.findById(soutenanceId);
        if (optionalSoutenance.isPresent()) {
            Soutenance soutenance = optionalSoutenance.get();
            
            // Vérifier que l'enseignant fait partie du jury
            if (soutenance.getJuryEnseignants() != null && 
                soutenance.getJuryEnseignants().contains(enseignant)) {
                
                // Enregistrer la note individuelle
                soutenance.getNotes().put(enseignant, note);
                
                // Calculer la moyenne des notes
                double moyenne = soutenance.getNotes().values().stream()
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(0.0);
                
                soutenance.setNoteFinale(moyenne);
                soutenanceRepository.save(soutenance);
            } else {
                throw new IllegalArgumentException("L'enseignant ne fait pas partie du jury de cette soutenance");
            }
        } else {
            throw new IllegalArgumentException("Soutenance introuvable");
        }
    }
    
    public List<Soutenance> getRapportsEtudiants(Enseignant enseignant) {
        return soutenanceRepository.findBySujetEncadrantAndRapportIsNotNull(enseignant);
    }

    public String getDisponibilites(Enseignant enseignant) {
        return enseignant.getDisponibilites();
    }

    public void enregistrerDisponibilites(Enseignant enseignant, String disponibilites) {
        enseignant.setDisponibilites(disponibilites);
        enseignantRepository.save(enseignant);
    }


    public void declarerDisponibilites(Enseignant enseignant, String disponibilites) {
        enseignant.setDisponibilites(disponibilites);
        enseignantRepository.save(enseignant);
    }
}