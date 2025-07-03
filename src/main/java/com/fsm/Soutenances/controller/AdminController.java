package com.fsm.Soutenances.controller;

import com.fsm.Soutenances.model.*;
import com.fsm.Soutenances.repository.*;
import com.fsm.Soutenances.service.SujetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final SujetRepository sujetRepository;
    private final EtudiantRepository etudiantRepository;
    private final EnseignantRepository enseignantRepository;
    private final SalleRepository salleRepository;
    private final SoutenanceRepository soutenanceRepository;
    private final SujetService sujetService;

    @Autowired
    public AdminController(SujetRepository sujetRepository, EtudiantRepository etudiantRepository,
                           EnseignantRepository enseignantRepository, SalleRepository salleRepository,
                           SoutenanceRepository soutenanceRepository, SujetService sujetService) {
        this.sujetRepository = sujetRepository;
        this.etudiantRepository = etudiantRepository;
        this.enseignantRepository = enseignantRepository;
        this.salleRepository = salleRepository;
        this.soutenanceRepository = soutenanceRepository;
        this.sujetService = sujetService;
    }

    // ========== DASHBOARD ==========
    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("sujets", sujetRepository.findAll());
        return "admin/dashboard";
    }

    // ========== GESTION DES SUJETS ==========
    @GetMapping("/sujets/valider/{id}")
    public String validerSujet(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            sujetService.validerSujet(id);
            redirectAttributes.addFlashAttribute("successMessage", "Sujet validé avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur: " + e.getMessage());
        }
        return "redirect:/admin/dashboard"; 
    }
    
    // ========== GESTION DES SOUTENANCES ==========
    @GetMapping("/soutenances")
    public String listeSoutenances(Model model) {
        model.addAttribute("soutenances", soutenanceRepository.findAllByOrderByDateAsc());
        return "admin/soutenances/liste";
    }

    @GetMapping("/soutenances/planifier")
    public String planifierSoutenanceForm(Model model) {
        model.addAttribute("soutenance", new Soutenance());
        model.addAttribute("etudiants", etudiantRepository.findWhereSujetIsNotNullAndSoutenanceIsNull());
        model.addAttribute("salles", salleRepository.findAll());
        return "admin/soutenances/planifier";
    }

    @PostMapping("/soutenances/planifier")
    public String planifierSoutenance(@ModelAttribute("soutenance") Soutenance soutenance,
            @RequestParam("dateSoutenance") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("heureSoutenance") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heure,
            @RequestParam(value = "juryEnseignants", required = false) List<Long> juryIds,
            RedirectAttributes redirectAttributes) {
        // ... L'code l's7i7 dyal l'post (nkhalliwh kifma howa)...
        soutenance.setDate(LocalDateTime.of(date, heure));
        Etudiant etudiant = etudiantRepository.findById(soutenance.getEtudiant().getId())
                .orElseThrow(() -> new IllegalArgumentException("Étudiant invalide"));
        soutenance.setSujet(etudiant.getSujet());
        if (juryIds != null) {
            soutenance.setJuryEnseignants(enseignantRepository.findAllById(juryIds));
        }
        soutenanceRepository.save(soutenance);
        redirectAttributes.addFlashAttribute("successMessage", "Soutenance planifiée !");
        return "redirect:/admin/soutenances";
    }

    // ========== GESTION DES SALLES ==========
    @GetMapping("/salles")
    public String gérerSalles(Model model) {
        model.addAttribute("salles", salleRepository.findAll());
        model.addAttribute("salle", new Salle()); 
        return "admin/salles/liste";
    }

    @PostMapping("/salles/ajouter")
    public String ajouterSalle(@ModelAttribute Salle salle, RedirectAttributes redirectAttributes) {
        salleRepository.save(salle);
        redirectAttributes.addFlashAttribute("successMessage", "Salle ajoutée.");
        return "redirect:/admin/salles";
    }
    
    // ========== GESTION DES UTILISATEURS ==========
    @GetMapping("/utilisateurs")
    public String listeUtilisateurs(Model model) {
        model.addAttribute("etudiants", etudiantRepository.findAll());
        model.addAttribute("enseignants", enseignantRepository.findAll());
        return "admin/utilisateurs/liste";
    }
    
    // On peut ajouter la logique de modification/suppression ici
}