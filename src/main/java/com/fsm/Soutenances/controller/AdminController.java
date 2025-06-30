package com.fsm.Soutenances.controller;

import com.fsm.Soutenances.model.*;
import com.fsm.Soutenances.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired private SujetRepository sujetRepository;
    @Autowired private EtudiantRepository etudiantRepository;
    @Autowired private EnseignantRepository enseignantRepository;
    @Autowired private SalleRepository salleRepository; // Correction du nom
    @Autowired private SoutenanceRepository soutenanceRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("sujets", sujetRepository.findAll());
        model.addAttribute("etudiants", etudiantRepository.findAll());
        model.addAttribute("enseignants", enseignantRepository.findAll());
        model.addAttribute("salles", salleRepository.findAll()); // Correction du nom
        model.addAttribute("soutenances", soutenanceRepository.findAll());
        return "admin/dashboard";
    }

    @PostMapping("/valider-sujet/{id}")
    public String validerSujet(@PathVariable Long id) { // Correction du nom de méthode
        Sujet sujet = sujetRepository.findById(id).orElseThrow();
        sujet.setValide(true);
        sujetRepository.save(sujet);
        return "redirect:/admin/dashboard";
    }

    // Ajout de la méthode de planification
    @PostMapping("/planifier-soutenance")
    public String planifierSoutenance(@ModelAttribute Soutenance soutenance) {
        soutenanceRepository.save(soutenance);
        return "redirect:/admin/dashboard";
    }
}