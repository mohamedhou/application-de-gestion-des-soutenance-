// SujetService.java
package com.fsm.Soutenances.service;

import com.fsm.Soutenances.model.Sujet;
import java.util.List;

public interface SujetService {
    Sujet save(Sujet sujet);
    List<Sujet> findAll();
}