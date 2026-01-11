package com.mondial2030.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilitaire Hibernate pour la gestion des sessions et de la SessionFactory.
 * Implémente le pattern Singleton pour garantir une seule instance.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class HibernateUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static SessionFactory sessionFactory;
    private static StandardServiceRegistry registry;
    
    private HibernateUtil() {
        // Constructeur privé pour empêcher l'instanciation
    }
    
    /**
     * Récupère l'instance unique de SessionFactory.
     * Crée la SessionFactory si elle n'existe pas.
     * 
     * @return SessionFactory configurée pour SQLite
     */
    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                logger.info("Initialisation de la SessionFactory Hibernate...");
                
                // Création du registre de services
                registry = new StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml")
                        .build();
                
                // Création des métadonnées
                MetadataSources sources = new MetadataSources(registry);
                Metadata metadata = sources.getMetadataBuilder().build();
                
                // Création de la SessionFactory
                sessionFactory = metadata.getSessionFactoryBuilder().build();
                
                logger.info("SessionFactory initialisée avec succès.");
                
            } catch (Exception e) {
                logger.error("Erreur lors de l'initialisation de la SessionFactory", e);
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
                throw new ExceptionInInitializerError(e);
            }
        }
        return sessionFactory;
    }
    
    /**
     * Ferme la SessionFactory et libère les ressources.
     * Doit être appelée à la fermeture de l'application.
     */
    public static synchronized void shutdown() {
        logger.info("Fermeture de la SessionFactory...");
        
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            sessionFactory = null;
        }
        
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
            registry = null;
        }
        
        logger.info("SessionFactory fermée avec succès.");
    }
    
    /**
     * Vérifie si la SessionFactory est initialisée et ouverte.
     * 
     * @return true si la SessionFactory est disponible
     */
    public static boolean isInitialized() {
        return sessionFactory != null && !sessionFactory.isClosed();
    }
    
    /**
     * Réinitialise la SessionFactory.
     * Utile pour les tests ou après une erreur critique.
     */
    public static synchronized void reinitialize() {
        shutdown();
        getSessionFactory();
    }
}
