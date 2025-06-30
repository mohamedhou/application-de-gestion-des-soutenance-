package com.fsm.Soutenances.repository;


import com.fsm.Soutenances.model.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {
    Administrateur findByEmail(String email);
}
