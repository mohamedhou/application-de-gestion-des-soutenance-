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

@Controller
@RequestMapping("/admin")
public class AdminController {

    // === INJECTION DES DÉPENDANCES (par constructeur) ===
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

    // ========== DASHBOARD & NAVIGATION PRINCIPALE ==========
    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("sujets", sujetRepository.findAll());
        // On peut ajouter d'autres stats ici si besoin
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

    // ========== GESTION DES SOUTENANCES (CRUD COMPLET) ==========

    // --- 1. Lister les soutenances ---
    @GetMapping("/soutenances")
    public String listeSoutenances(Model model) {
        model.addAttribute("soutenances", soutenanceRepository.findAllByOrderByDateAsc());
        return "admin/soutenances/liste";
    }

    // --- 2. Afficher le formulaire pour Planifier une soutenance ---
    @GetMapping("/soutenances/planifier")
    public String planifierSoutenanceForm(Model model) {
        model.addAttribute("soutenance", new Soutenance());
        model.addAttribute("etudiants", etudiantRepository.findWhereSujetIsNotNullAndSoutenanceIsNull());
        model.addAttribute("salles", salleRepository.findAll());
        return "admin/soutenances/planifier";
    }

    // --- 3. Soumettre le formulaire de Planification ---
    @PostMapping("/soutenances/planifier")
    public String planifierSoutenance(@ModelAttribute("soutenance") Soutenance soutenance,
            @RequestParam("dateSoutenance") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("heureSoutenance") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heure,
            @RequestParam(value = "juryEnseignants", required = false) List<Long> juryIds,
            RedirectAttributes redirectAttributes) {

        soutenance.setDate(LocalDateTime.of(date, heure));
        
        Etudiant etudiant = etudiantRepository.findById(soutenance.getEtudiant().getId())
                .orElseThrow(() -> new IllegalArgumentException("Étudiant invalide"));
        soutenance.setSujet(etudiant.getSujet());

        if (juryIds != null) {
            soutenance.setJuryEnseignants(enseignantRepository.findAllById(juryIds));
        }
        
        soutenanceRepository.save(soutenance);
        redirectAttributes.addFlashAttribute("successMessage", "Soutenance planifiée avec succès !");
        return "redirect:/admin/soutenances";
    }

    // --- 4. Afficher le formulaire pour Modifier une soutenance ---
    @GetMapping("/soutenances/modifier/{id}")
    public String modifierSoutenanceForm(@PathVariable Long id, Model model) {
        Soutenance soutenance = soutenanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Soutenance non trouvée: " + id));
        
        model.addAttribute("soutenance", soutenance);
        model.addAttribute("salles", salleRepository.findAll());
        model.addAttribute("enseignants", enseignantRepository.findAll());
        
        return "admin/soutenances/modifier";
    }

    // --- 5. Soumettre le formulaire de Modification ---
    @PostMapping("/soutenances/modifier/{id}")
    public String modifierSoutenance(@PathVariable Long id, 
                                   @RequestParam("dateSoutenance") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                   @RequestParam("heureSoutenance") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heure,
                                   @RequestParam("salle") Long salleId, // On récupère l'ID de la salle
                                   @RequestParam(value = "juryEnseignants", required = false) List<Long> juryIds,
                                   RedirectAttributes redirectAttributes) {

        Soutenance soutenanceToUpdate = soutenanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Soutenance non trouvée: " + id));

        // Mettre à jour les champs
        soutenanceToUpdate.setDate(LocalDateTime.of(date, heure));
        
        Salle salle = salleRepository.findById(salleId)
             .orElseThrow(() -> new IllegalArgumentException("Salle non trouvée: " + salleId));
        soutenanceToUpdate.setSalle(salle);

        if (juryIds != null) {
            soutenanceToUpdate.setJuryEnseignants(enseignantRepository.findAllById(juryIds));
        } else {
            // Si aucune case n'est cochée, on vide la liste du jury
            soutenanceToUpdate.getJuryEnseignants().clear();
        }

        soutenanceRepository.save(soutenanceToUpdate);
        redirectAttributes.addFlashAttribute("successMessage", "Soutenance modifiée avec succès.");

        return "redirect:/admin/soutenances";
    }

