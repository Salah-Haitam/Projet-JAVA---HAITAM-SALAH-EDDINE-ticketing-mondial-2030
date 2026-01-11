package com.mondial2030.util;

import com.mondial2030.dao.impl.*;
import com.mondial2030.entity.*;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Initialiseur de données de démonstration pour l'application.
 * Crée des données de test pour tester l'application.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class DataInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    private static final AdministrateurDAOImpl adminDAO = new AdministrateurDAOImpl();
    private static final SpectateurDAOImpl spectateurDAO = new SpectateurDAOImpl();
    private static final EquipeDAOImpl equipeDAO = new EquipeDAOImpl();
    private static final MatchDAOImpl matchDAO = new MatchDAOImpl();
    private static final ZoneDAOImpl zoneDAO = new ZoneDAOImpl();
    
    /**
     * Initialise les données de démonstration si la base est vide
     */
    public static void initializeData() {
        // Mettre à jour le schéma de la table alerte pour les nouvelles colonnes
        updateAlerteSchema();
        
        // Corriger les matchs existants qui n'ont pas de tickets disponibles
        fixExistingMatches();
        
        // Vérifier si des données existent déjà
        if (!adminDAO.findAll().isEmpty()) {
            logger.info("Données déjà présentes, initialisation ignorée.");
            return;
        }
        
        logger.info("Initialisation des données de démonstration...");
        
        try {
            // 1. Créer l'administrateur par défaut
            createDefaultAdmin();
            
            // 2. Créer un spectateur de test
            createTestSpectateur();
            
            // 3. Créer les équipes
            List<Equipe> equipes = createEquipes();
            
            // 4. Créer les matchs avec zones
            createMatchsWithZones(equipes);
            
            logger.info("Données de démonstration créées avec succès.");
            
        } catch (Exception e) {
            logger.error("Erreur lors de l'initialisation des données", e);
        }
    }
    
    /**
     * Met à jour le schéma de la table alerte pour ajouter les nouvelles colonnes
     */
    private static void updateAlerteSchema() {
        org.hibernate.Session session = null;
        org.hibernate.Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            
            // D'abord, supprimer la contrainte CHECK en recréant la table
            // SQLite ne permet pas de supprimer les contraintes directement
            try {
                // Vérifier si la table existe et recréer sans contrainte CHECK
                String recreateTable = """
                    CREATE TABLE IF NOT EXISTS alerte_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        titre VARCHAR(255),
                        message TEXT,
                        type_alerte VARCHAR(50),
                        niveau VARCHAR(20),
                        source VARCHAR(255),
                        date_creation TIMESTAMP,
                        resolue BOOLEAN DEFAULT 0,
                        date_resolution TIMESTAMP,
                        commentaire_resolution VARCHAR(500),
                        match_id BIGINT,
                        zone_id BIGINT,
                        resolue_par_id BIGINT,
                        creee_par_id BIGINT,
                        reponse_admin TEXT,
                        lue_par_admin BOOLEAN DEFAULT 0,
                        date_lecture TIMESTAMP,
                        date_reponse TIMESTAMP,
                        repondu_par_id BIGINT
                    )
                    """;
                
                // Vérifier si la contrainte CHECK pose problème
                try {
                    // Essayer d'insérer un type utilisateur pour voir si la contrainte existe
                    session.createNativeQuery("INSERT INTO alerte (titre, message, type_alerte, niveau, date_creation, resolue) VALUES ('test', 'test', 'QUESTION_UTILISATEUR', 'MOYEN', datetime('now'), 0)", Object.class).executeUpdate();
                    // Si ça marche, supprimer le test et continuer
                    session.createNativeQuery("DELETE FROM alerte WHERE titre = 'test' AND message = 'test'", Object.class).executeUpdate();
                    logger.info("La contrainte CHECK a déjà été supprimée.");
                } catch (Exception checkError) {
                    // La contrainte existe, il faut recréer la table
                    logger.info("Suppression de la contrainte CHECK sur type_alerte...");
                    
                    // Copier les données dans une nouvelle table sans contrainte
                    session.createNativeQuery(recreateTable, Object.class).executeUpdate();
                    
                    // Copier les anciennes données
                    session.createNativeQuery("""
                        INSERT INTO alerte_new (id, titre, message, type_alerte, niveau, source, date_creation, resolue, 
                            date_resolution, commentaire_resolution, match_id, zone_id, resolue_par_id, 
                            creee_par_id, reponse_admin, lue_par_admin, date_lecture, date_reponse, repondu_par_id)
                        SELECT id, titre, message, type_alerte, niveau, source, date_creation, resolue, 
                            date_resolution, commentaire_resolution, match_id, zone_id, resolue_par_id,
                            creee_par_id, reponse_admin, lue_par_admin, date_lecture, date_reponse, repondu_par_id
                        FROM alerte
                        """, Object.class).executeUpdate();
                    
                    // Supprimer l'ancienne table
                    session.createNativeQuery("DROP TABLE alerte", Object.class).executeUpdate();
                    
                    // Renommer la nouvelle table
                    session.createNativeQuery("ALTER TABLE alerte_new RENAME TO alerte", Object.class).executeUpdate();
                    
                    logger.info("Table alerte recréée sans contrainte CHECK.");
                }
            } catch (Exception e) {
                logger.debug("Mise à jour table alerte: " + e.getMessage());
            }
            
            // Ajouter les colonnes si elles n'existent pas
            String[] columns = {
                "ALTER TABLE alerte ADD COLUMN creee_par_id BIGINT",
                "ALTER TABLE alerte ADD COLUMN reponse_admin TEXT",
                "ALTER TABLE alerte ADD COLUMN lue_par_admin BOOLEAN DEFAULT 0",
                "ALTER TABLE alerte ADD COLUMN date_lecture TIMESTAMP",
                "ALTER TABLE alerte ADD COLUMN date_reponse TIMESTAMP",
                "ALTER TABLE alerte ADD COLUMN repondu_par_id BIGINT"
            };
            
            for (String sql : columns) {
                try {
                    session.createNativeQuery(sql, Object.class).executeUpdate();
                    logger.info("Colonne ajoutée: " + sql);
                } catch (Exception e) {
                    // La colonne existe probablement déjà - c'est normal
                    logger.debug("Colonne existante ou erreur ignorée: " + e.getMessage());
                }
            }
            
            tx.commit();
            logger.info("Schéma de la table alerte mis à jour.");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.warn("Mise à jour du schéma alerte: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }
    
    /**
     * Corrige les matchs existants qui n'ont pas de tickets disponibles
     */
    private static void fixExistingMatches() {
        try {
            List<Match> matchs = matchDAO.findAll();
            for (Match match : matchs) {
                boolean needsUpdate = false;
                
                if (match.getTicketsDisponibles() == null) {
                    int capacite = match.getCapaciteStade() != null ? match.getCapaciteStade() : 72500;
                    match.setTicketsDisponibles(capacite);
                    needsUpdate = true;
                    logger.info("Correction ticketsDisponibles pour match " + match.getId());
                }
                
                if (match.getCapaciteStade() == null) {
                    match.setCapaciteStade(72500);
                    needsUpdate = true;
                }
                
                if (match.getPrixBase() == null) {
                    match.setPrixBase(100.0);
                    needsUpdate = true;
                }
                
                if (needsUpdate) {
                    matchDAO.update(match);
                }
            }
        } catch (Exception e) {
            logger.warn("Erreur lors de la correction des matchs: " + e.getMessage());
        }
    }
    
    private static void createDefaultAdmin() {
        Administrateur admin = new Administrateur();
        admin.setNom("Admin");
        admin.setPrenom("System");
        admin.setEmail("admin@mondial2030.com");
        admin.setMotDePasse("admin123");
        admin.setTelephone("+33 1 23 45 67 89");
        admin.setNiveauAcces(3);
        admin.setDepartement("Direction");
        admin.setActif(true);
        
        adminDAO.save(admin);
        logger.info("Administrateur par défaut créé: admin@mondial2030.com / admin123");
    }
    
    private static void createTestSpectateur() {
        Spectateur spectateur = new Spectateur();
        spectateur.setNom("Dupont");
        spectateur.setPrenom("Jean");
        spectateur.setEmail("jean.dupont@email.com");
        spectateur.setMotDePasse("test1234");
        spectateur.setTelephone("+33 6 12 34 56 78");
        spectateur.setNationalite("France");
        spectateur.setActif(true);
        
        spectateurDAO.save(spectateur);
        logger.info("Spectateur de test créé: jean.dupont@email.com / test1234");
    }
    
    private static List<Equipe> createEquipes() {
        List<Equipe> equipes = new ArrayList<>();
        
        // Équipes du groupe A
        equipes.add(createEquipe("Maroc", "MAR", "Afrique", "A"));
        equipes.add(createEquipe("Espagne", "ESP", "Europe", "A"));
        equipes.add(createEquipe("Portugal", "POR", "Europe", "A"));
        equipes.add(createEquipe("Argentine", "ARG", "Amérique du Sud", "A"));
        
        // Équipes du groupe B
        equipes.add(createEquipe("France", "FRA", "Europe", "B"));
        equipes.add(createEquipe("Brésil", "BRA", "Amérique du Sud", "B"));
        equipes.add(createEquipe("Allemagne", "GER", "Europe", "B"));
        equipes.add(createEquipe("Japon", "JPN", "Asie", "B"));
        
        // Équipes du groupe C
        equipes.add(createEquipe("Angleterre", "ENG", "Europe", "C"));
        equipes.add(createEquipe("Pays-Bas", "NED", "Europe", "C"));
        equipes.add(createEquipe("Belgique", "BEL", "Europe", "C"));
        equipes.add(createEquipe("États-Unis", "USA", "Amérique du Nord", "C"));
        
        return equipes;
    }
    
    private static Equipe createEquipe(String nom, String code, String confederation, String groupe) {
        Equipe equipe = new Equipe();
        equipe.setNom(nom);
        equipe.setCodePays(code);
        equipe.setConfederation(confederation);
        equipe.setGroupe(groupe);
        equipe.setClassementFifa((int) (Math.random() * 50) + 1);
        
        equipeDAO.save(equipe);
        return equipe;
    }
    
    private static void createMatchsWithZones(List<Equipe> equipes) {
        // Match 1 - Phase de groupes
        Match match1 = createMatch(
                equipes.get(0), equipes.get(1), // Maroc vs Espagne
                LocalDateTime.now().plusDays(30).withHour(18).withMinute(0),
                "Stade Mohammed V", "Casablanca", "Maroc",
                PhaseMatch.PHASE_GROUPES, "A"
        );
        createZonesForMatch(match1);
        
        // Match 2 - Phase de groupes
        Match match2 = createMatch(
                equipes.get(2), equipes.get(3), // Portugal vs Argentine
                LocalDateTime.now().plusDays(31).withHour(21).withMinute(0),
                "Estadio Santiago Bernabéu", "Madrid", "Espagne",
                PhaseMatch.PHASE_GROUPES, "A"
        );
        createZonesForMatch(match2);
        
        // Match 3 - Phase de groupes
        Match match3 = createMatch(
                equipes.get(4), equipes.get(5), // France vs Brésil
                LocalDateTime.now().plusDays(32).withHour(20).withMinute(0),
                "Estádio da Luz", "Lisbonne", "Portugal",
                PhaseMatch.PHASE_GROUPES, "B"
        );
        createZonesForMatch(match3);
        
        // Match 4 - Phase de groupes
        Match match4 = createMatch(
                equipes.get(6), equipes.get(7), // Allemagne vs Japon
                LocalDateTime.now().plusDays(33).withHour(15).withMinute(0),
                "Stade Moulay Abdellah", "Rabat", "Maroc",
                PhaseMatch.PHASE_GROUPES, "B"
        );
        createZonesForMatch(match4);
        
        // Match 5 - Huitièmes de finale
        Match match5 = createMatch(
                equipes.get(8), equipes.get(9), // Angleterre vs Pays-Bas
                LocalDateTime.now().plusDays(45).withHour(18).withMinute(0),
                "Camp Nou", "Barcelone", "Espagne",
                PhaseMatch.HUITIEMES, null
        );
        createZonesForMatch(match5);
        
        // Match 6 - Quart de finale
        Match match6 = createMatch(
                equipes.get(0), equipes.get(4), // Maroc vs France (exemple)
                LocalDateTime.now().plusDays(50).withHour(21).withMinute(0),
                "Grand Stade de Casablanca", "Casablanca", "Maroc",
                PhaseMatch.QUARTS, null
        );
        createZonesForMatch(match6);
        
        // Match 7 - Demi-finale
        Match match7 = createMatch(
                equipes.get(1), equipes.get(5), // Espagne vs Brésil (exemple)
                LocalDateTime.now().plusDays(55).withHour(21).withMinute(0),
                "Estadio Metropolitano", "Madrid", "Espagne",
                PhaseMatch.DEMI_FINALES, null
        );
        createZonesForMatch(match7);
        
        // Finale
        Match finale = createMatch(
                equipes.get(0), equipes.get(4), // Maroc vs France (exemple)
                LocalDateTime.now().plusDays(60).withHour(18).withMinute(0),
                "Grand Stade de Casablanca", "Casablanca", "Maroc",
                PhaseMatch.FINALE, null
        );
        createZonesForMatch(finale);
    }
    
    private static Match createMatch(Equipe domicile, Equipe exterieur, LocalDateTime dateHeure,
                                     String stade, String ville, String pays,
                                     PhaseMatch phase, String groupe) {
        Match match = new Match();
        match.setEquipeDomicile(domicile);
        match.setEquipeExterieur(exterieur);
        match.setDateHeure(dateHeure);
        match.setStade(stade);
        match.setVille(ville);
        match.setPays(pays);
        match.setPhase(phase);
        match.setGroupe(groupe);
        match.setTermine(false);
        
        // Initialiser la capacité et les tickets disponibles
        int capacite = 72500; // Capacité par défaut des stades de la Coupe du Monde
        match.setCapaciteStade(capacite);
        match.setTicketsDisponibles(capacite);
        match.setPrixBase(100.0); // Prix de base par défaut
        
        matchDAO.save(match);
        return match;
    }
    
    private static void createZonesForMatch(Match match) {
        // Zone VIP
        Zone vip = new Zone();
        vip.setNom("VIP Premium");
        vip.setTypeZone(TypeZone.VIP);
        vip.setCapacite(500);
        vip.setCoefficientPrix(3.0);
        vip.setAccessiblePmr(true);
        vip.setMatch(match);
        zoneDAO.save(vip);
        
        // Tribune Nord
        Zone nord = new Zone();
        nord.setNom("Tribune Nord");
        nord.setTypeZone(TypeZone.TRIBUNE);
        nord.setCapacite(15000);
        nord.setCoefficientPrix(1.5);
        nord.setAccessiblePmr(true);
        nord.setMatch(match);
        zoneDAO.save(nord);
        
        // Tribune Sud
        Zone sud = new Zone();
        sud.setNom("Tribune Sud");
        sud.setTypeZone(TypeZone.TRIBUNE);
        sud.setCapacite(15000);
        sud.setCoefficientPrix(1.5);
        sud.setAccessiblePmr(true);
        sud.setMatch(match);
        zoneDAO.save(sud);
        
        // Tribune Est
        Zone est = new Zone();
        est.setNom("Tribune Est");
        est.setTypeZone(TypeZone.TRIBUNE);
        est.setCapacite(12000);
        est.setCoefficientPrix(1.2);
        est.setAccessiblePmr(false);
        est.setMatch(match);
        zoneDAO.save(est);
        
        // Tribune Ouest
        Zone ouest = new Zone();
        ouest.setNom("Tribune Ouest");
        ouest.setTypeZone(TypeZone.TRIBUNE);
        ouest.setCapacite(12000);
        ouest.setCoefficientPrix(1.2);
        ouest.setAccessiblePmr(false);
        ouest.setMatch(match);
        zoneDAO.save(ouest);
        
        // Virages
        Zone virageNord = new Zone();
        virageNord.setNom("Virage Nord");
        virageNord.setTypeZone(TypeZone.VIRAGE);
        virageNord.setCapacite(8000);
        virageNord.setCoefficientPrix(0.8);
        virageNord.setAccessiblePmr(false);
        virageNord.setMatch(match);
        zoneDAO.save(virageNord);
        
        Zone virageSud = new Zone();
        virageSud.setNom("Virage Sud");
        virageSud.setTypeZone(TypeZone.VIRAGE);
        virageSud.setCapacite(8000);
        virageSud.setCoefficientPrix(0.8);
        virageSud.setAccessiblePmr(false);
        virageSud.setMatch(match);
        zoneDAO.save(virageSud);
        
        // Zone Pelouse (pour les familles)
        Zone pelouse = new Zone();
        pelouse.setNom("Espace Famille");
        pelouse.setTypeZone(TypeZone.PELOUSE);
        pelouse.setCapacite(2000);
        pelouse.setCoefficientPrix(2.0);
        pelouse.setAccessiblePmr(true);
        pelouse.setMatch(match);
        zoneDAO.save(pelouse);
    }
}
