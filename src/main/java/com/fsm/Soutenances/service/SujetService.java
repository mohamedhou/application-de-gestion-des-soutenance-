package com.fsm.Soutenances.service;

import com.fsm.Soutenances.model.Sujet;
import java.util.List;
import java.util.Optional; // C'est une bonne pratique de retourner un Optional

public interface SujetService {
    
    Sujet save(Sujet sujet);
    
    List<Sujet> findAll();
    
    Sujet findById(Long id); // On retourne l'objet directement ou null
    
    void validerSujet(Long sujetId);
}