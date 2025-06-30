// SujetServiceImpl.java
package com.fsm.Soutenances.service;

import com.fsm.Soutenances.model.Sujet;
import com.fsm.Soutenances.repository.*;
import com.fsm.Soutenances.service.SujetService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SujetServiceImpl implements SujetService {
    private final SujetRepository sujetRepository;

    public SujetServiceImpl(SujetRepository sujetRepository) {
        this.sujetRepository = sujetRepository;
    }

    @Override
    public Sujet save(Sujet sujet) {
        return sujetRepository.save(sujet);
    }

    @Override
    public List<Sujet> findAll() {
        return sujetRepository.findAll();
    }
}