package com.mondial2030.dao.interfaces;

import com.mondial2030.entity.Zone;
import com.mondial2030.entity.TypeZone;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour la gestion des zones.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public interface ZoneDAO extends GenericDAO<Zone, Long> {
    
    /**
     * Recherche une zone par son nom
     * @param nom Le nom de la zone
     * @return La zone trouvée ou empty
     */
    Optional<Zone> findByNom(String nom);
    
    /**
     * Recherche les zones par type
     * @param type Le type de zone
     * @return Liste des zones de ce type
     */
    List<Zone> findByType(TypeZone type);
    
    /**
     * Recherche les zones d'un match
     * @param matchId L'identifiant du match
     * @return Liste des zones du match
     */
    List<Zone> findByMatchId(Long matchId);
    
    /**
     * Recherche les zones accessibles PMR
     * @return Liste des zones PMR
     */
    List<Zone> findAccessiblesPMR();
    
    /**
     * Recherche les zones avec des places disponibles
     * @param matchId L'identifiant du match
     * @return Liste des zones avec places
     */
    List<Zone> findWithPlacesDisponibles(Long matchId);
    
    /**
     * Met à jour le nombre de places disponibles
     * @param zoneId L'identifiant de la zone
     * @param places Le nouveau nombre de places
     */
    void updatePlacesDisponibles(Long zoneId, int places);
    
    /**
     * Décrémente le nombre de places disponibles
     * @param zoneId L'identifiant de la zone
     */
    void decrementerPlaces(Long zoneId);
    
    /**
     * Incrémente le nombre de places disponibles
     * @param zoneId L'identifiant de la zone
     */
    void incrementerPlaces(Long zoneId);
    
    /**
     * Calcule le taux d'occupation d'une zone
     * @param zoneId L'identifiant de la zone
     * @return Le taux d'occupation
     */
    Double calculerTauxOccupation(Long zoneId);
    
    /**
     * Recherche les zones en surpopulation
     * @param seuilPourcentage Le seuil en pourcentage
     * @return Liste des zones en surpopulation
     */
    List<Zone> findZonesEnSurpopulation(double seuilPourcentage);
}
