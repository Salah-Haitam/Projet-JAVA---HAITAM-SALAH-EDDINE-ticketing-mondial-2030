# 🏆 Plateforme Intelligente de Gestion des Tickets - Mondial 2030

Une application JavaFX complète pour la gestion des tickets de la Coupe du Monde 2030.

## 📋 Table des matières

- [Description](#Description)
- [Fonctionnalités](#fonctionnalités)
- [Architecture](#architecture)
- [Technologies](#technologies)
- [Installation](#installation)
- [Utilisation](#utilisation)
- [Structure du projet](#structure-du-projet)
- [Comptes de test](#comptes-de-test)

---
## 📝 Description

Cette plateforme permet la gestion complète des tickets pour la Coupe du Monde 2030, incluant :
- Gestion des matchs et des équipes
- Vente et transfert de tickets
- Suivi des flux de spectateurs
- Génération de rapports
- Système d'alertes en temps réel

## ✨ Fonctionnalités

### Pour les Spectateurs
- 🔐 Inscription et authentification sécurisée
- ⚽ Consultation des matchs disponibles
- 🎫 Achat de tickets avec choix de zone et catégorie
- 📤 Transfert de tickets à d'autres spectateurs
- ❌ Annulation et remboursement de tickets
- 📱 QR code unique pour chaque ticket
- 📊 Historique des transactions

### Pour les Administrateurs
- 📊 Dashboard avec statistiques en temps réel
- ⚽ Gestion complète des matchs
- 👥 Gestion des utilisateurs
- 🎫 Suivi de toutes les ventes de tickets
- 🔔 Système d'alertes (surpopulation, fraudes, etc.)
- 📈 Génération et export de rapports (PDF)
- 🚶 Visualisation des flux de spectateurs

## 🏗 Architecture

Le projet suit une architecture en couches (Layer Architecture) :

```
┌─────────────────────────────────────┐
│         Présentation (JavaFX)        │
│     Controllers + FXML + CSS         │
├─────────────────────────────────────┤
│           Services (Métier)          │
│  AuthService, TicketService, etc.    │
├─────────────────────────────────────┤
│        DAO (Data Access Object)      │
│    Interfaces + Implémentations      │
├─────────────────────────────────────┤
│         Entités JPA (Entity)         │
│   Utilisateur, Ticket, Match, etc.   │
├─────────────────────────────────────┤
│      Hibernate ORM + SQLite DB       │
└─────────────────────────────────────┘
```

### Patterns utilisés
- **DAO Pattern** : Séparation de la logique d'accès aux données
- **Singleton** : Services métier avec instance unique
- **MVC** : Model-View-Controller pour l'interface JavaFX
- **Factory** : Création des sessions Hibernate

## 🛠 Technologies

| Technologie | Version | Usage |
|-------------|---------|-------|
| Java | 17 | Langage principal |
| JavaFX | 21.0.1 | Interface utilisateur |
| Maven | 3.x | Gestion des dépendances |
| Hibernate | 6.4.1 | ORM (Object-Relational Mapping) |
| SQLite | 3.44.1 | Base de données embarquée |
| BCrypt | 0.4 | Hachage des mots de passe |
| ZXing | 3.5.2 | Génération de QR codes |
| iText | 8.0.2 | Génération de PDF |
| SLF4J | 2.0.9 | Logging |

## 📦 Installation

### Prérequis
- JDK 17 ou supérieur
- Maven 3.6 ou supérieur
- IDE recommandé : IntelliJ IDEA ou VS Code

### Étapes d'installation

1. **Cloner le projet**
   ```bash
   cd E:\javaprojet\ticketing-mondial-2030
   ```

2. **Compiler le projet**
   ```bash
   mvn clean compile
   ```

3. **Exécuter l'application**
   ```bash
   mvn javafx:run
   ```

### Alternative : Créer un JAR exécutable
```bash
mvn clean package
java -jar target/ticketing-mondial-2030-1.0-SNAPSHOT.jar
```

## 🚀 Utilisation

### Démarrage
1. Lancer l'application via Maven ou le JAR
2. L'écran de connexion s'affiche
3. Se connecter avec les identifiants de test (voir ci-dessous)

### Navigation Spectateur
1. **Accueil** : Aperçu des matchs et tickets
2. **Matchs** : Liste des matchs disponibles
3. **Mes Tickets** : Gestion des tickets achetés
4. **Historique** : Transactions passées
5. **Profil** : Modification des informations personnelles

### Navigation Administrateur
1. **Dashboard** : Statistiques globales et graphiques
2. **Matchs** : CRUD des matchs
3. **Utilisateurs** : Liste et gestion des spectateurs
4. **Tickets** : Vue de tous les tickets
5. **Alertes** : Gestion des alertes système
6. **Rapports** : Génération et export
7. **Flux** : Suivi des spectateurs

## 📁 Structure du projet

```
ticketing-mondial-2030/
├── src/main/java/com/mondial2030/
│   ├── MainApp.java                 # Point d'entrée
│   ├── controller/                   # Contrôleurs JavaFX
│   │   ├── BaseController.java
│   │   ├── LoginController.java
│   │   ├── AdminDashboardController.java
│   │   └── SpectateurDashboardController.java
│   ├── dao/                          # Data Access Objects
│   │   ├── interfaces/               # Interfaces DAO
│   │   └── impl/                     # Implémentations DAO
│   ├── entity/                       # Entités JPA
│   │   ├── Utilisateur.java
│   │   ├── Administrateur.java
│   │   ├── Spectateur.java
│   │   ├── Match.java
│   │   ├── Equipe.java
│   │   ├── Ticket.java
│   │   ├── Zone.java
│   │   ├── Siege.java
│   │   ├── Transaction.java
│   │   ├── Rapport.java
│   │   ├── Alerte.java
│   │   ├── FluxSpectateurs.java
│   │   ├── OptimisateurFlux.java
│   │   └── enums/                    # Énumérations
│   ├── service/                      # Services métier
│   │   ├── AuthenticationService.java
│   │   ├── TicketService.java
│   │   ├── MatchService.java
│   │   ├── AlerteService.java
│   │   ├── RapportService.java
│   │   └── FluxService.java
│   └── util/                         # Utilitaires
│       ├── HibernateUtil.java
│       ├── DataInitializer.java
│       └── QRCodeGenerator.java
├── src/main/resources/
│   ├── fxml/                         # Fichiers FXML
│   │   ├── Login.fxml
│   │   ├── AdminDashboard.fxml
│   │   └── SpectateurDashboard.fxml
│   ├── css/
│   │   └── style.css                 # Styles CSS
│   ├── hibernate.cfg.xml             # Config Hibernate
│   ├── application.properties        # Config application
│   └── logback.xml                   # Config logging
├── pom.xml                           # Config Maven
└── README.md                         # Ce fichier
```

## 🔑 Comptes de test

### Administrateur
- **Email** : `admin@mondial2030.com`
- **Mot de passe** : `admin123`

### Spectateur
- **Email** : `jean.dupont@email.com`
- **Mot de passe** : `test1234`



## 🔧 Configuration

### Base de données
La base SQLite est créée automatiquement au premier lancement (`mondial2030.db`).

### Personnalisation
Modifier `application.properties` pour :
- Seuils d'alertes de surpopulation
- Paramètres des tickets
- Chemins d'export des rapports

## 📄 Licence

Projet académique - Mondial 2030

## 👥 Auteurs

Mondial 2030 Development Team

---

⚽ **Mondial 2030** - Maroc, Espagne, Portugal
