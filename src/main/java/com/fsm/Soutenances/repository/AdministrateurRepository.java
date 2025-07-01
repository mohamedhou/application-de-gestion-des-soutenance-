package com.fsm.Soutenances.repository;


import com.fsm.Soutenances.model.Administrateur;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {
    Optional<Administrateur> findByEmail(String email);
}
