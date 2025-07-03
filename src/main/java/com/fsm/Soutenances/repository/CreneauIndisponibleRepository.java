package com.fsm.Soutenances.repository;

import com.fsm.Soutenances.model.CreneauIndisponible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CreneauIndisponibleRepository extends JpaRepository<CreneauIndisponible, Long> {
    
    // Yjib les indisponibilités dyal wa7ed l'enseignant
    List<CreneauIndisponible> findByEnseignantId(Long enseignantId);
    
    // Hada mohim l l'admin: Yjib ga3 les enseignants li 3AMR lihom l'we9t f wa7ed l'créneau mo7addad
    @Query("SELECT DISTINCT c.enseignant.id FROM CreneauIndisponible c WHERE (c.debut < :finSoutenance AND c.fin > :debutSoutenance)")
    List<Long> findIndisponibleEnseignantIdsForPeriod(LocalDateTime debutSoutenance, LocalDateTime finSoutenance);
    
    @Query("SELECT c FROM CreneauIndisponible c WHERE c.enseignant.id = :enseignantId " +
            "AND (c.debut < :fin AND c.fin > :debut)")
     List<CreneauIndisponible> findByEnseignantIdAndPeriodOverlap(
         @Param("enseignantId") Long enseignantId,
         @Param("debut") LocalDateTime debut,
         @Param("fin") LocalDateTime fin);
    
}