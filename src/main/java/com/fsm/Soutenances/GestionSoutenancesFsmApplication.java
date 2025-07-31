package com.fsm.Soutenances;
<<<<<<< HEAD
import com.fsm.Soutenances.repository.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestionSoutenancesFsmApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionSoutenancesFsmApplication.class, args);
	}

}
=======

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class GestionSoutenancesFsmApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(GestionSoutenancesFsmApplication.class, args);
    }
}
>>>>>>> develop
