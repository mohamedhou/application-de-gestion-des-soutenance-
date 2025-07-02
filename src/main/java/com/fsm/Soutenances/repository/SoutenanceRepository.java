package com.fsm.Soutenances.repository;

import com.fsm.Soutenances.model.Enseignant;
import com.fsm.Soutenances.model.Etudiant;
import com.fsm.Soutenances.model.Soutenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SoutenanceRepository extends JpaRepository<Soutenance, Long> {
	
    List<Soutenance> findByJuryEnseignants(Enseignant enseignant);
    List<Soutenance> findBySujetEncadrantAndRapportIsNotNull(Enseignant enseignant);
    
    @Query("SELECT s FROM Soutenance s JOIN s.juryEnseignants j WHERE j.id = :enseignantId")
    List<Soutenance> findByJuryEnseignantId(@Param("enseignantId") Long enseignantId);
    
    // Nouvelle méthode pour trouver la soutenance d'un étudiant
    Optional<Soutenance> findByEtudiant(Etudiant etudiant);
    
    // Alternative si vous préférez par ID
    @Query("SELECT s FROM Soutenance s WHERE s.etudiant.id = :etudiantId")
    Optional<Soutenance> findByEtudiantId(@Param("etudiantId") Long etudiantId);
    
    @Query("SELECT s FROM Soutenance s JOIN FETCH s.sujet suj JOIN FETCH suj.encadrant WHERE suj.encadrant = :enseignant")
    List<Soutenance> findBySujetEncadrant(@Param("enseignant") Enseignant enseignant);
    @Query("SELECT s FROM Soutenance s " +
            "JOIN FETCH s.sujet sujet " +
            "JOIN FETCH sujet.encadrant encadrant " +
            "WHERE encadrant.id = :enseignantId")
     List<Soutenance> findByEnseignantId(@Param("enseignantId") Long enseignantId);
    
}