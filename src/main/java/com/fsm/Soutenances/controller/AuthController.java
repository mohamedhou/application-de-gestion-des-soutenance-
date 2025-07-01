package com.fsm.Soutenances.controller;

import com.fsm.Soutenances.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired 
    private AuthService authService;

    @GetMapping("/")
    public String home() {
        return "redirect:/loginEtudiant";
    }

    @GetMapping("/loginEtudiant")
    public String loginEtudiantForm() {
        return "loginEtudiant";
    }

    @GetMapping("/loginEnseignant")
    public String loginEnseignantForm() {
        return "loginEnseignant";
    }

    @GetMapping("/loginAdmin")
    public String loginAdminForm() {
        return "loginAdmin";
    }

    @PostMapping("/loginEtudiant")
    public String loginEtudiant(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        
        Object user = authService.authenticate(email, password, "etudiant");
        return handleLogin(user, "etudiant", session, model, "loginEtudiant", email);
    }

    @PostMapping("/loginEnseignant")
    public String loginEnseignant(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        
        Object user = authService.authenticate(email, password, "enseignant");
        return handleLogin(user, "enseignant", session, model, "loginEnseignant", email);
    }

    @PostMapping("/loginAdmin")
    public String loginAdmin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        
        Object user = authService.authenticate(email, password, "admin");
        return handleLogin(user, "admin", session, model, "loginAdmin", email);
    }

    private String handleLogin(Object user, String role, 
                              HttpSession session, Model model, 
                              String loginPage, String email) {
        if (user != null) {
            session.setAttribute("user", user);
            session.setAttribute("role", role);
            return "redirect:/" + role + "/dashboard";
        }
        
        model.addAttribute("error", "Email ou mot de passe incorrect");
        model.addAttribute("lastEmail", email); // Conserve l'email pour ré-afficher
        return loginPage;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginEtudiant";
    }
}