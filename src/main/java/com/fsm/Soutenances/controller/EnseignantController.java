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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/enseignant")
public class EnseignantController {
    
    @Autowired 
    private SujetRepository sujetRepository;
    
    @Autowired 
    private SoutenanceRepository soutenanceRepository;
    
    @Autowired 
    private EnseignantService enseignantService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        
        if (user instanceof Enseignant) {
            Enseignant enseignant = (Enseignant) user;
            
            // Utiliser directement l'objet de session sans rafraîchir
            model.addAttribute("enseignant", enseignant);
            model.addAttribute("sujets", sujetRepository.findByEncadrant(enseignant));
            model.addAttribute("soutenances", soutenanceRepository.findByEnseignantId(enseignant.getId()));
            
            return "enseignant/dashboard";
        } else {
            return "redirect:/login";
        }
    }
    
    @GetMapping("/proposer-sujet")
    public String proposerSujetForm(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (!(user instanceof Enseignant)) {
            return "redirect:/login";
        }
        
        model.addAttribute("sujet", new Sujet());
        return "enseignant/proposer-sujet";
    }

    @PostMapping("/proposer-sujet")
    public String proposerSujet(@ModelAttribute Sujet sujet, HttpSession session) {
        Object user = session.getAttribute("user");
        if (!(user instanceof Enseignant)) {
            return "redirect:/login";
        }
        
        Enseignant enseignant = (Enseignant) user;
        // Rafraîchir les données depuis la base
        enseignant = enseignantService.findById(enseignant.getId());
        
        sujet.setEncadrant(enseignant);
        sujet.setValide(false);
        sujetRepository.save(sujet);
        return "redirect:/enseignant/dashboard";
    }
}