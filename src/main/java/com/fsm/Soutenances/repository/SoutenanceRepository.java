package com.fsm.Soutenances.repository;

<<<<<<< HEAD
import com.fsm.Soutenances.model.Soutenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SoutenanceRepository extends JpaRepository<Soutenance, Long> {
    List<Soutenance> findByEtudiantId(Long etudiantId);
    List<Soutenance> findByEncadrantId(Long encadrantId); // Ajout de cette méthode
//    List<Soutenance> findByEncadrant_Id(Long id);
    List<Soutenance> findByEncadrant_Id(Long encadrantId);

=======
import com.fsm.Soutenances.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.time.LocalDateTime; // Zid had l'import
import java.util.List;
import java.util.Optional;

public interface SoutenanceRepository extends JpaRepository<Soutenance, Long> {

    // CORRECTION 1: Hiyedna HeureAsc men smiya
    List<Soutenance> findAllByOrderByDateAsc();

    // CORRECTION 2: Sla7na la requete bach tkhdem b bidayt w nihayt l'nhar
    @Query("SELECT s FROM Soutenance s WHERE s.date >= :startOfDay AND s.date < :endOfDay ORDER BY s.date ASC")
    List<Soutenance> findByDateBetweenRange(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
    
    // Nta derti had la méthode, walakin 7sen nkhadmo b dyal l'range. Ghadi nkhalliha f commentaire.
    // @Query("SELECT s FROM Soutenance s WHERE s.date BETWEEN :start AND :end ORDER BY s.date, s.heure")
    // List<Soutenance> findByDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT s FROM Soutenance s JOIN s.sujet suj WHERE suj.encadrant.id = :enseignantId")
    List<Soutenance> findByEncadrantId(@Param("enseignantId") Long enseignantId);
    
    @Query("SELECT s FROM Soutenance s WHERE s.etudiant.id = :etudiantId")
    List<Soutenance> findByEtudiantId(Long etudiantId);
    
    @Query("SELECT s FROM Soutenance s WHERE s.salle.id = :salleId AND s.date >= :startOfDay AND s.date < :endOfDay")
    List<Soutenance> findBySalleAndDate(@Param("salleId") Long salleId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT s FROM Soutenance s JOIN s.juryEnseignants j WHERE j.id = :enseignantId")
    List<Soutenance> findByJuryEnseignantId(@Param("enseignantId") Long enseignantId);
    
    @Query("SELECT s FROM Soutenance s JOIN s.sujet suj WHERE suj.encadrant.id = :enseignantId AND s.rapport IS NOT NULL")
    List<Soutenance> findBySujetEncadrantAndRapportIsNotNull(@Param("enseignantId") Long enseignantId);
    
    @Query("SELECT s FROM Soutenance s JOIN s.sujet suj WHERE suj.encadrant.id = :enseignantId")
    List<Soutenance> findBySujetEncadrant(@Param("enseignantId") Long enseignantId);
    
    @Query("SELECT s FROM Soutenance s JOIN s.juryEnseignants j WHERE j = :enseignant")
    List<Soutenance> findByJuryEnseignants(@Param("enseignant") Enseignant enseignant);
    
    Optional<Soutenance> findByEtudiant(Etudiant etudiant);
    
 // pour un encadrant spécifique.
    @Query("SELECT s FROM Soutenance s JOIN s.sujet suj " +
           "WHERE suj.encadrant.id = :enseignantId AND s.rapport IS NOT NULL")
    List<Soutenance> findByEncadrantIdAndRapportIsNotNull(@Param("enseignantId") Long enseignantId);
    
    // ===========> ZID HADI L'METHODE L'JADIDA <===========
    // Kayjib ga3 les enseignants li kayn f la liste dyal "filieresEnseignees"
    // dyalhom l'ID dyal la filière li bghina
    @Query("SELECT e FROM Enseignant e JOIN e.filieresEnseignees f WHERE f.id = :filiereId")
    List<Enseignant> findByFiliereId(@Param("filiereId") Long filiereId);
>>>>>>> develop
}