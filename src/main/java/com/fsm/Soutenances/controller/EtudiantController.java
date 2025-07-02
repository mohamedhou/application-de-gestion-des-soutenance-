package com.fsm.Soutenances.controller;

import com.fsm.Soutenances.model.*;
import com.fsm.Soutenances.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/etudiant")
public class EtudiantController {
    
    @Autowired 
    private SujetRepository sujetRepository;
    
    @Autowired 
    private SoutenanceRepository soutenanceRepository;
    
    @Autowired
    private EtudiantRepository etudiantRepository;
    
    @Autowired
    private RapportRepository rapportRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Etudiant etudiant = (Etudiant) session.getAttribute("user");
        model.addAttribute("etudiant", etudiant);
        
        // Sujets validés disponibles
        model.addAttribute("sujets", sujetRepository.findByValideTrue());
        
        // Soutenance de l'étudiant
        Soutenance soutenance = getSoutenanceForEtudiant(etudiant);
        model.addAttribute("soutenance", soutenance);
        
        // Vérifier si l'étudiant a déjà un sujet
        boolean hasSujet = etudiant.getSujet() != null;
        model.addAttribute("hasSujet", hasSujet);
        
        // Vérifier si un rapport a été déposé
        if (soutenance != null && soutenance.getRapport() != null) {
            model.addAttribute("hasRapport", true);
        } else {
            model.addAttribute("hasRapport", false);
        }
        
        return "etudiant/dashboard";
    }

    @PostMapping("/choisir-sujet/{sujetId}")
    public String choisirSujet(@PathVariable Long sujetId, HttpSession session) {
        Etudiant etudiant = (Etudiant) session.getAttribute("user");
        
        // Vérifier que l'étudiant n'a pas déjà un sujet
        if (etudiant.getSujet() == null) {
            Sujet sujet = sujetRepository.findById(sujetId).orElse(null);
            if (sujet != null) {
                etudiant.setSujet(sujet);
                etudiantRepository.save(etudiant);
            }
        }
        return "redirect:/etudiant/dashboard";
    }
    
    @GetMapping("/consulter-sujets")
    public String consulterSujets(Model model) {
        model.addAttribute("sujets", sujetRepository.findByValideTrue());
        return "etudiant/consulterSujets";
    }
    
    @GetMapping("/telecharger-convocation")
    public ResponseEntity<Resource> telechargerConvocation(HttpSession session) {
        Etudiant etudiant = (Etudiant) session.getAttribute("user");
        Soutenance soutenance = getSoutenanceForEtudiant(etudiant);
        
        if (soutenance == null || soutenance.getConvocation() == null) {
            return ResponseEntity.notFound().build();
        }
        
        Rapport convocation = soutenance.getConvocation();
        ByteArrayResource resource = new ByteArrayResource(convocation.getData());
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + convocation.getNomFichier() + "\"")
            .contentType(MediaType.parseMediaType(convocation.getTypeMime()))
            .body(resource);
    }
    
    @GetMapping("/consulter-soutenance")
    public String consulterSoutenance(HttpSession session, Model model) {
        Etudiant etudiant = (Etudiant) session.getAttribute("user");
        Soutenance soutenance = getSoutenanceForEtudiant(etudiant);
        
        if (soutenance == null) {
            return "redirect:/etudiant/dashboard?error=no_soutenance";
        }
        
        model.addAttribute("soutenance", soutenance);
        return "etudiant/detailsSoutenance";
    }
    
    @GetMapping("/soumettre-rapport")
    public String soumettreRapportForm(HttpSession session, Model model) {
        Etudiant etudiant = (Etudiant) session.getAttribute("user");
        Soutenance soutenance = getSoutenanceForEtudiant(etudiant);
        
        if (soutenance == null) {
            return "redirect:/etudiant/dashboard?error=no_soutenance";
        }
        
        model.addAttribute("soutenance", soutenance);
        return "etudiant/soumettreRapport";
    }
    
    @PostMapping("/soumettre-rapport")
    public String soumettreRapport(
        @RequestParam("file") MultipartFile file,
        HttpSession session
    ) {
        Etudiant etudiant = (Etudiant) session.getAttribute("user");
        Soutenance soutenance = getSoutenanceForEtudiant(etudiant);
        
        if (soutenance == null) {
            return "redirect:/etudiant/dashboard?error=no_soutenance";
        }
        
        try {
            Rapport rapport = new Rapport();
            rapport.setNomFichier(file.getOriginalFilename());
            rapport.setTypeMime(file.getContentType());
            rapport.setData(file.getBytes());
            rapport.setDateSoumission(LocalDateTime.now());
            
            rapportRepository.save(rapport);
            soutenance.setRapport(rapport);
            soutenanceRepository.save(soutenance);
            
            return "redirect:/etudiant/dashboard?success=rapport_soumis";
        } catch (IOException e) {
            return "redirect:/etudiant/soumettre-rapport?error=upload_failed";
        }
    }
    
    private Soutenance getSoutenanceForEtudiant(Etudiant etudiant) {
        return soutenanceRepository.findByEtudiant(etudiant).orElse(null);
    }
    
}