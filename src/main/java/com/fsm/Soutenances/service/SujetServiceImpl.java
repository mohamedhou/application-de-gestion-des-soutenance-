<<<<<<< HEAD
// SujetServiceImpl.java
package com.fsm.Soutenances.service;

import com.fsm.Soutenances.model.Sujet;
import com.fsm.Soutenances.repository.*;
import com.fsm.Soutenances.service.SujetService;
=======
package com.fsm.Soutenances.service;

import com.fsm.Soutenances.model.Sujet;
import com.fsm.Soutenances.repository.SujetRepository;
// SujetService import is optional if they are in the same package
// import com.fsm.Soutenances.service.SujetService;
import org.springframework.beans.factory.annotation.Autowired; // Utilisons l'injection Autowired
>>>>>>> develop
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SujetServiceImpl implements SujetService {
<<<<<<< HEAD
    private final SujetRepository sujetRepository;

=======

    // Injection du repository
    private final SujetRepository sujetRepository;

    // Constructeur pour l'injection de dépendances (best practice)
    @Autowired
>>>>>>> develop
    public SujetServiceImpl(SujetRepository sujetRepository) {
        this.sujetRepository = sujetRepository;
    }

<<<<<<< HEAD
    @Override
    public Sujet save(Sujet sujet) {
        return sujetRepository.save(sujet);
    }

    @Override
    public List<Sujet> findAll() {
        return sujetRepository.findAll();
    }
=======
    /**
     * Enregistre un nouveau sujet ou met à jour un sujet existant.
     */
    @Override
    public Sujet save(Sujet sujet) {
        // CORRECTION: On retourne l'objet sauvé
        return sujetRepository.save(sujet);
    }

    /**
     * Récupère la liste de tous les sujets.
     */
    @Override
    public List<Sujet> findAll() {
        // CORRECTION: Il manquait l'implémentation de cette méthode
        return sujetRepository.findAll();
    }
    
    /**
     * Récupère un sujet par son ID.
     */
     @Override
     public Sujet findById(Long id) {
        // CORRECTION: Il manquait l'implémentation de cette méthode
        return sujetRepository.findById(id).orElse(null); // Retourne null si non trouvé
     }

    /**
     * Valide un sujet en mettant son attribut 'valide' à true.
     */
    @Override
    public void validerSujet(Long sujetId) {
        // Ton implémentation était déjà correcte, on la garde.
        Sujet sujet = sujetRepository.findById(sujetId)
            .orElseThrow(() -> new RuntimeException("Sujet non trouvé pour l'id: " + sujetId));
        
        sujet.setValide(true);
        sujetRepository.save(sujet);
    }
>>>>>>> develop
}