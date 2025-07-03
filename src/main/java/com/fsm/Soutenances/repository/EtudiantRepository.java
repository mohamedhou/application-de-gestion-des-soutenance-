package com.fsm.Soutenances.repository;

//EtudiantRepository.java

import com.fsm.Soutenances.model.Etudiant;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findByEmail(String email);
    
    @Query("SELECT e FROM Etudiant e WHERE e.sujet IS NOT NULL AND NOT EXISTS (SELECT s FROM Soutenance s WHERE s.etudiant = e)")
    List<Etudiant> findWhereSujetIsNotNullAndSoutenanceIsNull();
}