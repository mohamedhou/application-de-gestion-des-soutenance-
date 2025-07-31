package com.fsm.Soutenances.model;

import jakarta.persistence.Entity;
<<<<<<< HEAD
=======
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
>>>>>>> develop
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
<<<<<<< HEAD
public class Administrateur extends Personne {
=======
@Table(name = "administrateur")
public class Administrateur extends Personne {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;  // Ajout de l'ID
	    
	    // ... autres champs spécifiques si nécessaire ...

	    // Getters et setters pour l'ID
	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }
>>>>>>> develop
}