package com.fsm.Soutenances.controller;


import com.fsm.Soutenances.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    }
}