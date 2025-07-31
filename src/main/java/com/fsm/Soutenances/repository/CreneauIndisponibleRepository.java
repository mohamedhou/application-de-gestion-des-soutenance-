package com.fsm.Soutenances.repository;

import com.fsm.Soutenances.model.CreneauIndisponible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

// Hada interface, fih ghir les signatures dyal les queries
public interface CreneauIndisponibleRepository extends JpaRepository<CreneauIndisponible, Long> {
    
    // Pour l'API dyal l'enseignant
    List<CreneauIndisponible> findByEnseignantId(Long enseignantId);
    
    // Pour l'API dyal l'admin
    @Query("SELECT DISTINCT c.enseignant.id FROM CreneauIndisponible c WHERE c.enseignant IS NOT NULL AND c.debut < :finSoutenance AND c.fin > :debutSoutenance")
    List<Long> findIndisponibleEnseignantIdsForPeriod(
            @Param("debutSoutenance") LocalDateTime debutSoutenance, 
            @Param("finSoutenance") LocalDateTime finSoutenance);
}