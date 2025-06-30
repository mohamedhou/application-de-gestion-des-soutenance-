package com.fsm.Soutenances.model;


import jakarta.persistence.Entity;
import java.util.Date;

@Entity
public class Etudiant extends Personne {
    private Date dateInscription;
    private String massar;

    // Getters/setters
    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getMassar() {
        return massar;
    }

    public void setMassar(String massar) {
        this.massar = massar;
    }
}