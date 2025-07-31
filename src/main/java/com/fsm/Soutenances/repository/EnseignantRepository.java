package com.fsm.Soutenances.repository;

<<<<<<< HEAD


import com.fsm.Soutenances.model.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
    Enseignant findByEmail(String email);
=======
import com.fsm.Soutenances.model.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {

    Optional<Enseignant> findByEmail(String email);

    // ===========> CORRECTION MAJEURE: VERSION WA3RA <===========
    // Katjib l'enseignant + les filières dyalo + les sujets dyalo b requete wa7da
    @Query("SELECT e FROM Enseignant e LEFT JOIN FETCH e.filieresEnseignees LEFT JOIN FETCH e.sujets WHERE e.id = :id")
    Optional<Enseignant> findByIdWithFilieresAndSujets(@Param("id") Long id);  
    // Nkhalliw dyal filières bo7dha l cas l'akhor
    @Query("SELECT e FROM Enseignant e LEFT JOIN FETCH e.filieresEnseignees WHERE e.id = :id")
    Optional<Enseignant> findByIdWithFilieres(@Param("id") Long id);
    
 // Dans EnseignantRepository.java

 // N zidoha bach dima n7ello l mochkil f blassa wa7da
 @Query("SELECT en FROM Enseignant en") // Simple select est suffisant car la spécialité n'est pas LAZY
 List<Enseignant> findAllEnseignants();
 
 @Query("SELECT e FROM Enseignant e JOIN e.filieresEnseignees f WHERE f.id = :filiereId")
 List<Enseignant> findByFiliereId(@Param("filiereId") Long filiereId);
    
>>>>>>> develop
}