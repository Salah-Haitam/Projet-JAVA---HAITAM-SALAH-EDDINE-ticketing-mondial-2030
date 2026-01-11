package com.mondial2030.dao.interfaces;

import com.mondial2030.entity.Alerte;
import com.mondial2030.entity.TypeAlerte;
import com.mondial2030.entity.NiveauAlerte;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour la gestion des alertes.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public interface AlerteDAO extends GenericDAO<Alerte, Long> {
    
    /**
     * Recherche les alertes par type
     * @param type Le type d'alerte
     * @return Liste des alertes de ce type
     */
    List<Alerte> findByType(TypeAlerte type);
    
    /**
     * Recherche les alertes par niveau
     * @param niveau Le niveau d'alerte
     * @return Liste des alertes de ce niveau
     */
    List<Alerte> findByNiveau(NiveauAlerte niveau);
    
    /**
     * Recherche les alertes non résolues
     * @return Liste des alertes actives
     */
    List<Alerte> findNonResolues();
    
    /**
     * Recherche les alertes résolues
     * @return Liste des alertes résolues
     */
    List<Alerte> findResolues();
    
    /**
     * Recherche les alertes d'un match
     * @param matchId L'identifiant du match
     * @return Liste des alertes du match
     */
    List<Alerte> findByMatchId(Long matchId);
    
    /**
     * Recherche les alertes d'une zone
     * @param zoneId L'identifiant de la zone
     * @return Liste des alertes de la zone
     */
    List<Alerte> findByZoneId(Long zoneId);
    
    /**
     * Recherche les alertes critiques non résolues
     * @return Liste des alertes critiques actives
     */
    List<Alerte> findCritiquesNonResolues();
    
    /**
     * Recherche les alertes dans une plage de dates
     * @param debut Date de début
     * @param fin Date de fin
     * @return Liste des alertes dans cette période
     */
    List<Alerte> findByDateRange(LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Résout une alerte
     * @param alerteId L'identifiant de l'alerte
     * @param adminId L'identifiant de l'admin qui résout
     * @param commentaire Le commentaire de résolution
     */
    void resoudre(Long alerteId, Long adminId, String commentaire);
    
    /**
     * Compte le nombre d'alertes non résolues
     * @return Le nombre d'alertes actives
     */
    long countNonResolues();
    
    /**
     * Compte le nombre d'alertes par niveau
     * @param niveau Le niveau
     * @return Le nombre d'alertes
     */
    long countByNiveau(NiveauAlerte niveau);
    
    /**
     * Recherche les dernières alertes
     * @param limite Nombre maximum d'alertes
     * @return Liste des dernières alertes
     */
    List<Alerte> findDernieresAlertes(int limite);
    
    /**
     * Recherche les alertes créées par un spectateur
     * @param spectateurId L'identifiant du spectateur
     * @return Liste des alertes du spectateur
     */
    List<Alerte> findBySpectateurId(Long spectateurId);
    
    /**
     * Recherche les alertes utilisateurs non lues
     * @return Liste des alertes utilisateurs non lues
     */
    List<Alerte> findAlertesUtilisateursNonLues();
    
    /**
     * Recherche toutes les alertes créées par des utilisateurs
     * @return Liste des alertes utilisateurs
     */
    List<Alerte> findAlertesUtilisateurs();
    
    /**
     * Marque une alerte comme lue
     * @param alerteId L'identifiant de l'alerte
     * @param adminId L'identifiant de l'admin qui lit
     */
    void marquerCommeLue(Long alerteId, Long adminId);
    
    /**
     * Ajoute une réponse à une alerte utilisateur
     * @param alerteId L'identifiant de l'alerte
     * @param adminId L'identifiant de l'admin qui répond
     * @param reponse La réponse de l'administrateur
     */
    void repondre(Long alerteId, Long adminId, String reponse);
    
    /**
     * Compte le nombre d'alertes utilisateurs non lues
     * @return Le nombre d'alertes non lues
     */
    long countAlertesUtilisateursNonLues();
}