    // --- 6. Supprimer une soutenance ---
    @PostMapping("/soutenances/supprimer/{id}")
    public String supprimerSoutenance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (soutenanceRepository.existsById(id)) {
            soutenanceRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Soutenance supprimée avec succès.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur: soutenance non trouvée.");
        }
        return "redirect:/admin/soutenances";
    }


    // ========== RECHERCHE DE SOUTENANCES ==========
    @GetMapping("/soutenances/rechercher")
    public String showRecherchePage(Model model) {
        model.addAttribute("enseignants", enseignantRepository.findAll());
        model.addAttribute("etudiants", etudiantRepository.findAll());
        // On n'envoie pas de résultats au premier chargement de la page
        return "admin/soutenances/rechercher";
    }

    @PostMapping("/soutenances/rechercher")
    public String rechercherSoutenances(
            @RequestParam(required = false) Long enseignantId,
            @RequestParam(required = false) Long etudiantId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {
        
        // ... Logique de recherche (laisser comme avant) ...

        // Ajouter le code de recherche ici
        
        model.addAttribute("enseignants", enseignantRepository.findAll());
        model.addAttribute("etudiants", etudiantRepository.findAll());
        return "admin/soutenances/rechercher";
    }

    // ========== GESTION DES SALLES ==========
    @GetMapping("/salles")
    public String gérerSalles(Model model) {
        model.addAttribute("salles", salleRepository.findAll());
        model.addAttribute("nouvelleSalle", new Salle()); // <-- Smiya jdida bach matkherbe9ch m3a la liste
        return "admin/salles/liste";
    }

    @PostMapping("/salles/ajouter")
    public String ajouterSalle(@ModelAttribute("nouvelleSalle") Salle salle, RedirectAttributes redirectAttributes) {
        try {
            // N'AFFICHIO L'OBJET LI JA MBLA MA NSAUVEGARDIW
            System.out.println("Tentative d'ajout de la salle : " + salle.getNom() + ", Capacité: " + salle.getCapacite());
            
            salleRepository.save(salle);
            redirectAttributes.addFlashAttribute("successMessage", "Salle '" + salle.getNom() + "' ajoutée avec succès !");
        
        } catch (Exception e) {
        	
            System.err.println("ERREUR DÉTAILLÉE LORS DE L'AJOUT DE LA SALLE:");
            e.printStackTrace(); // HADA GHAY'AFFICHILIK L'ERREUR KAMLA F CONSOLE

            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'ajout de la salle. Consultez les logs pour les détails.");
        }
        return "redirect:/admin/salles";
    }

    @PostMapping("/salles/supprimer/{id}")
    public String supprimerSalle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Avant de supprimer, on peut vérifier si elle n'est pas utilisée
            // Cette logique peut être ajoutée dans un service plus tard
            salleRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Salle supprimée avec succès.");
        } catch (Exception e) {
            // Erreur typique : on essaie de supprimer une salle utilisée dans une soutenance (Foreign Key constraint)
            redirectAttributes.addFlashAttribute("errorMessage", "Impossible de supprimer cette salle, elle est peut-être utilisée dans une soutenance planifiée.");
        }
        return "redirect:/admin/salles";
    }
    
    // ========== GESTION DES UTILISATEURS ==========
    @GetMapping("/utilisateurs")
    public String listeUtilisateurs(Model model) {
        model.addAttribute("etudiants", etudiantRepository.findAll());
        model.addAttribute("enseignants", enseignantRepository.findAll());
        return "admin/utilisateurs/liste";
    }
    
    @GetMapping("/utilisateurs/modifier-etudiant/{id}")
    public String showFormModifierEtudiant(@PathVariable Long id, Model model) {
        model.addAttribute("etudiant", etudiantRepository.findById(id).orElseThrow());
        return "admin/utilisateurs/modifier-etudiant";
    }

    @PostMapping("/utilisateurs/modifier-etudiant")
    public String modifierEtudiant(@ModelAttribute Etudiant etudiant) {
        Etudiant existing = etudiantRepository.findById(etudiant.getId()).orElse(null);
        if (existing != null) {
            existing.setNom(etudiant.getNom());
           
            etudiantRepository.save(existing);
        }
        return "redirect:/admin/utilisateurs";
    }
    
// --- Modifier Enseignant (LES MÉTHODES QUI ÉTAIENT MANQUANTES) ---
    
    @GetMapping("/utilisateurs/modifier-enseignant/{id}")
    public String showFormModifierEnseignant(@PathVariable Long id, Model model) {
        model.addAttribute("enseignant", enseignantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Enseignant non trouvé: " + id)));
        return "admin/utilisateurs/modifier-enseignant"; // Le template que vous avez créé
    }
    
    @PostMapping("/utilisateurs/modifier-enseignant")
    public String modifierEnseignant(@ModelAttribute Enseignant enseignant, RedirectAttributes redirectAttributes) {
        Enseignant existing = enseignantRepository.findById(enseignant.getId())
                .orElseThrow(() -> new IllegalArgumentException("Enseignant non trouvé: " + enseignant.getId()));

        // Mettre à jour les champs
        existing.setNom(enseignant.getNom());
        existing.setPrenom(enseignant.getPrenom());
        existing.setEmail(enseignant.getEmail());
        existing.setCni(enseignant.getCni());
        existing.setSpecialite(enseignant.getSpecialite());

        enseignantRepository.save(existing);
        redirectAttributes.addFlashAttribute("successMessage", "Enseignant modifié avec succès.");
        return "redirect:/admin/utilisateurs";
    }
    
}