package com.fsm.Soutenances.repository;



import com.fsm.Soutenances.model.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
    Enseignant findByEmail(String email);
}