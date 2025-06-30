package com.fsm.Soutenances.service;


import com.fsm.Soutenances.model.*;
import com.fsm.Soutenances.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired private EtudiantRepository etudiantRepository;
    @Autowired private EnseignantRepository enseignantRepository;
    @Autowired private AdministrateurRepository adminRepository;

    public Object authenticate(String email, String role) {
        switch (role) {
            case "etudiant":
                return etudiantRepository.findByEmail(email);
            case "enseignant":
                return enseignantRepository.findByEmail(email);
            case "admin":
                return adminRepository.findByEmail(email);
            default:
                return null;
        }
    }
}