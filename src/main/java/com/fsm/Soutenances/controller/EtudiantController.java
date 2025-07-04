package com.fsm.Soutenances.controller;
import org.springframework.transaction.annotation.Transactional;

import com.fsm.Soutenances.model.*;
import com.fsm.Soutenances.repository.*;
import com.fsm.Soutenances.service.PdfGenerationService;

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
    @Transactional // <-- Annotation mohimma l la méthode li katjib des données LAZY
    public String dashboard(HttpSession session, Model model) {
        
        // 1. Kanakhdo l'objet l'9dim men HttpSession bach n3erfo ghir l'ID
        Object userFromSession = session.getAttribute("user");
        if (!(userFromSession instanceof Etudiant)) {
            return "redirect:/login-etudiant";
        }
        Long etudiantId = ((Etudiant) userFromSession).getId();

        // 2. HADA HOWA L'POINT L'MOHIM: Kan3awdo njbdo l'étudiant jdid men la base de données
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé, veuillez vous reconnecter."));
        
        // 3. Daba, l'objet "etudiant" m'attaché l la session Hibernate.
        // N9edro n accessiw les relations LAZY dyalo bla mochkil.
        
        model.addAttribute("etudiant", etudiant);
        
        // Hada l'code li kan 3ndek kayn, w daba ghadi ykhdem
        // La liste des sujets est pour le cas où l'étudiant n'a pas encore de sujet
        if(etudiant.getSujet() == null){
            model.addAttribute("sujets", sujetRepository.findByValideTrueAndEtudiantIsNull());
        }
        
        Soutenance soutenance = soutenanceRepository.findByEtudiant(etudiant).orElse(null);
        model.addAttribute("soutenance", soutenance);
        
        model.addAttribute("hasSujet", etudiant.getSujet() != null);
        model.addAttribute("hasRapport", soutenance != null && soutenance.getRapport() != null);
        
        return "etudiant/dashboard";
    }
   
    
    /**
     * Méthode "Filter" privée pour vérifier si l'utilisateur est un étudiant connecté.
     * Si ce n'est pas le cas, on le redirige vers le login.
     * C'est une sécurité supplémentaire.
     */
    private boolean isUserNotStudent(HttpSession session) {
        Object user = session.getAttribute("user");
        // La condition retourne true si l'utilisateur n'est PAS un étudiant.
        return !(user instanceof Etudiant);
    }
    
    @GetMapping({"", "/"})
    public String redirectToDashboard() {
        return "redirect:/etudiant/dashboard";
    }

//    @GetMapping("/dashboard")
//    public String dashboard(HttpSession session, Model model) {
//        Etudiant etudiant = (Etudiant) session.getAttribute("user");
//        model.addAttribute("etudiant", etudiant);
//        
//        // Sujets validés disponibles
//        model.addAttribute("sujets", sujetRepository.findByValideTrue());
//        
//        // Soutenance de l'étudiant
//        Soutenance soutenance = getSoutenanceForEtudiant(etudiant);
//        model.addAttribute("soutenance", soutenance);
//        
//        // Vérifier si l'étudiant a déjà un sujet
//        boolean hasSujet = etudiant.getSujet() != null;
//        model.addAttribute("hasSujet", hasSujet);
//        
//        // Vérifier si un rapport a été déposé
//        if (soutenance != null && soutenance.getRapport() != null) {
//            model.addAttribute("hasRapport", true);
//        } else {
//            model.addAttribute("hasRapport", false);
//        }
//        
//        return "etudiant/dashboard";
//    }

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
    
//    @GetMapping("/telecharger-convocation")
//    public ResponseEntity<Resource> telechargerConvocation(HttpSession session) {
//        Etudiant etudiant = (Etudiant) session.getAttribute("user");
//        Soutenance soutenance = getSoutenanceForEtudiant(etudiant);
//        
//        if (soutenance == null || soutenance.getConvocation() == null) {
//            return ResponseEntity.notFound().build();
//        }
//        
//        Rapport convocation = soutenance.getConvocation();
//        ByteArrayResource resource = new ByteArrayResource(convocation.getData());
//        
//        return ResponseEntity.ok()
//            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + convocation.getNomFichier() + "\"")
//            .contentType(MediaType.parseMediaType(convocation.getTypeMime()))
//            .body(resource);
//    }
    
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
    

 // F EtudiantController.java

    @Autowired
    private PdfGenerationService pdfService; // <-- ZID L'INJECTION DYAL SERVICE JADID

    @GetMapping("/telecharger-convocation")
    public ResponseEntity<Resource> telechargerConvocation(HttpSession session) {
        if (isUserNotStudent(session)) {
            // Redirection ne fonctionnera pas ici, on retourne un statut interdit
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Etudiant etudiant = (Etudiant) session.getAttribute("user");
        // On recharge l'étudiant pour être sûr que toutes les données sont à jour (surtout les relations)
        etudiant = etudiantRepository.findById(etudiant.getId()).orElse(null);
        if(etudiant == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        
        Soutenance soutenance = soutenanceRepository.findByEtudiant(etudiant).orElse(null);
        
        if (soutenance == null) {
            // Normalement ce cas est déjà géré par la logique d'affichage du bouton
            return ResponseEntity.notFound().build();
        }
        
        try {
            // ==========> HNA FIN KAN3EYTO L SERVICE JADID <==========
            byte[] pdfBytes = pdfService.generateConvocationPdf(soutenance);
            
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"convocation_" + etudiant.getNom() + ".pdf\"")
                    .contentType(MediaType.APPLICATION_PDF) // <-- Le type de contenu correct
                    .contentLength(pdfBytes.length)
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace(); // Pour le débuggage
            // On retourne une erreur 500 si la génération du PDF a échoué
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    
}