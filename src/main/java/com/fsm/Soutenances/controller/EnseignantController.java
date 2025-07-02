package com.fsm.Soutenances.controller;

import com.fsm.Soutenances.model.Enseignant;
import com.fsm.Soutenances.model.Sujet;
import com.fsm.Soutenances.repository.SoutenanceRepository;
import com.fsm.Soutenances.repository.SujetRepository;
import com.fsm.Soutenances.service.CreneauIndisponibleService;
import com.fsm.Soutenances.service.EnseignantService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/enseignant")
public class EnseignantController {
    
    @Autowired 
    private SujetRepository sujetRepository;
    
    // N7iydo SoutenanceRepository men hna 7it EnseignantService déja kayst3mlo
    // @Autowired 
    // private SoutenanceRepository soutenanceRepository; 
    
    @Autowired 
    private EnseignantService enseignantService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        
        if (user instanceof Enseignant) {
            Enseignant enseignant = (Enseignant) user;
            
            model.addAttribute("enseignant", enseignant);
            model.addAttribute("sujets", sujetRepository.findByEncadrant(enseignant));
            
            model.addAttribute("soutenancesJure", enseignantService.getSoutenancesJure(enseignant));
            model.addAttribute("soutenancesEncadrees", enseignantService.getSoutenancesEncadrees(enseignant));
            
            return "enseignant/dashboard";
        } else {
            // CORRECTION: Redirection vers la page de login spécifique
            return "redirect:/login-enseignant";
        }
    }
    
    @GetMapping("/proposer-sujet")
    public String proposerSujetForm(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (!(user instanceof Enseignant)) {
            // CORRECTION: Redirection vers la page de login spécifique
            return "redirect:/login-enseignant";
        }
        
        model.addAttribute("sujet", new Sujet());
        return "enseignant/proposer-sujet";
    }

    @PostMapping("/proposer-sujet")
    public String proposerSujet(@ModelAttribute Sujet sujet, HttpSession session) {
        Object user = session.getAttribute("user");
        if (!(user instanceof Enseignant)) {
            return "redirect:/login-enseignant";
        }
        
        Enseignant enseignant = (Enseignant) user; 
        
        sujet.setEncadrant(enseignant);
        sujet.setValide(false); // Par defaut, un sujet n'est pas valide
        sujetRepository.save(sujet);
        return "redirect:/enseignant/dashboard";
    }
    
 // F EnseignantController.java 

    @GetMapping("/disponibilites")
    public String showDisponibilitesForm(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (!(user instanceof Enseignant)) {
            return "redirect:/login-enseignant";
        }
        
        Enseignant enseignant = (Enseignant) user;
        // Nṣifto les disponibilités l'7aliyin l la page bach ybanou
        model.addAttribute("disponibilitesActuelles", enseignant.getDisponibilites());
        
        return "enseignant/disponibilites"; // Ghadi nṣaybo had l'fichier HTML
    }

    @PostMapping("/disponibilites")
    public String saveDisponibilites(@RequestParam("disponibilitesText") String disponibilitesText, 
                                     HttpSession session) {
        Object user = session.getAttribute("user");
        if (!(user instanceof Enseignant)) {
            return "redirect:/login-enseignant";
        }

        Enseignant enseignant = (Enseignant) user;
        
        enseignantService.enregistrerDisponibilites(enseignant, disponibilitesText);
        
        return "redirect:/enseignant/dashboard?success=dispo_saved";
    }
 // ... Injecter le nouveau service ...
    @Autowired private CreneauIndisponibleService creneauService;

  
    @GetMapping("/calendrier-disponibilites")
    public String showCalendrierPage(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (!(user instanceof Enseignant)) {
            return "redirect:/login-enseignant";
        }
        
        Enseignant enseignant = (Enseignant) user;
        // On passe l'ID de l'enseignant à la vue pour que le JavaScript puisse l'utiliser
        model.addAttribute("enseignantId", enseignant.getId());
        
        return "enseignant/calendrier_disponibilites"; // Le nom du nouveau fichier HTML
    }
    
    
}