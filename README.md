# 🎓 Application de Gestion des Soutenances - FSM

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Application web complète pour la gestion centralisée du processus des soutenances de fin d'études à la Faculté des Sciences de Meknès (FSM), conçue pour remplacer les processus manuels par un système fluide, automatisé et efficace.

![Aperçu du Dashboard Admin](https://i.ibb.co/6P6Xy9h/image.png)

---

## 🚀 Problématique

La gestion manuelle des soutenances via des fichiers Excel et des échanges d'e-mails est souvent source de confusion, d'erreurs et de stress pour l'administration, les enseignants et les étudiants. Ce projet a été développé pour résoudre cette problématique en offrant une plateforme unique et centralisée pour tous les acteurs du processus.

## ✨ Fonctionnalités Clés

L'application est divisée en trois espaces distincts, chacun avec des fonctionnalités adaptées à son rôle :

### 👨‍💼 Espace Administrateur
- **Tableau de bord centralisé** avec statistiques clés.
- **Validation des sujets** proposés par les enseignants.
- **Planification intelligente des soutenances** avec vérification dynamique des disponibilités des enseignants (jury).
- **Gestion complète (CRUD)** des salles, des utilisateurs (étudiants et enseignants) et des filières.
- **Moteur de recherche avancé** pour filtrer les soutenances par étudiant, encadrant ou date.

### 👨‍🏫 Espace Enseignant
- **Dashboard personnalisé** affichant les sujets proposés, les soutenances encadrées et les participations en tant que jury.
- **Proposition de nouveaux sujets** de PFE.
- **Gestion des indisponibilités** via un calendrier interactif et dynamique (FullCalendar).
- **Consultation des rapports** déposés par les étudiants encadrés.

### 🎓 Espace Étudiant
- **Tableau de bord personnalisé** pour suivre l'évolution de son PFE.
- **Consultation des sujets disponibles** filtrés par sa filière.
- **Choix d'un sujet** de fin d'études.
- **Consultation des détails de sa soutenance** (date, heure, salle, jury).
- **Dépôt du rapport** de PFE (upload de fichiers).
- **Génération et téléchargement de la convocation** de soutenance au format **PDF**.

---

## 🛠️ Stack Technique

| Catégorie      | Technologie                                                                  |
|----------------|------------------------------------------------------------------------------|
| **Backend**    | Java 17, Spring Boot 3.x, Spring MVC, Spring Data JPA, Hibernate             |
| **Frontend**   | Thymeleaf (Templates côté serveur), HTML5, CSS3, JavaScript (AJAX/Fetch API) |
| **Base de Données**| MySQL                                                                        |
| **Outils**     | Maven, Git & GitHub, iText 7 (pour la génération PDF)                         |
| **Sécurité**   | Mécanisme manuel via `HttpSession` et Intercepteurs Spring MVC                |

---

## 🏁 Installation et Lancement

Pour lancer ce projet en local, suivez ces étapes :

### Prérequis
- JDK 17 ou supérieur
- Apache Maven
- Un serveur MySQL

### Étapes
1.  **Clonez le dépôt :**
    ```bash
    git clone https://github.com/[VOTRE_USERNAME]/[NOM_DU_PROJET].git
    cd [NOM_DU_PROJET]
    ```
2.  **Configurez la base de données :**
    - Créez une base de données MySQL nommée `gestion_soutenances`.
    - Ouvrez le fichier `src/main/resources/application.properties` et mettez à jour les informations de connexion (`spring.datasource.username` et `spring.datasource.password`).
    
3.  **Lancez l'application :**
    ```bash
    mvn spring-boot:run
    ```
4.  **Accédez à l'application :**
    Ouvrez votre navigateur et allez à `http://localhost:8080`.

---
---

---

## 📸 Galerie d'aperçus

Voici un aperçu des différentes interfaces de l'application, organisées par rôle d'utilisateur.

### 🔑 Authentification
| Page de Connexion Administrateur  | Page de Connexion Étudiant | Page de Connexion Enseignant |
| :-----------------------: | :-----------------------: | :-----------------------: |
| ![][login-admin] | ![][login-etudiant] | ![][login-enseignant] |

### 👨‍💼 Espace Administrateur
| Dashboard Principal (Vue d'ensemble) | Gestion des Salles (CRUD) | Planification de Soutenance (Interface Dynamique) |
| :---: | :---: | :---: |
| ![][admin-dashboard] | ![][admin-salles] | ![][admin-planifier] |

| Gestion des Utilisateurs | Recherche Avancée de Soutenances | Gestion des Soutenances |
| :---: | :---: | :---: |
| ![][admin-utilisateurs] | ![][admin-recherche] | ![][admin-soutenances] |

### 👨‍🏫 Espace Enseignant
| Dashboard Enseignant | Proposition d'un Nouveau Sujet | Calendrier des indisponibilités |
| :---: | :---: | :---: |
| ![][enseignant-dashboard] | ![][enseignant-proposer] | ![][enseignant-calendrier] |

### 🎓 Espace Étudiant
| Dashboard (Sujet à choisir) | Dashboard (Soutenance planifiée) | Convocation PDF |
| :---: | :---: | :---: |
| ![][etudiant-choisir] | ![][etudiant-dashboard] | ![][etudiant-pdf] |

<!-- ====== DÉFINITION DES LIENS D'IMAGES (LI GHADI YKHEDMO) ====== -->
<!-- Remplacez les noms de fichiers par les vôtres s'ils sont différents -->

[login-admin]: aperçu/loginAdmin.png "Page de Connexion Admin"
[login-etudiant]: aperçu/loginEtudiant.png "Page de Connexion Étudiant"
[login-enseignant]: aperçu/loginEnseignant.png "Page de Connexion Enseignant"

[admin-dashboard]: aperçu/dashboardAdmin.png "Dashboard de l'Admin"
[admin-salles]: aperçu/gestionSalles.png "Gestion des Salles"
[admin-planifier]: aperçu/planifierSoutenance.png "Planification de Soutenance"
[admin-utilisateurs]: aperçu/gererEtilisateur.png "Gestion des Utilisateurs"
[admin-recherche]: aperçu/chercherSoutenance.png "Recherche de Soutenances"
[admin-soutenances]: aperçu/gestionSoutenance.png "Liste des Soutenances"

[enseignant-dashboard]: aperçu/dachboardEnseignant.png "Dashboard Enseignant"
[enseignant-proposer]: aperçu/proposerSujet.png "Proposition de Sujet"
[enseignant-calendrier]: aperçu/disponibilite.png "Calendrier des Indisponibilités"

[etudiant-choisir]: aperçu/choisirSujet.png "Dashboard Étudiant - Choisir un Sujet"
[etudiant-dashboard]: aperçu/dachboardEtudiant.jpg "Dashboard Étudiant - Sujet Affecté"
[etudiant-pdf]: aperçu/convocation.png "Exemple de Convocation PDF"
---
## 📜 Licence

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

---

## 👨‍💻 Auteur
- **HOUCHT MOHAMED** - [mohammedhoucht](https://github.com/mohamedhou)
