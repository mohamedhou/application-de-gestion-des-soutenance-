package com.fsm.Soutenances.repository;

//EtudiantRepository.java

import com.fsm.Soutenances.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Etudiant findByEmail(String email);
}