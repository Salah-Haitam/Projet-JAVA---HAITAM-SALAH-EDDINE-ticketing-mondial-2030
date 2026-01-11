package com.mondial2030;

import com.mondial2030.util.HibernateUtil;
import com.mondial2030.util.DataInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe principale de l'application Mondial 2030.
 * Point d'entrée JavaFX pour la plateforme de gestion des tickets.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class MainApp extends Application {
    
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);
    private static final String APP_TITLE = "Plateforme de Gestion des Tickets - Mondial 2030";
    
    @Override
    public void init() throws Exception {
        super.init();
        logger.info("Initialisation de l'application Mondial 2030...");
        
        // Initialiser Hibernate et créer le schéma
        HibernateUtil.getSessionFactory();
        logger.info("Base de données initialisée avec succès.");
        
        // Initialiser les données de démonstration
        DataInitializer.initializeData();
        logger.info("Données de démonstration chargées.");
    }
    
    @Override
    public void start(Stage primaryStage) {
        try {
            logger.info("Démarrage de l'interface utilisateur...");
            
            // Charger la vue de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            
            // Créer la scène
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            
            // Configurer la fenêtre principale
            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.centerOnScreen();
            
            // Icône de l'application (si disponible)
            try {
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
            } catch (Exception e) {
                logger.debug("Icône de l'application non trouvée.");
            }
            
            primaryStage.show();
            
            // Activer le rechargement CSS en temps réel (modifiez style.css et voyez les changements instantanément)
            fr.brouillard.oss.cssfx.CSSFX.start();
            
            logger.info("Application démarrée avec succès.");
            
        } catch (Exception e) {
            logger.error("Erreur lors du démarrage de l'application", e);
            throw new RuntimeException("Impossible de démarrer l'application", e);
        }
    }
    
    @Override
    public void stop() throws Exception {
        super.stop();
        logger.info("Arrêt de l'application...");
        
        // Fermer la connexion Hibernate
        HibernateUtil.shutdown();
        
        logger.info("Application arrêtée proprement.");
    }
    
    /**
     * Point d'entrée principal de l'application.
     * 
     * @param args Arguments de ligne de commande
     */
    public static void main(String[] args) {
        logger.info("========================================");
        logger.info("  MONDIAL 2030 - Ticketing Platform");
        logger.info("========================================");
        
        launch(args);
    }
}
