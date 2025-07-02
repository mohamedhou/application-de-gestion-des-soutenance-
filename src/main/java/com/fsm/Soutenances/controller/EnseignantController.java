package com.fsm.Soutenances.controller;

import com.fsm.Soutenances.model.Enseignant;
import com.fsm.Soutenances.model.Sujet;
import com.fsm.Soutenances.repository.SoutenanceRepository;
import com.fsm.Soutenances.repository.SujetRepository;
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
            // CORRECTION: Redirection vers la page de login spécifique
            return "redirect:/login-enseignant";
        }
        
        Enseignant enseignant = (Enseignant) user;
        // Pas besoin de refaire un findById, l'objet dans la session est souvent suffisant
        // enseignant = enseignantService.findById(enseignant.getId()); 
        
        sujet.setEncadrant(enseignant);
        sujet.setValide(false); // Par défaut, un sujet n'est pas validé
        sujetRepository.save(sujet);
        return "redirect:/enseignant/dashboard";
    }
}