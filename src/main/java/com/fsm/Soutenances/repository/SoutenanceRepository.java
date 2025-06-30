package com.fsm.Soutenances.repository;

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

}