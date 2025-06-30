package com.fsm.Soutenances.controller;
import com.fsm.Soutenances.model.Etudiant;
import com.fsm.Soutenances.model.Soutenance;
import com.fsm.Soutenances.repository.SoutenanceRepository;
import com.fsm.Soutenances.repository.SujetRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/etudiant")
public class EtudiantController {
    @Autowired private SujetRepository sujetRepository;
    @Autowired private SoutenanceRepository soutenanceRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Etudiant etudiant = (Etudiant) session.getAttribute("user");
        model.addAttribute("etudiant", etudiant);
        
        model.addAttribute("sujets", sujetRepository.findByValideTrue());
        
        Soutenance soutenance = soutenanceRepository.findByEtudiantId(etudiant.getId())
                                  .stream()
                                  .findFirst()
                                  .orElse(null);
        model.addAttribute("soutenance", soutenance);
        
        return "etudiant/dashboard";
    }

    @PostMapping("/choisir-sujet/{id}")
    public String choisirSujet(@PathVariable Long id, HttpSession session) {
        Etudiant etudiant = (Etudiant) session.getAttribute("user");
        // Logique pour associer le sujet à l'étudiant
        return "redirect:/etudiant/dashboard";
    }
}