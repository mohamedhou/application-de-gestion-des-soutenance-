package com.fsm.Soutenances.service;

import com.fsm.Soutenances.model.*;
import com.fsm.Soutenances.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired private EtudiantRepository etudiantRepository;
    @Autowired private EnseignantRepository enseignantRepository;
    @Autowired private AdministrateurRepository adminRepository;

    public Object authenticate(String email, String password, String role) {
        switch (role.toLowerCase()) {
            case "etudiant":
                Optional<Etudiant> etudiant = etudiantRepository.findByEmail(email);
                if (etudiant.isPresent() && etudiant.get().getPassword().equals(password)) {
                    return etudiant.get();
                }
                break;
                
            case "enseignant":
                Optional<Enseignant> enseignant = enseignantRepository.findByEmail(email);
                if (enseignant.isPresent() && enseignant.get().getPassword().equals(password)) {
                    return enseignant.get();
                }
                break;
                
            case "admin":
                Optional<Administrateur> admin = adminRepository.findByEmail(email);
                if (admin.isPresent() && admin.get().getPassword().equals(password)) {
                    return admin.get();
                }
                break;
        }
        return null;
    }
}