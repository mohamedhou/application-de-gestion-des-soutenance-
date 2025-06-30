package com.fsm.Soutenances.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Etudiant extends Personne {
    private Date dateInscription;
    private String massar;
}
