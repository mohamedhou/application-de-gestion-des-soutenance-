package com.fsm.Soutenances.controller;
<<<<<<< HEAD
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
=======

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fsm.Soutenances.model.*;
import com.fsm.Soutenances.repository.*;
import com.fsm.Soutenances.service.PdfGenerationService;

@Controller
@RequestMapping("/etudiant")
public class EtudiantController {
    
    // Dépendances injectées (bonne pratique avec le constructeur)
    private final SujetRepository sujetRepository;
    private final SoutenanceRepository soutenanceRepository;
    private final EtudiantRepository etudiantRepository;
    private final RapportRepository rapportRepository;
    private final PdfGenerationService pdfService;

    @Autowired
    public EtudiantController(SujetRepository sujetRepository, SoutenanceRepository soutenanceRepository,
                              EtudiantRepository etudiantRepository, RapportRepository rapportRepository,
                              PdfGenerationService pdfService) {
        this.sujetRepository = sujetRepository;
        this.soutenanceRepository = soutenanceRepository;
        this.etudiantRepository = etudiantRepository;
        this.rapportRepository = rapportRepository;
        this.pdfService = pdfService;
    }
    
    // ========== Default Redirect & Security Check ==========
    @GetMapping({"", "/"})
    public String redirectToDashboard() {
        return "redirect:/etudiant/dashboard";
    }

    private boolean isUserNotStudent(HttpSession session) {
        return !(session.getAttribute("user") instanceof Etudiant);
    }
    
    // ========== DASHBOARD ==========
    @GetMapping("/dashboard")
    @Transactional
    public String dashboard(HttpSession session, Model model) {
        if (isUserNotStudent(session)) return "redirect:/login-etudiant";
        
        Long etudiantId = ((Etudiant) session.getAttribute("user")).getId();
        Etudiant etudiant = etudiantRepository.findById(etudiantId).orElseThrow(() -> new RuntimeException("Étudiant introuvable."));
        
        model.addAttribute("etudiant", etudiant);
        model.addAttribute("hasSujet", etudiant.getSujet() != null);

        Soutenance soutenance = soutenanceRepository.findByEtudiant(etudiant).orElse(null);
        model.addAttribute("soutenance", soutenance);
        model.addAttribute("hasRapport", soutenance != null && soutenance.getRapport() != null);
        
        if (etudiant.getSujet() == null) {
            if (etudiant.getFiliere() != null) {
                model.addAttribute("sujets", sujetRepository.findSujetsDisponiblesPourFiliere(etudiant.getFiliere().getId()));
            } else {
                model.addAttribute("sujets", Collections.emptyList());
            }
        }
>>>>>>> develop
        
        return "etudiant/dashboard";
    }

<<<<<<< HEAD
    @PostMapping("/choisir-sujet/{id}")
    public String choisirSujet(@PathVariable Long id, HttpSession session) {
        Etudiant etudiant = (Etudiant) session.getAttribute("user");
        // Logique pour associer le sujet à l'étudiant
        return "redirect:/etudiant/dashboard";
    }
=======
    // ========== ACTIONS ÉTUDIANT ==========
    @PostMapping("/choisir-sujet/{sujetId}")
    @Transactional
    public String choisirSujet(@PathVariable Long sujetId, HttpSession session, RedirectAttributes redirectAttributes) {
        if (isUserNotStudent(session)) return "redirect:/login-etudiant";

        Long etudiantId = ((Etudiant) session.getAttribute("user")).getId();
        Etudiant etudiant = etudiantRepository.findById(etudiantId).orElseThrow(() -> new RuntimeException("Étudiant introuvable"));
        
        if (etudiant.getSujet() != null) {
             redirectAttributes.addFlashAttribute("errorMessage", "Vous avez déjà un sujet.");
             return "redirect:/etudiant/dashboard";
        }
        
        Sujet sujetChoisi = sujetRepository.findById(sujetId).orElseThrow(() -> new RuntimeException("Sujet introuvable"));
        
        if(sujetChoisi.getEtudiant() != null){
             redirectAttributes.addFlashAttribute("errorMessage", "Désolé, ce sujet a déjà été pris par un autre étudiant.");
             return "redirect:/etudiant/dashboard";
        }

        etudiant.setSujet(sujetChoisi);
        etudiantRepository.save(etudiant);

        redirectAttributes.addFlashAttribute("successMessage", "Sujet '" + sujetChoisi.getTitre() + "' choisi avec succès !");
        return "redirect:/etudiant/dashboard";
    }

    // ==========> HADI HIYA LA METHODE L'JDIDA LI T'ZADET <==========
    /**
     * Affiche la page (formulaire) pour soumettre un rapport.
     * Répond à la requête GET.
     */
    @GetMapping("/soumettre-rapport")
    public String showSoumettreRapportForm(HttpSession session) {
        if (isUserNotStudent(session)) {
            return "redirect:/login-etudiant";
        }
        // Pas besoin d'envoyer de modèle, la page est simple
        return "etudiant/soumettreRapport"; // Nom du fichier HTML à afficher
    }

    /**
     * Traite la soumission du formulaire de rapport (le fichier uploadé).
     * Répond à la requête POST.
     */
    @PostMapping("/soumettre-rapport")
    public String soumettreRapport(@RequestParam("file") MultipartFile file, HttpSession session, RedirectAttributes redirectAttributes) {
        if (isUserNotStudent(session)) return "redirect:/login-etudiant";

        Etudiant etudiant = (Etudiant) session.getAttribute("user");
        Soutenance soutenance = soutenanceRepository.findByEtudiant(etudiant).orElse(null);
        
        if (soutenance == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Action impossible: aucune soutenance n'est planifiée pour vous.");
            return "redirect:/etudiant/dashboard";
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
            
            redirectAttributes.addFlashAttribute("successMessage", "Rapport soumis avec succès!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Échec de l'envoi du rapport. Veuillez réessayer.");
            return "redirect:/etudiant/soumettre-rapport";
        }
        return "redirect:/etudiant/dashboard";
    }

    // ========== ACTIONS ÉTUDIANT ==========

   
    
    // ========== CONSULTATION & TÉLÉCHARGEMENT ==========
    
    @GetMapping("/consulter-soutenance")
    @Transactional 
    public String consulterSoutenance(HttpSession session, Model model) {
        if (isUserNotStudent(session)) { return "redirect:/login-etudiant"; }

        Long etudiantId = ((Etudiant) session.getAttribute("user")).getId();
        Soutenance soutenance = soutenanceRepository.findByEtudiant(etudiantRepository.findById(etudiantId).get())
                .orElse(null); // ou .orElseThrow(...)
        
        model.addAttribute("soutenance", soutenance);
        return "etudiant/detailsSoutenance";
    }
    
    @GetMapping("/telecharger-convocation")
    public ResponseEntity<Resource> telechargerConvocation(HttpSession session) {
        if (isUserNotStudent(session)) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }
        
        Long etudiantId = ((Etudiant) session.getAttribute("user")).getId();
        Soutenance soutenance = soutenanceRepository.findByEtudiant(etudiantRepository.findById(etudiantId).get()).orElse(null);
        
        if (soutenance == null) { return ResponseEntity.notFound().build(); }
        
        try {
            byte[] pdfBytes = pdfService.generateConvocationPdf(soutenance);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"convocation.pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdfBytes.length)
                    .body(new ByteArrayResource(pdfBytes));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
>>>>>>> develop
}