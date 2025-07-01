package com.fsm.Soutenances.repository;



import com.fsm.Soutenances.model.Enseignant;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
    Optional<Enseignant> findByEmail(String email);
}