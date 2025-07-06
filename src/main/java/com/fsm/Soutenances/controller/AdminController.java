package com.fsm.Soutenances.controller;

import com.fsm.Soutenances.model.*;
import com.fsm.Soutenances.repository.*;
import com.fsm.Soutenances.service.SujetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // === INJECTION DES DÉPENDANCES (le point le plus important) ===
    private final SujetRepository sujetRepository;
    private final EtudiantRepository etudiantRepository;
    private final EnseignantRepository enseignantRepository;
    private final SalleRepository salleRepository;
    private final SoutenanceRepository soutenanceRepository;
    private final SujetService sujetService;
    private final FiliereRepository filiereRepository; // ==> L'ATTRIBUT LI KAN NA9ESS <==

    @Autowired
    public AdminController(SujetRepository sujetRepository, EtudiantRepository etudiantRepository,
                           EnseignantRepository enseignantRepository, SalleRepository salleRepository,
                           SoutenanceRepository soutenanceRepository, SujetService sujetService,
                           FiliereRepository filiereRepository) { // ==> ZEDNAH HNA F CONSTRUCTEUR <==
        this.sujetRepository = sujetRepository;
        this.etudiantRepository = etudiantRepository;
        this.enseignantRepository = enseignantRepository;
        this.salleRepository = salleRepository;
        this.soutenanceRepository = soutenanceRepository;
        this.sujetService = sujetService;
        this.filiereRepository = filiereRepository; // ==> W HNA F L'AFFECTATION <==
    }

    // ========== DASHBOARD ==========
    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("sujets", sujetRepository.findAll());
        model.addAttribute("totalSoutenances", soutenanceRepository.count());
        model.addAttribute("totalEtudiants", etudiantRepository.count());
        model.addAttribute("totalEnseignants", enseignantRepository.count());
        model.addAttribute("soutenances", soutenanceRepository.findAllByOrderByDateAsc());
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
    
    // ... La partie gestion des soutenances reste inchangée ...
    
    @GetMapping("/soutenances")
    public String listeSoutenances(Model model) {
        model.addAttribute("soutenances", soutenanceRepository.findAllByOrderByDateAsc());
        return "admin/soutenances/liste";
    }

    @GetMapping("/soutenances/planifier")
    public String planifierSoutenanceForm(Model model) {
        if (!model.containsAttribute("soutenance")) {
            model.addAttribute("soutenance", new Soutenance());
        }
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
        try {
            soutenance.setDate(LocalDateTime.of(date, heure));

            Etudiant etudiantComplet = etudiantRepository.findById(soutenance.getEtudiant().getId())
                .orElseThrow(() -> new IllegalStateException("Étudiant non trouvé"));
            
            soutenance.setEtudiant(etudiantComplet);
            soutenance.setSujet(etudiantComplet.getSujet());

            if (juryIds != null) {
                soutenance.setJuryEnseignants(enseignantRepository.findAllById(juryIds));
            }

            soutenanceRepository.save(soutenance);
            redirectAttributes.addFlashAttribute("successMessage", "Soutenance planifiée avec succès!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur: " + e.getMessage());
        }
        return "redirect:/admin/soutenances";
    }
    // --->>RECHERCHE Soutenance  <<---
    
    @GetMapping("/soutenances/rechercher")
    public String rechercherSoutenances(
            @RequestParam(required = false) Long enseignantId,
            @RequestParam(required = false) Long etudiantId,
            @RequestParam(required = false) String date,
            Model model) {

        if (enseignantId != null || etudiantId != null || (date != null && !date.isEmpty())) {
            List<Soutenance> resultats;
            if (enseignantId != null) {
                resultats = soutenanceRepository.findByEncadrantId(enseignantId);
            } else if (etudiantId != null) {
                resultats = soutenanceRepository.findByEtudiantId(etudiantId);
            } else { // C'est forcément par date
                try {
                    LocalDateTime start = LocalDate.parse(date).atStartOfDay();
                    LocalDateTime end = start.plusDays(1);
                    resultats = soutenanceRepository.findByDateBetweenRange(start, end);
                } catch (Exception e) {
                    resultats = List.of(); // Si la date est invalide
                }
            }
            model.addAttribute("soutenances", resultats);
        }
        // On envoie toujours les listes pour les dropdowns du formulaire
        model.addAttribute("enseignants", enseignantRepository.findAll());
        model.addAttribute("etudiants", etudiantRepository.findAll());
        return "admin/soutenances/rechercher";
    }
    
    @GetMapping("/soutenances/modifier/{id}")
    public String modifierSoutenanceForm(@PathVariable Long id, Model model) {
        Soutenance soutenance = soutenanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Soutenance non trouvée: " + id));
        
        model.addAttribute("soutenance", soutenance);
        model.addAttribute("salles", salleRepository.findAll());
        // On envoie tous les enseignants pour le choix du jury
        model.addAttribute("enseignants", enseignantRepository.findAll());
        
        return "admin/soutenances/modifier"; // -> Le template que tu as créé
    }

    // --- 5. Soumettre le formulaire de Modification ---
    @PostMapping("/soutenances/modifier/{id}")
    public String modifierSoutenance(
            @PathVariable Long id,
            // On ne peut pas binder tout l'objet car il est complexe
            @RequestParam("dateSoutenance") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("heureSoutenance") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heure,
            @RequestParam("salle") Long salleId,
            @RequestParam(value = "juryEnseignants", required = false) List<Long> juryIds,
            RedirectAttributes redirectAttributes) {

        // On charge la soutenance existante depuis la DB
        Soutenance soutenanceAjour = soutenanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Soutenance non trouvée: " + id));
        
        // On met à jour ses propriétés
        soutenanceAjour.setDate(LocalDateTime.of(date, heure));
        
        Salle salle = salleRepository.findById(salleId)
                .orElseThrow(() -> new IllegalArgumentException("Salle non trouvée: " + salleId));
        soutenanceAjour.setSalle(salle);
        
        if (juryIds != null) {
            soutenanceAjour.setJuryEnseignants(enseignantRepository.findAllById(juryIds));
        } else {
            // Si aucune case n'est cochée, on vide la liste
            soutenanceAjour.getJuryEnseignants().clear();
        }

        soutenanceRepository.save(soutenanceAjour);
        redirectAttributes.addFlashAttribute("successMessage", "Soutenance modifiée avec succès.");
        return "redirect:/admin/soutenances";
    }
    
    // --- 6. Supprimer une soutenance ---
    @PostMapping("/soutenances/supprimer/{id}")
    public String supprimerSoutenance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            if (soutenanceRepository.existsById(id)) {
                soutenanceRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("successMessage", "Soutenance supprimée avec succès.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Soutenance non trouvée.");
            }
        } catch(Exception e) {
             redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression.");
        }
        return "redirect:/admin/soutenances";
    }
    
    
    // ========== GESTION DES SALLES (FLUX COMPLET) ==========

    @GetMapping("/salles")
    public String gererSalles(Model model) {
        // 1. On envoie la liste des salles existantes pour l'affichage
        model.addAttribute("salles", salleRepository.findAll());
        
        // 2. On s'assure qu'un objet vide 'sallePourAjout' est toujours disponible pour le formulaire
        //    (Le nom est différent pour éviter la confusion avec une variable dans une boucle)
        if (!model.containsAttribute("sallePourAjout")) {
            model.addAttribute("sallePourAjout", new Salle());
        }
        
        return "admin/salles/liste"; 
    }

    @PostMapping("/salles/ajouter")
    public String ajouterSalle(@ModelAttribute("sallePourAjout") Salle salle, // On récupère l'objet bindé
                               BindingResult bindingResult, // Pour la validation future
                               RedirectAttributes redirectAttributes) {
        
        // Validation simple: s'assurer que le nom n'est pas vide
        if (salle.getNom() == null || salle.getNom().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Le nom de la salle ne peut pas être vide.");
            redirectAttributes.addFlashAttribute("sallePourAjout", salle); // Renvoyer l'objet pour corriger
            return "redirect:/admin/salles";
        }

        try {
            salleRepository.save(salle);
            redirectAttributes.addFlashAttribute("successMessage", "Salle '" + salle.getNom() + "' ajoutée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'ajout de la salle.");
            e.printStackTrace();
        }
        return "redirect:/admin/salles";
    }

    @PostMapping("/salles/supprimer/{id}")
    public String supprimerSalle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // La logique est déjà bonne
        try {
            salleRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Salle supprimée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Impossible de supprimer. La salle est peut-être utilisée dans une soutenance.");
        }
        return "redirect:/admin/salles";
    }
    // ========== GESTION DES UTILISATEURS ==========
    @GetMapping("/utilisateurs")
    public String listeUtilisateurs(Model model) {
        // CORRECTION: findAll() est suffisant, car les relations LAZY seront gérées par la vue/transaction
        model.addAttribute("etudiants", etudiantRepository.findAll());
        model.addAttribute("enseignants", enseignantRepository.findAll());
        return "admin/utilisateurs/liste";
    }

    // --- Gérer Étudiant ---
    @GetMapping("/utilisateurs/modifier-etudiant/{id}")
    public String showFormModifierEtudiant(@PathVariable Long id, Model model) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Étudiant non trouvé: " + id));
        
        model.addAttribute("etudiant", etudiant);
        // On envoie la liste de TOUTES les filières disponibles pour le dropdown
        model.addAttribute("allFilieres", filiereRepository.findAll());
        
        return "admin/utilisateurs/modifier-etudiant";
    }

    @PostMapping("/utilisateurs/modifier-etudiant")
    public String modifierEtudiant(@ModelAttribute Etudiant etudiant, RedirectAttributes redirectAttributes) {
        Etudiant existing = etudiantRepository.findById(etudiant.getId())
                .orElseThrow(() -> new IllegalArgumentException("Étudiant non trouvé pour modification"));
        
        // Mettre à jour seulement les champs nécessaires pour éviter d'écraser le mot de passe
        existing.setNom(etudiant.getNom());
        existing.setPrenom(etudiant.getPrenom());
        existing.setEmail(etudiant.getEmail());
        existing.setCni(etudiant.getCni());
        existing.setMassar(etudiant.getMassar());
        existing.setNiveau(etudiant.getNiveau());
        existing.setFiliere(etudiant.getFiliere()); // Spring va binder l'objet Filiere

        etudiantRepository.save(existing);
        redirectAttributes.addFlashAttribute("successMessage", "Informations de l'étudiant modifiées avec succès.");
        return "redirect:/admin/utilisateurs";
    }

    // --- Gérer Enseignant ---
    @GetMapping("/utilisateurs/modifier-enseignant/{id}")
    public String showFormModifierEnseignant(@PathVariable Long id, Model model) {
        // ... (Pas de filières à gérer ici pour l'instant)
        model.addAttribute("enseignant", enseignantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Enseignant non trouvé: " + id)));
        return "admin/utilisateurs/modifier-enseignant"; 
    }

    @PostMapping("/utilisateurs/modifier-enseignant")
    public String modifierEnseignant(@ModelAttribute Enseignant enseignant, RedirectAttributes redirectAttributes) {
        Enseignant existing = enseignantRepository.findById(enseignant.getId())
                .orElseThrow(() -> new IllegalArgumentException("Enseignant non trouvé pour modification"));

        existing.setNom(enseignant.getNom());
        existing.setPrenom(enseignant.getPrenom());
        existing.setEmail(enseignant.getEmail());
        existing.setCni(enseignant.getCni());
        existing.setSpecialite(enseignant.getSpecialite());

        enseignantRepository.save(existing);
        redirectAttributes.addFlashAttribute("successMessage", "Informations de l'enseignant modifiées avec succès.");
        return "redirect:/admin/utilisateurs";
    }
}