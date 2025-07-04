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

    // --- Modification Étudiant ---
    @GetMapping("/utilisateurs/modifier-etudiant/{id}")
    public String showFormModifierEtudiant(@PathVariable Long id, Model model) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Étudiant non trouvé: " + id));
        model.addAttribute("etudiant", etudiant);
        return "admin/utilisateurs/modifier-etudiant"; // La page HTML que tu as déjà
    }

    @PostMapping("/utilisateurs/modifier-etudiant")
    public String modifierEtudiant(@ModelAttribute Etudiant etudiant, RedirectAttributes redirectAttributes) {
        // Logique sécurisée pour ne pas écraser le mot de passe
        Etudiant existingEtudiant = etudiantRepository.findById(etudiant.getId()).orElse(null);
        if (existingEtudiant != null) {
            existingEtudiant.setNom(etudiant.getNom());
            existingEtudiant.setPrenom(etudiant.getPrenom());
            existingEtudiant.setEmail(etudiant.getEmail());
            existingEtudiant.setCni(etudiant.getCni());
            existingEtudiant.setMassar(etudiant.getMassar());
            existingEtudiant.setFiliere(etudiant.getFiliere());
            existingEtudiant.setNiveau(etudiant.getNiveau());
            etudiantRepository.save(existingEtudiant);
            redirectAttributes.addFlashAttribute("successMessage", "Étudiant modifié avec succès.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la modification.");
        }
        return "redirect:/admin/utilisateurs";
    }
    
    // --- Modification Enseignant ---
    @GetMapping("/utilisateurs/modifier-enseignant/{id}")
    public String showFormModifierEnseignant(@PathVariable Long id, Model model) {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Enseignant non trouvé: " + id));
        model.addAttribute("enseignant", enseignant);
        return "admin/utilisateurs/modifier-enseignant"; // Page HTML à créer
    }

    @PostMapping("/utilisateurs/modifier-enseignant")
    public String modifierEnseignant(@ModelAttribute Enseignant enseignant, RedirectAttributes redirectAttributes) {
        // Logique sécurisée pour ne pas écraser le mot de passe
        Enseignant existingEnseignant = enseignantRepository.findById(enseignant.getId()).orElse(null);
        if(existingEnseignant != null) {
            existingEnseignant.setNom(enseignant.getNom());
            existingEnseignant.setPrenom(enseignant.getPrenom());
            existingEnseignant.setEmail(enseignant.getEmail());
            existingEnseignant.setCni(enseignant.getCni());
            existingEnseignant.setSpecialite(enseignant.getSpecialite());
            enseignantRepository.save(existingEnseignant);
            redirectAttributes.addFlashAttribute("successMessage", "Enseignant modifié avec succès.");
        } else {
             redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la modification.");
        }
        return "redirect:/admin/utilisateurs";
    }
}