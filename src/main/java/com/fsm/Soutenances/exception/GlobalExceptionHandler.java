package com.fsm.Soutenances.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public String handleNullPointer(NullPointerException e, Model model, HttpServletRequest request) {
        if (e.getMessage() != null && e.getMessage().contains("com.fsm.Soutenances.model.Enseignant")) {
            return "redirect:/login";
        }
        
        model.addAttribute("error", "Erreur système : " + e.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model, HttpServletRequest request) {
        // Ignorer les erreurs de ressources statiques
        if (e instanceof NoResourceFoundException || 
            (e.getMessage() != null && e.getMessage().contains("favicon.ico"))) {
            return null;
        }
        
        // Redirection pour les erreurs de session
        if (e instanceof NullPointerException && 
            e.getMessage().contains("com.fsm.Soutenances.model.Enseignant")) {
            return "redirect:/login";
        }
        
        model.addAttribute("error", "Erreur : " + e.getMessage());
        return "error";
    }
}