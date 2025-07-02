package com.fsm.Soutenances.controller;

import com.fsm.Soutenances.model.*;
import com.fsm.Soutenances.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // L'injection par constructeur est meilleure, mais on garde Autowired pour la simplicité
    @Autowired private SujetRepository sujetRepository;
    @Autowired private EtudiantRepository etudiantRepository;
    @Autowired private EnseignantRepository enseignantRepository;
    @Autowired private SalleRepository salleRepository;
    @Autowired private SoutenanceRepository soutenanceRepository;

    // Tableau de bord principal
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // CORRECTION: Utilisation de la méthode correcte pour le tri
        model.addAttribute("soutenances", soutenanceRepository.findAllByOrderByDateAsc());
        model.addAttribute("sujets", sujetRepository.findAll());
        model.addAttribute("etudiants", etudiantRepository.findAll());
        model.addAttribute("enseignants", enseignantRepository.findAll());
        model.addAttribute("salles", salleRepository.findAll());
        return "admin/dashboard";
    }

    // Gestion des soutenances
    @GetMapping("/soutenances")
    public String listeSoutenances(Model model) {
        // CORRECTION: Utilisation de la méthode correcte pour le tri
        model.addAttribute("soutenances", soutenanceRepository.findAllByOrderByDateAsc());
        return "admin/soutenances/liste";
    }

    @GetMapping("/soutenances/planifier")
    public String planifierSoutenanceForm(Model model) {
        model.addAttribute("soutenance", new Soutenance());
        model.addAttribute("etudiants", etudiantRepository.findAll());
        // On ne propose que les sujets validés et qui n'ont pas encore d'étudiant
        model.addAttribute("sujets", sujetRepository.findByValideTrueAndEtudiantIsNull());
        model.addAttribute("enseignants", enseignantRepository.findAll());
        model.addAttribute("salles", salleRepository.findAll());
        return "admin/soutenances/planifier";
    }

    @PostMapping("/soutenances/planifier")
    public String planifierSoutenance(@ModelAttribute Soutenance soutenance) {
        soutenanceRepository.save(soutenance);
        return "redirect:/admin/soutenances";
    }

    @GetMapping("/soutenances/modifier/{id}")
    public String modifierSoutenanceForm(@PathVariable Long id, Model model) {
        Soutenance soutenance = soutenanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Soutenance non trouvée pour l'id: " + id));
        model.addAttribute("soutenance", soutenance);
        model.addAttribute("etudiants", etudiantRepository.findAll());
        model.addAttribute("sujets", sujetRepository.findByValideTrue());
        model.addAttribute("enseignants", enseignantRepository.findAll());
        model.addAttribute("salles", salleRepository.findAll());
        return "admin/soutenances/modifier";
    }

    @PostMapping("/soutenances/modifier/{id}")
    public String modifierSoutenance(@PathVariable Long id, @ModelAttribute Soutenance soutenance) {
        // Pour s'assurer qu'on modifie bien la bonne entité
        soutenance.setId(id);
        soutenanceRepository.save(soutenance);
        return "redirect:/admin/soutenances";
    }

    @PostMapping("/soutenances/supprimer/{id}")
    public String supprimerSoutenance(@PathVariable Long id) {
        if (soutenanceRepository.existsById(id)) {
            soutenanceRepository.deleteById(id);
        }
        return "redirect:/admin/soutenances";
    }

    // Gestion des salles (Aucun changement nécessaire ici)
    @GetMapping("/salles")
    public String listeSalles(Model model) {
        model.addAttribute("salles", salleRepository.findAll());
        return "admin/salles/liste";
    }

    // Recherche des soutenances
    @GetMapping("/soutenances/rechercher")
    public String rechercherSoutenances(
            @RequestParam(required = false) Long enseignantId,
            @RequestParam(required = false) Long etudiantId,
            @RequestParam(required = false) String date, // On reçoit la date en format String "YYYY-MM-DD"
            Model model) {

        List<Soutenance> resultats;

        if (enseignantId != null) {
            resultats = soutenanceRepository.findByEncadrantId(enseignantId);
        } else if (etudiantId != null) {
            resultats = soutenanceRepository.findByEtudiantId(etudiantId);
        } else if (date != null && !date.isEmpty()) {
            // CORRECTION: Logique pour chercher dans un intervalle de temps (un jour entier)
            try {
                LocalDate localDate = LocalDate.parse(date);
                LocalDateTime startOfDay = localDate.atStartOfDay();              // Ex: 2024-07-03T00:00:00
                LocalDateTime endOfDay = localDate.plusDays(1).atStartOfDay(); // Ex: 2024-07-04T00:00:00

                // Utilisation de la méthode corrigée du repository
                resultats = soutenanceRepository.findByDateBetweenRange(startOfDay, endOfDay);
            } catch (Exception e) {
                // En cas de format de date invalide, on ne renvoie rien ou tous
                resultats = List.of(); // Renvoie une liste vide
            }
        } else {
            // CORRECTION: Utilisation de la méthode correcte pour le tri
            resultats = soutenanceRepository.findAllByOrderByDateAsc();
        }

        model.addAttribute("soutenances", resultats);
        // On envoie toujours ces listes pour les menus déroulants de la recherche
        model.addAttribute("enseignants", enseignantRepository.findAll());
        model.addAttribute("etudiants", etudiantRepository.findAll());
        return "admin/soutenances/liste"; // On peut réutiliser la vue de la liste pour afficher les résultats
    }

    // Gestion des utilisateurs (Aucun changement nécessaire ici)
    @GetMapping("/utilisateurs")
    public String listeUtilisateurs(Model model) {
        model.addAttribute("etudiants", etudiantRepository.findAll());
        model.addAttribute("enseignants", enseignantRepository.findAll());
        return "admin/utilisateurs/liste";
    }
}