package com.fsm.Soutenances.repository;

import com.fsm.Soutenances.model.Enseignant;
import com.fsm.Soutenances.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    
    // Pour l'authentification
    Optional<Etudiant> findByEmail(String email);
    
    // ==========> HADI HIYA L'MÉTHODE LI KANET NA9SAK <==========
    // Hada kayjib lik ghir les étudiants li 3endhom sujet w MAZAL ma 3ndhom soutenance
    @Query("SELECT e FROM Etudiant e WHERE e.sujet IS NOT NULL AND NOT EXISTS (SELECT s FROM Soutenance s WHERE s.etudiant = e)")
    List<Etudiant> findWhereSujetIsNotNullAndSoutenanceIsNull();

    @Query("SELECT e FROM Enseignant e LEFT JOIN FETCH e.filieresEnseignees WHERE e.id = :id")
    Optional<Enseignant> findByIdWithFilieres(@Param("id") Long id);


    @Query("SELECT e FROM Etudiant e JOIN FETCH e.filiere")
    List<Etudiant> findAllWithFiliere();
}

