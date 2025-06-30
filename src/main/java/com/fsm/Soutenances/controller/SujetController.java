// SujetController.java
package com.fsm.Soutenances.controller;

import com.fsm.Soutenances.model.Sujet;
import com.fsm.Soutenances.service.SujetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sujets")
public class SujetController {
    private final SujetService sujetService;

    public SujetController(SujetService sujetService) {
        this.sujetService = sujetService;
    }

    @GetMapping
    public String listSujets(Model model) {
        model.addAttribute("sujets", sujetService.findAll());
        return "sujets/list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("sujet", new Sujet());
        return "sujets/form";
    }

    @PostMapping
    public String saveSujet(@ModelAttribute Sujet sujet) {
        sujetService.save(sujet);
        return "redirect:/sujets";
    }
}