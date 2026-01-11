# ⚙️ Projet et Fichiers de Configuration - Ticketing Mondial 2030


## � Table des Matières

- [Configuration Maven (pom.xml)](#-configuration-maven-pomxml)
  - [Informations du Projet](#informations-du-projet)
  - [Propriétés](#propriétés)
  - [Dépendances](#dépendances)
  - [Plugins Maven](#plugins-maven)
- [Configuration Hibernate (hibernate.cfg.xml)](#️-configuration-hibernate-aborneabornatecfgxml)
- [Configuration Application (application.properties)](#-configuration-application-applicationproperties)
- [Configuration Logging (logback.xml)](#-configuration-logging-logbackxml)
- [Commandes Maven](#-commandes-maven)
- [Structure des Fichiers Générés](#-structure-des-fichiers-générés)
- [Configuration pour Production](#-configuration-pour-production)
- [Récapitulatif des Technologies](#-récapitulatif-des-technologies)

---
## 📦 Configuration Maven (pom.xml)

### Informations du Projet
```xml
<groupId>com.mondial2030</groupId>
<artifactId>ticketing-mondial-2030</artifactId>
<version>1.0-SNAPSHOT</version>
<packaging>jar</packaging>
<name>Plateforme Intelligente de Gestion des Tickets - Mondial 2030</name>
```

### Propriétés
| Propriété | Valeur |
|-----------|--------|
| `project.build.sourceEncoding` | UTF-8 |
| `maven.compiler.source` | 17 |
| `maven.compiler.target` | 17 |
| `javafx.version` | 21.0.1 |
| `hibernate.version` | 6.4.1.Final |
| `sqlite.version` | 3.44.1.0 |

### Dépendances

#### JavaFX (Interface Utilisateur)
| Dépendance | Version | Description |
|------------|---------|-------------|
| `javafx-controls` | 21.0.1 | Composants UI (Button, TextField, etc.) |
| `javafx-fxml` | 21.0.1 | Support FXML |
| `javafx-graphics` | 21.0.1 | Rendu graphique |
| `javafx-swing` | 21.0.1 | Interopérabilité Swing (images) |

#### Persistance (Base de Données)
| Dépendance | Version | Description |
|------------|---------|-------------|
| `hibernate-core` | 6.4.1.Final | ORM Hibernate |
| `hibernate-community-dialects` | 6.4.1.Final | Dialect SQLite |
| `sqlite-jdbc` | 3.44.1.0 | Driver JDBC SQLite |
| `jakarta.persistence-api` | 3.1.0 | Annotations JPA |

#### Utilitaires
| Dépendance | Version | Description |
|------------|---------|-------------|
| `itext7-core` | 8.0.2 | Génération de PDF |
| `jbcrypt` | 0.4 | Hachage de mots de passe |
| `zxing-core` | 3.5.2 | Génération QR codes |
| `zxing-javase` | 3.5.2 | QR codes pour Java SE |
| `cssfx` | 11.5.1 | Hot-reload CSS (dev) |

#### Logging
| Dépendance | Version | Description |
|------------|---------|-------------|
| `slf4j-api` | 2.0.9 | API de logging |
| `slf4j-simple` | 2.0.9 | Implémentation simple |

### Plugins Maven

#### maven-compiler-plugin
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
        <source>17</source>
        <target>17</target>
    </configuration>
</plugin>
```

#### javafx-maven-plugin
```xml
<plugin>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-maven-plugin</artifactId>
    <version>0.0.8</version>
    <configuration>
        <mainClass>com.mondial2030.MainApp</mainClass>
    </configuration>
</plugin>
```

### Repository Externe
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

---

## 🗄️ Configuration Hibernate (hibernate.cfg.xml)

### Connexion Base de Données
| Propriété | Valeur |
|-----------|--------|
| `hibernate.connection.driver_class` | `org.sqlite.JDBC` |
| `hibernate.connection.url` | `jdbc:sqlite:mondial2030.db` |
| `hibernate.connection.username` | (vide - SQLite) |
| `hibernate.connection.password` | (vide - SQLite) |
| `hibernate.dialect` | `org.hibernate.community.dialect.SQLiteDialect` |

### Pool de Connexions
| Propriété | Valeur |
|-----------|--------|
| `hibernate.connection.pool_size` | 10 |

### Génération du Schéma
| Propriété | Valeur | Description |
|-----------|--------|-------------|
| `hibernate.hbm2ddl.auto` | `update` | Mise à jour automatique du schéma |

### Debugging SQL
| Propriété | Valeur | Recommandation |
|-----------|--------|----------------|
| `hibernate.show_sql` | `true` | Désactiver en production |
| `hibernate.format_sql` | `true` | Désactiver en production |
| `hibernate.use_sql_comments` | `true` | Désactiver en production |

### Cache
| Propriété | Valeur |
|-----------|--------|
| `hibernate.cache.use_second_level_cache` | `false` |
| `hibernate.cache.use_query_cache` | `false` |

### Sessions
| Propriété | Valeur |
|-----------|--------|
| `hibernate.current_session_context_class` | `thread` |
| `hibernate.id.new_generator_mappings` | `true` |

### Mapping des Entités
```xml
<mapping class="com.mondial2030.entity.Utilisateur"/>
<mapping class="com.mondial2030.entity.Administrateur"/>
<mapping class="com.mondial2030.entity.Spectateur"/>
<mapping class="com.mondial2030.entity.Equipe"/>
<mapping class="com.mondial2030.entity.Match"/>
<mapping class="com.mondial2030.entity.Zone"/>
<mapping class="com.mondial2030.entity.Siege"/>
<mapping class="com.mondial2030.entity.Ticket"/>
<mapping class="com.mondial2030.entity.Transaction"/>
<mapping class="com.mondial2030.entity.Rapport"/>
<mapping class="com.mondial2030.entity.Alerte"/>
<mapping class="com.mondial2030.entity.FluxSpectateurs"/>
<mapping class="com.mondial2030.entity.OptimisateurFlux"/>
```

---

## 📝 Configuration Application (application.properties)

### Section Application
| Propriété | Valeur |
|-----------|--------|
| `app.name` | Plateforme Intelligente de Gestion des Tickets - Mondial 2030 |
| `app.version` | 1.0.0 |
| `app.author` | Mondial 2030 Team |

### Section Base de Données
| Propriété | Valeur | Description |
|-----------|--------|-------------|
| `db.name` | mondial2030.db | Nom du fichier SQLite |
| `db.backup.enabled` | true | Activation des backups |
| `db.backup.path` | ./backups/ | Chemin des sauvegardes |

### Section Sécurité
| Propriété | Valeur | Description |
|-----------|--------|-------------|
| `security.password.min_length` | 8 | Longueur minimale mot de passe |
| `security.session.timeout` | 3600 | Timeout session (secondes) |
| `security.max_login_attempts` | 5 | Tentatives max avant blocage |

### Section Tickets
| Propriété | Valeur | Description |
|-----------|--------|-------------|
| `ticket.transfer.enabled` | true | Transfert activé |
| `ticket.transfer.max_per_match` | 2 | Max transferts par match |
| `ticket.qrcode.size` | 200 | Taille QR code (pixels) |

### Section Alertes
| Propriété | Valeur | Description |
|-----------|--------|-------------|
| `alert.surpopulation.seuil` | 85 | Seuil d'alerte (%) |
| `alert.surpopulation.critique` | 95 | Seuil critique (%) |
| `alert.notification.enabled` | true | Notifications activées |

### Section Flux
| Propriété | Valeur | Description |
|-----------|--------|-------------|
| `flux.update.interval` | 5000 | Intervalle mise à jour (ms) |
| `flux.evacuation.taux` | 100 | Taux d'évacuation |

### Section Rapports
| Propriété | Valeur | Description |
|-----------|--------|-------------|
| `rapport.export.path` | ./rapports/ | Chemin d'export |
| `rapport.pdf.enabled` | true | Export PDF activé |

### Section JavaFX
| Propriété | Valeur |
|-----------|--------|
| `javafx.theme` | modern |
| `javafx.language` | fr |

### Section Logging
| Propriété | Valeur |
|-----------|--------|
| `logging.level` | INFO |
| `logging.file` | ./logs/mondial2030.log |

---

## 📋 Configuration Logging (logback.xml)

### Appenders Configurés

#### Console Appender
```xml
<appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
        <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
    </encoder>
</appender>
```

#### File Appender (Rolling)
```xml
<appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>logs/mondial2030.log</file>
    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
        <fileNamePattern>logs/mondial2030.%d{yyyy-MM-dd}.log</fileNamePattern>
        <maxHistory>30</maxHistory>
    </rollingPolicy>
    <encoder>
        <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
    </encoder>
</appender>
```

### Niveaux de Log par Package
| Logger | Niveau | Description |
|--------|--------|-------------|
| `org.hibernate` | WARN | Réduit la verbosité Hibernate |
| `org.hibernate.SQL` | DEBUG | Affiche les requêtes SQL |
| `org.hibernate.type.descriptor.sql` | TRACE | Affiche les paramètres SQL |
| `org.sqlite` | WARN | Réduit la verbosité SQLite |
| `com.mondial2030` | DEBUG | Logs détaillés pour l'application |
| Root | INFO | Niveau par défaut |

---

## 🚀 Commandes Maven

### Compilation
```bash
# Nettoyer et compiler
mvn clean compile

# Compiler sans nettoyer
mvn compile
```

### Exécution
```bash
# Lancer l'application JavaFX
mvn javafx:run
```

### Packaging
```bash
# Créer le JAR
mvn clean package

# Exécuter le JAR
java -jar target/ticketing-mondial-2030-1.0-SNAPSHOT.jar
```

### Autres Commandes Utiles
```bash
# Vérifier les dépendances
mvn dependency:tree

# Mettre à jour les dépendances
mvn versions:display-dependency-updates

# Nettoyer le projet
mvn clean
```

---

## 📁 Structure des Fichiers Générés

```
ticketing-mondial-2030/
├── mondial2030.db          # Base de données SQLite (généré au runtime)
├── logs/
│   └── mondial2030.log     # Fichiers de log
├── rapports/               # Rapports PDF exportés
├── backups/                # Sauvegardes base de données
└── target/
    ├── classes/            # Classes compilées
    └── *.jar               # JAR exécutable (après package)
```

---

## 🔧 Configuration pour Production

### Modifications Recommandées

#### hibernate.cfg.xml
```xml
<!-- Désactiver l'affichage SQL -->
<property name="hibernate.show_sql">false</property>
<property name="hibernate.format_sql">false</property>
<property name="hibernate.use_sql_comments">false</property>

<!-- Changer le mode DDL -->
<property name="hibernate.hbm2ddl.auto">validate</property>
```

#### logback.xml
```xml
<!-- Réduire le niveau de log -->
<logger name="com.mondial2030" level="INFO"/>
<logger name="org.hibernate.SQL" level="WARN"/>
```

#### application.properties
```properties
# Ajuster les paramètres
logging.level=WARN
security.session.timeout=1800
```

---

## 📊 Récapitulatif des Technologies

| Catégorie | Technologie | Version |
|-----------|-------------|---------|
| **Langage** | Java | 17 |
| **UI Framework** | JavaFX | 21.0.1 |
| **Build Tool** | Maven | 3.x |
| **ORM** | Hibernate | 6.4.1.Final |
| **Base de données** | SQLite | 3.44.1.0 |
| **Sécurité** | BCrypt | 0.4 |
| **QR Code** | ZXing | 3.5.2 |
| **PDF** | iText | 8.0.2 |
| **Logging** | SLF4J + Logback | 2.0.9 |

---

