package com.mondial2030.dao.interfaces;

import com.mondial2030.entity.OptimisateurFlux;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour la gestion de l'optimisateur de flux.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public interface OptimisateurFluxDAO extends GenericDAO<OptimisateurFlux, Long> {
    
    /**
     * Recherche les analyses d'un match
     * @param matchId L'identifiant du match
     * @return Liste des analyses du match
     */
    List<OptimisateurFlux> findByMatchId(Long matchId);
    
    /**
     * Recherche la dernière analyse d'un match
     * @param matchId L'identifiant du match
     * @return La dernière analyse ou empty
     */
    Optional<OptimisateurFlux> findDerniereAnalyse(Long matchId);
    
    /**
     * Recherche les analyses par statut
     * @param statut Le statut de l'analyse
     * @return Liste des analyses avec ce statut
     */
    List<OptimisateurFlux> findByStatut(String statut);
    
    /**
     * Recherche les analyses dans une plage de dates
     * @param debut Date de début
     * @param fin Date de fin
     * @return Liste des analyses dans cette période
     */
    List<OptimisateurFlux> findByDateRange(LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Recherche les analyses avec score d'efficacité inférieur
     * @param scoreMax Le score maximum
     * @return Liste des analyses avec score faible
     */
    List<OptimisateurFlux> findByScoreInferieur(double scoreMax);
    
    /**
     * Récupère les dernières analyses
     * @param limite Nombre maximum d'analyses
     * @return Liste des dernières analyses
     */
    List<OptimisateurFlux> findDernieresAnalyses(int limite);
    
    /**
     * Calcule le score d'efficacité moyen
     * @return Le score moyen
     */
    Double calculerScoreMoyen();
}
