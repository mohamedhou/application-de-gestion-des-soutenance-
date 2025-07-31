package com.fsm.Soutenances.repository;


import com.fsm.Soutenances.model.Administrateur;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {
    Administrateur findByEmail(String email);
=======

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {
    Optional<Administrateur> findByEmail(String email);
    
>>>>>>> develop
}
