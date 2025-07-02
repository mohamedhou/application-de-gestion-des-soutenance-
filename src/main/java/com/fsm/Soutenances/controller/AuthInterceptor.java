package com.fsm.Soutenances.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, 
                             HttpServletResponse response, 
                             Object handler) throws Exception {
        
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");

        // URLs publiques qu'on ne protège pas
        if (requestURI.startsWith("/login") || 
            requestURI.startsWith("/css") || 
            requestURI.startsWith("/js") || 
            requestURI.equals("/")) {
            return true;
        }

        if (user != null) {
            return true;
        }

        // Sauvegarder l'URL demandée pour redirection après login
        session.setAttribute("originalUrl", requestURI);

        if (requestURI.startsWith("/admin")) {
            response.sendRedirect("/login-admin");
        } else if (requestURI.startsWith("/enseignant")) {
            response.sendRedirect("/login-enseignant");
        } else {
            response.sendRedirect("/login-etudiant");
        }

        return false;
    }
}
