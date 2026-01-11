package com.mondial2030.dao.interfaces;

import com.mondial2030.entity.FluxSpectateurs;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface DAO pour la gestion des flux de spectateurs.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public interface FluxSpectateurDAO extends GenericDAO<FluxSpectateurs, Long> {
    
    /**
     * Recherche les flux d'un match
     * @param matchId L'identifiant du match
     * @return Liste des flux du match
     */
    List<FluxSpectateurs> findByMatchId(Long matchId);
    
    /**
     * Recherche les flux d'une zone
     * @param zoneId L'identifiant de la zone
     * @return Liste des flux de la zone
     */
    List<FluxSpectateurs> findByZoneId(Long zoneId);
    
    /**
     * Recherche les flux d'un match et d'une zone
     * @param matchId L'identifiant du match
     * @param zoneId L'identifiant de la zone
     * @return Liste des flux
     */
    List<FluxSpectateurs> findByMatchAndZone(Long matchId, Long zoneId);
    
    /**
     * Recherche les flux dans une plage horaire
     * @param debut Horodatage de début
     * @param fin Horodatage de fin
     * @return Liste des flux dans cette période
     */
    List<FluxSpectateurs> findByHorodatageRange(LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Recherche les flux en surpopulation
     * @param seuilDensite Le seuil de densité
     * @return Liste des flux en surpopulation
     */
    List<FluxSpectateurs> findEnSurpopulation(double seuilDensite);
    
    /**
     * Calcule l'occupation totale d'un match
     * @param matchId L'identifiant du match
     * @return L'occupation totale
     */
    Integer calculerOccupationTotale(Long matchId);
    
    /**
     * Récupère le dernier flux d'une zone
     * @param zoneId L'identifiant de la zone
     * @return Le dernier flux enregistré
     */
    FluxSpectateurs findDernierFluxByZone(Long zoneId);
    
    /**
     * Calcule la densité moyenne d'un match
     * @param matchId L'identifiant du match
     * @return La densité moyenne
     */
    Double calculerDensiteMoyenne(Long matchId);
    
    /**
     * Enregistre une entrée pour une zone
     * @param matchId L'identifiant du match
     * @param zoneId L'identifiant de la zone
     */
    void enregistrerEntree(Long matchId, Long zoneId);
    
    /**
     * Enregistre une sortie pour une zone
     * @param matchId L'identifiant du match
     * @param zoneId L'identifiant de la zone
     */
    void enregistrerSortie(Long matchId, Long zoneId);
}
