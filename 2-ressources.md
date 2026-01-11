# 📁 Ressources - Ticketing Mondial 2030


## � Table des Matières

- [Structure des Ressources](#-structure-des-ressources)
- [Fichiers FXML (Vues JavaFX)](#-fichiers-fxml-vues-javafx)
- [Feuille de Styles CSS](#-feuille-de-styles-css)
- [Images](#️-images)
- [Comptes de Test](#-comptes-de-test)
- [Chargement des Ressources dans le Code](#-chargement-des-ressources-dans-le-code)
- [Hot-Reload CSS (Développement)](#-hot-reload-css-développement)
- [Ressources Compilées](#-ressources-compilées-targetclasses)
- [Bonnes Pratiques Ressources](#-bonnes-pratiques-ressources)

---

## 📂 Structure des Ressources

```
src/main/resources/
├── fxml/                         # Vues JavaFX
│   ├── Login.fxml
│   ├── AdminDashboard.fxml
│   └── SpectateurDashboard.fxml
├── css/
│   └── style.css                 # Styles CSS (1206 lignes)
├── images/                       # Images et icônes
│   └── icon.png
├── hibernate.cfg.xml             # Configuration Hibernate
├── application.properties        # Configuration application
└── logback.xml                   # Configuration logging
```

---

## 🎨 Fichiers FXML (Vues JavaFX)

### 1. Login.fxml
| Propriété | Valeur |
|-----------|--------|
| **Chemin** | `src/main/resources/fxml/Login.fxml` |
| **Contrôleur** | `com.mondial2030.controller.LoginController` |
| **Fonction** | Page de connexion et inscription |
| **Caractéristiques** | Switch connexion/inscription, validation des champs |

### 2. AdminDashboard.fxml
| Propriété | Valeur |
|-----------|--------|
| **Chemin** | `src/main/resources/fxml/AdminDashboard.fxml` |
| **Contrôleur** | `com.mondial2030.controller.AdminDashboardController` |
| **Fonction** | Tableau de bord administrateur |
| **Sections** | Dashboard, Matchs, Utilisateurs, Tickets, Alertes, Rapports, Flux |

### 3. SpectateurDashboard.fxml
| Propriété | Valeur |
|-----------|--------|
| **Chemin** | `src/main/resources/fxml/SpectateurDashboard.fxml` |
| **Contrôleur** | `com.mondial2030.controller.SpectateurDashboardController` |
| **Fonction** | Espace spectateur |
| **Sections** | Accueil, Matchs, Mes Tickets, Historique, Profil |

---

## 🎨 Feuille de Styles CSS

### style.css
| Propriété | Valeur |
|-----------|--------|
| **Chemin** | `src/main/resources/css/style.css` |
| **Lignes** | 1206 lignes |
| **Police** | Segoe UI, Arial, sans-serif |
| **Thème** | Design moderne avec dégradés et animations |

### Principales Classes CSS

#### Conteneurs
| Classe | Description |
|--------|-------------|
| `.login-container` | Conteneur page de connexion (dégradé bleu) |
| `.dashboard-container` | Conteneur dashboard (dégradé gris clair) |
| `.login-card` | Carte de connexion avec ombre |

#### Formulaires
| Classe | Description |
|--------|-------------|
| `.text-field-custom` | Champs de texte personnalisés |
| `.text-field-custom:focused` | État focus avec bordure orange |
| `.field-label` | Labels des champs |
| `.error-label` | Messages d'erreur (rouge) |

#### Boutons
| Classe | Description |
|--------|-------------|
| `.btn-primary` | Bouton principal (orange) |
| `.btn-secondary` | Bouton secondaire |
| `.btn-danger` | Bouton danger (rouge) |

#### Typographie
| Classe | Description |
|--------|-------------|
| `.login-title` | Titre principal (36px, bold, blanc) |
| `.login-subtitle` | Sous-titre (14px, gris) |
| `.card-title` | Titre de carte (26px, bold, bleu) |

### Palette de Couleurs
| Couleur | Code Hex | Usage |
|---------|----------|-------|
| Bleu foncé | `#0f2847` | Fond login |
| Bleu moyen | `#1e3a5f` | Accents |
| Bleu clair | `#2c5282` | Dégradés |
| Orange | `#ed8936` | Accent principal, focus |
| Gris clair | `#f0f4f8` | Fond dashboard |
| Rouge | `#e53e3e` | Erreurs, danger |
| Vert | `#38a169` | Succès |

---

## 🖼️ Images

### Dossier images/
| Fichier | Description |
|---------|-------------|
| `icon.png` | Icône de l'application (optionnel) |

> Les images sont chargées dynamiquement dans `MainApp.java` :
> ```java
> primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
> ```

---

## 🔑 Comptes de Test

### Fichier motdepasse.md
| Rôle | Email | Mot de passe |
|------|-------|--------------|
| **Administrateur** | `admin@mondial2030.com` | `admin123` |
| **Spectateur** | `jean.dupont@email.com` | `test1234` |
| **Spectateur** | `salah@gmail.com` | `salah123` |

---

## 📊 Chargement des Ressources dans le Code

### Dans MainApp.java
```java
// Chargement FXML
FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));

// Chargement CSS
scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

// Chargement Image
primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
```

### Dans les Contrôleurs
```java
// Changement de vue
FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminDashboard.fxml"));
Parent root = loader.load();
```

---

## 🔄 Hot-Reload CSS (Développement)

Le projet utilise **CSSFX** pour le rechargement automatique des styles CSS en développement :

```java
// Dans MainApp.start()
fr.brouillard.oss.cssfx.CSSFX.start();
```

> **Avantage** : Modifiez `style.css` et voyez les changements instantanément sans redémarrer l'application.

---

## 📁 Ressources Compilées (target/classes)

Après compilation (`mvn compile`), les ressources sont copiées dans :

```
target/classes/
├── fxml/
│   ├── Login.fxml
│   ├── AdminDashboard.fxml
│   └── SpectateurDashboard.fxml
├── css/
│   └── style.css
├── hibernate.cfg.xml
├── application.properties
└── logback.xml
```

---

## 📝 Bonnes Pratiques Ressources

1. **FXML** : Utiliser Scene Builder pour l'édition visuelle
2. **CSS** : Organiser par sections (login, dashboard, composants)
3. **Images** : Formats PNG/SVG optimisés pour la taille
4. **Chemins** : Toujours utiliser des chemins absolus depuis la racine (`/fxml/...`)

---

*Document généré le 11 janvier 2026*
