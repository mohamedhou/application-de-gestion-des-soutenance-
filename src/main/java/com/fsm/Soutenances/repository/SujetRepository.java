package com.fsm.Soutenances.repository;

<<<<<<< HEAD
import com.fsm.Soutenances.model.Sujet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SujetRepository extends JpaRepository<Sujet, Long> {
    List<Sujet> findByValideTrue();
    List<Sujet> findByEncadrantId(Long encadrantId); // Ajout de cette méthode
=======
import com.fsm.Soutenances.model.Enseignant;
import com.fsm.Soutenances.model.Filiere; // <-- ZID HAD L'IMPORT
import com.fsm.Soutenances.model.Sujet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SujetRepository extends JpaRepository<Sujet, Long> {
    
    List<Sujet> findByEncadrant(Enseignant enseignant);
    List<Sujet> findByValideTrue();
    List<Sujet> findByValideTrueAndEtudiantIsNull();
    
    // ====> HADI HIYA LA MÉTHODE JDIDA W L'S7I7A <====
    @Query("SELECT suj FROM Sujet suj " +
           "JOIN suj.encadrant enc " +
           "JOIN enc.filieresEnseignees fe " +
           "WHERE suj.valide = true AND suj.etudiant IS NULL AND fe.id = :filiereId")
    List<Sujet> findSujetsPourFiliere(@Param("filiereId") Long filiereId);
    
    // ==> HADI HIYA L'METHODE L'MOHIMMA L L'ETUDIANT <==
    @Query("SELECT s FROM Sujet s WHERE s.valide = true AND s.etudiant IS NULL AND s.filiere.id = :filiereId")
    List<Sujet> findSujetsDisponiblesPourFiliere(@Param("filiereId") Long filiereId);
>>>>>>> develop
}