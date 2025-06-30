package com.fsm.Soutenances.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Jury {
    @Id
    private Long id;
    
    @ManyToMany
    private List<Enseignant> enseignants;
}