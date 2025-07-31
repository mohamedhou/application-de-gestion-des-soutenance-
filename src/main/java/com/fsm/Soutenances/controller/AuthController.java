package com.fsm.Soutenances.controller;

<<<<<<< HEAD

=======
>>>>>>> develop
import com.fsm.Soutenances.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    @Autowired private AuthService authService;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, 
                       @RequestParam String role,
                       HttpSession session,
                       Model model) {
        
        Object user = authService.authenticate(email, role);
        if (user != null) {
            session.setAttribute("user", user);
            session.setAttribute("role", role);
            
            switch (role) {
                case "etudiant": return "redirect:/etudiant/dashboard";
                case "enseignant": return "redirect:/enseignant/dashboard";
                case "admin": return "redirect:/admin/dashboard";
            }
        }
        
        model.addAttribute("error", "Email ou rôle incorrect");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
=======
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Pages de login spécifiques
    @GetMapping("/login-etudiant")
    public String showLoginEtudiant() {
        return "loginEtudiant";
    }

    @GetMapping("/login-enseignant")
    public String showLoginEnseignant() {
        return "loginEnseignant";
    }

    @GetMapping("/login-admin")
    public String showLoginAdmin() {
        return "loginAdmin";
    }

    // Points d'authentification spécifiques
    @PostMapping("/login-etudiant")
    public String loginEtudiant(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        return authenticateUser(email, password, "etudiant", session, model);
    }

    @PostMapping("/login-enseignant")
    public String loginEnseignant(@RequestParam String email,
                                 @RequestParam String password,
                                 HttpSession session,
                                 Model model) {
        return authenticateUser(email, password, "enseignant", session, model);
    }

    @PostMapping("/login-admin")
    public String loginAdmin(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {
        return authenticateUser(email, password, "admin", session, model);
    }

    // Méthode centrale d'authentification
    private String authenticateUser(String email, String password, String role,
                                  HttpSession session, Model model) {
        Object user = authService.authenticate(email, password, role);
        
        if (user != null) {
            // Authentification réussie
            session.setAttribute("user", user);
            
            // Récupération de l'URL originale demandée
            String originalUrl = (String) session.getAttribute("originalUrl");
            if (originalUrl != null && !originalUrl.isEmpty()) {
                session.removeAttribute("originalUrl");
                return "redirect:" + originalUrl;
            }
            
            // Redirection par défaut selon le rôle
            switch (role) {
                case "etudiant":
                    return "redirect:/etudiant/dashboard";
                case "enseignant":
                    return "redirect:/enseignant/dashboard";
                case "admin":
                    return "redirect:/admin/dashboard";
                default:
                    return "redirect:/";
            }
        } else {
            // Authentification échouée
            model.addAttribute("error", "Email ou mot de passe incorrect");
            model.addAttribute("lastEmail", email);
            
            // Retour à la page de login correspondante
            switch (role) {
                case "etudiant":
                    return "loginEtudiant";
                case "enseignant":
                    return "loginEnseignant";
                case "admin":
                    return "loginAdmin";
                default:
                    return "redirect:/";
            }
        }
    }

    // Méthode pour vérifier l'accès (utilisée par les autres contrôleurs)
    public static boolean checkAccess(HttpSession session, String requiredRole) {
        Object user = session.getAttribute("user");
        if (user == null) return false;
        
        // Implémentez la logique de vérification du rôle selon votre modèle
        // Exemple simplifié :
        return user.getClass().getSimpleName().equalsIgnoreCase(requiredRole);
>>>>>>> develop
    }
}