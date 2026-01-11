package com.mondial2030.dao.interfaces;

import com.mondial2030.entity.Siege;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour la gestion des sièges.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public interface SiegeDAO extends GenericDAO<Siege, Long> {
    
    /**
     * Recherche un siège par son numéro dans une zone
     * @param numeroSiege Le numéro du siège
     * @param zoneId L'identifiant de la zone
     * @return Le siège trouvé ou empty
     */
    Optional<Siege> findByNumeroAndZone(String numeroSiege, Long zoneId);
    
    /**
     * Recherche les sièges d'une zone
     * @param zoneId L'identifiant de la zone
     * @return Liste des sièges de la zone
     */
    List<Siege> findByZoneId(Long zoneId);
    
    /**
     * Recherche les sièges disponibles d'une zone
     * @param zoneId L'identifiant de la zone
     * @return Liste des sièges disponibles
     */
    List<Siege> findDisponiblesByZoneId(Long zoneId);
    
    /**
     * Recherche les sièges par rangée
     * @param zoneId L'identifiant de la zone
     * @param rangee La rangée
     * @return Liste des sièges de la rangée
     */
    List<Siege> findByRangee(Long zoneId, String rangee);
    
    /**
     * Recherche les sièges PMR
     * @param zoneId L'identifiant de la zone
     * @return Liste des sièges PMR
     */
    List<Siege> findSiegesPMR(Long zoneId);
    
    /**
     * Réserve un siège
     * @param siegeId L'identifiant du siège
     */
    void reserver(Long siegeId);
    
    /**
     * Libère un siège
     * @param siegeId L'identifiant du siège
     */
    void liberer(Long siegeId);
    
    /**
     * Compte le nombre de sièges disponibles dans une zone
     * @param zoneId L'identifiant de la zone
     * @return Le nombre de sièges disponibles
     */
    long countDisponibles(Long zoneId);
    
    /**
     * Vérifie si un siège est disponible
     * @param siegeId L'identifiant du siège
     * @return true si disponible
     */
    boolean isDisponible(Long siegeId);
}
