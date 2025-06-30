package com.fsm.Soutenances.controller;

import com.fsm.Soutenances.model.Enseignant;
import com.fsm.Soutenances.model.Sujet;
import com.fsm.Soutenances.repository.SoutenanceRepository;
import com.fsm.Soutenances.repository.SujetRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/enseignant")
public class EnseignantController {
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
        return "enseignant/proposer-sujet";
    }

    @PostMapping("/proposer-sujet")
    public String proposerSujet(@ModelAttribute Sujet sujet, HttpSession session) {
        Enseignant enseignant = (Enseignant) session.getAttribute("user");
        sujet.setEncadrant(enseignant);
        sujet.setValide(false);
        sujetRepository.save(sujet);
        return "redirect:/enseignant/dashboard";
    }
}