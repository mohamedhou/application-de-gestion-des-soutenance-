package com.fsm.Soutenances.model;

<<<<<<< HEAD
import jakarta.persistence.Entity;

@Entity
public class Enseignant extends Personne {
    private String specialite;

    // Getters/setters
    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
=======
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList; // Zid had l'import

@Entity
public class Enseignant extends Personne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String specialite;
    // L'attribut String disponibilites ma b9inach kanst3mlouh
  
    // ==> ZID HADI L'RELATION L'MOHIMMA <==
    @OneToMany(mappedBy = "enseignant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreneauIndisponible> indisponibilites = new ArrayList<>();

    // Relation m3a Filiere (déja kayna 3ndek)
    // L'enseignant EST propriétaire de la relation.
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
      name = "enseignant_filiere", 
      joinColumns = @JoinColumn(name = "enseignant_id"), 
      inverseJoinColumns = @JoinColumn(name = "filiere_id"))
    private List<Filiere> filieresEnseignees = new ArrayList<>();
    
    // ==> HADI HIYA L'RELATION LI KANET NA9SSA <==
    // Un enseignant peut encadrer plusieurs sujets
    @OneToMany(mappedBy = "encadrant", fetch = FetchType.LAZY)
    private List<Sujet> sujets = new ArrayList<>();

    // GETTERS & SETTERS (KAMLIN)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
    
    public List<CreneauIndisponible> getIndisponibilites() { return indisponibilites; }
    public void setIndisponibilites(List<CreneauIndisponible> indisponibilites) { this.indisponibilites = indisponibilites; }

    public List<Filiere> getFilieresEnseignees() { return filieresEnseignees; }
    public void setFilieresEnseignees(List<Filiere> filieresEnseignees) { this.filieresEnseignees = filieresEnseignees; }

    // ===> ZID L'GETTER W L'SETTER DYAL SUJETS <===
    public List<Sujet> getSujets() { return sujets; }
    public void setSujets(List<Sujet> sujets) { this.sujets = sujets; }
>>>>>>> develop
}