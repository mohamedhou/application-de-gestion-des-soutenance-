package com.fsm.Soutenances.config;

import com.fsm.Soutenances.controller.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/**") // 7mi kolchi
            .excludePathPatterns(
                "/", 
                "/login-etudiant", 
                "/login-enseignant", 
                "/login-admin", 
                "/css/**", 
                "/js/**", 
                "/images/**", 
                "/error", 
                "/favicon.ico"
            );
    }
}