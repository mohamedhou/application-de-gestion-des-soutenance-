package com.fsm.Soutenances.controller;

<<<<<<< HEAD
import com.fsm.Soutenances.model.Enseignant;
import com.fsm.Soutenances.model.Sujet;
import com.fsm.Soutenances.repository.SoutenanceRepository;
import com.fsm.Soutenances.repository.SujetRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
=======
// === IMPORTS IMPORTANTS ===
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fsm.Soutenances.model.Enseignant;
import com.fsm.Soutenances.model.Filiere; // <-- N'oubliez pas cet import
import com.fsm.Soutenances.model.Rapport;
import com.fsm.Soutenances.model.Soutenance;
import com.fsm.Soutenances.model.Sujet;
import com.fsm.Soutenances.repository.FiliereRepository; // <-- Ni celui-ci
import com.fsm.Soutenances.repository.SujetRepository;
import com.fsm.Soutenances.service.EnseignantService;
>>>>>>> develop

@Controller
@RequestMapping("/enseignant")
public class EnseignantController {
<<<<<<< HEAD
    @Autowired private SujetRepository sujetRepository;
    @Autowired private SoutenanceRepository soutenanceRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Enseignant enseignant = (Enseignant) session.getAttribute("user");
        model.addAttribute("enseignant", enseignant);
        
        // Sujets proposés par l'enseignant
        model.addAttribute("sujets", sujetRepository.findByEncadrantId(enseignant.getId()));
        
        // Soutenances où l'enseignant est impliqué
        model.addAttribute("soutenances", soutenanceRepository.findByEncadrantId(enseignant.getId()));
        
        return "enseignant/dashboard";
    }

    @GetMapping("/proposer-sujet")
    public String proposerSujetForm(Model model) {
        model.addAttribute("sujet", new Sujet());
=======
    
    // --- Dépendances injectées par constructeur (meilleure pratique) ---
    private final SujetRepository sujetRepository;
    private final EnseignantService enseignantService;
    private final FiliereRepository filiereRepository; // Ajout du repository de filière

    @Autowired
    public EnseignantController(SujetRepository sujetRepository, EnseignantService enseignantService, FiliereRepository filiereRepository) {
        this.sujetRepository = sujetRepository;
        this.enseignantService = enseignantService;
        this.filiereRepository = filiereRepository;
    }
    
    // ========== DASHBOARD & PAGES PRINCIPALES ==========

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(HttpSession session, Model model) {
        Object userFromSession = session.getAttribute("user");
        if (!(userFromSession instanceof Enseignant)) {
            return "redirect:/login-enseignant";
        }
        
        // On recharge l'enseignant depuis la BD pour s'assurer que les relations LAZY sont accessibles
        Enseignant enseignant = enseignantService.findById(((Enseignant) userFromSession).getId());

        model.addAttribute("enseignant", enseignant);
        model.addAttribute("sujets", enseignantService.getSujetsEncadres(enseignant));
        model.addAttribute("soutenancesJure", enseignantService.getSoutenancesJure(enseignant));
        model.addAttribute("soutenancesEncadrees", enseignantService.getSoutenancesEncadrees(enseignant));
        
        return "enseignant/dashboard";
    }
    
   //  Gérer Indisponibilités

    @GetMapping("/calendrier-disponibilites")
    public String showCalendrierIndisponibilites(HttpSession session, Model model) {
        Object userFromSession = session.getAttribute("user");
        
        if (!(userFromSession instanceof Enseignant)) {
            return "redirect:/login-enseignant";
        }

        Enseignant enseignant = (Enseignant) userFromSession;
        model.addAttribute("enseignantId", enseignant.getId());

        return "enseignant/calendrier_disponibilites"; // ⚠ Assure-toi que ce fichier HTML existe dans templates/enseignant
    }


    // ========== GESTION DES SUJETS ==========

    @GetMapping("/proposer-sujet")
    public String proposerSujetForm(HttpSession session, Model model) {
        Object userFromSession = session.getAttribute("user");
        if (!(userFromSession instanceof Enseignant)) {
            return "redirect:/login-enseignant";
        }
        
        Enseignant enseignant = enseignantService.findById(((Enseignant) userFromSession).getId());

        model.addAttribute("sujet", new Sujet());
        model.addAttribute("filieres", enseignant.getFilieresEnseignees()); // On passe la liste des filières de l'enseignant
        
>>>>>>> develop
        return "enseignant/proposer-sujet";
    }

    @PostMapping("/proposer-sujet")
<<<<<<< HEAD
    public String proposerSujet(@ModelAttribute Sujet sujet, HttpSession session) {
        Enseignant enseignant = (Enseignant) session.getAttribute("user");
        sujet.setEncadrant(enseignant);
        sujet.setValide(false);
        sujetRepository.save(sujet);
        return "redirect:/enseignant/dashboard";
    }
=======
    public String proposerSujet(@ModelAttribute Sujet sujet, 
                              @RequestParam("filiereId") Long filiereId,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Object userFromSession = session.getAttribute("user");
        if (!(userFromSession instanceof Enseignant)) {
            return "redirect:/login-enseignant";
        }

        try {
            Enseignant enseignant = (Enseignant) userFromSession;
            sujet.setEncadrant(enseignant);

            Filiere filiere = filiereRepository.findById(filiereId)
                .orElseThrow(() -> new RuntimeException("Filière sélectionnée invalide."));
            sujet.setFiliere(filiere);
            
            sujet.setValide(false); // Un sujet est toujours non validé à sa création
            sujetRepository.save(sujet);

            redirectAttributes.addFlashAttribute("successMessage", "Sujet proposé avec succès et en attente de validation.");
        } catch(Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur est survenue lors de la proposition du sujet.");
        }
        
        return "redirect:/enseignant/dashboard";
    }

    // ========== TÉLÉCHARGEMENT DE RAPPORT ==========

    @GetMapping("/telecharger-rapport/{soutenanceId}")
    public ResponseEntity<Resource> telechargerRapport(@PathVariable Long soutenanceId, HttpSession session) {
        Object userFromSession = session.getAttribute("user");
        if (!(userFromSession instanceof Enseignant)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Enseignant enseignant = (Enseignant) userFromSession;
        
        Soutenance soutenance = enseignantService.getSoutenanceDetails(soutenanceId);
        
        // Sécurité : On vérifie que l'enseignant est bien l'encadrant du sujet de cette soutenance
        if (soutenance == null || 
            soutenance.getSujet() == null ||
            soutenance.getSujet().getEncadrant() == null ||
            !soutenance.getSujet().getEncadrant().getId().equals(enseignant.getId()) ||
            soutenance.getRapport() == null) {
            
            return ResponseEntity.notFound().build();
        }
        
        Rapport rapport = soutenance.getRapport();
        ByteArrayResource resource = new ByteArrayResource(rapport.getData());
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + rapport.getNomFichier() + "\"")
            .contentType(MediaType.parseMediaType(rapport.getTypeMime()))
            .body(resource);
    }
    
    // === CETTE PAGE N'EST PLUS NECESSAIRE SI ON FAIT LA GESTION DYNAMIQUE PAR CALENDRIER ===
    // Mais on la garde pour l'instant si vous voulez garder la version textarea
    @GetMapping("/disponibilites")
    public String showDisponibilitesForm(HttpSession session, Model model) {
        // ... Logique ...
        return "enseignant/disponibilites";
    }
>>>>>>> develop
}