package com.fsm.Soutenances.repository;


import com.fsm.Soutenances.model.Enseignant;
import com.fsm.Soutenances.model.Sujet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SujetRepository extends JpaRepository<Sujet, Long> {
    List<Sujet> findByValideTrue();
    List<Sujet> findByEncadrant(Enseignant enseignant);
}