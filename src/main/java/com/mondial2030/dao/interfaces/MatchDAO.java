package com.mondial2030.dao.interfaces;

import com.mondial2030.entity.Match;
import com.mondial2030.entity.PhaseMatch;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour la gestion des matchs.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public interface MatchDAO extends GenericDAO<Match, Long> {
    
    /**
     * Recherche un match par son numéro
     * @param numeroMatch Le numéro du match
     * @return Le match trouvé ou empty
     */
    Optional<Match> findByNumeroMatch(String numeroMatch);
    
    /**
     * Recherche les matchs par phase
     * @param phase La phase du tournoi
     * @return Liste des matchs de cette phase
     */
    List<Match> findByPhase(PhaseMatch phase);
    
    /**
     * Recherche les matchs par groupe
     * @param groupe Le groupe
     * @return Liste des matchs de ce groupe
     */
    List<Match> findByGroupe(String groupe);
    
    /**
     * Recherche les matchs d'une équipe
     * @param equipeId L'identifiant de l'équipe
     * @return Liste des matchs de l'équipe
     */
    List<Match> findByEquipeId(Long equipeId);
    
    /**
     * Recherche les matchs à venir
     * @return Liste des matchs à venir
     */
    List<Match> findMatchsAVenir();
    
    /**
     * Recherche les matchs terminés
     * @return Liste des matchs terminés
     */
    List<Match> findMatchsTermines();
    
    /**
     * Recherche les matchs par stade
     * @param stade Le nom du stade
     * @return Liste des matchs dans ce stade
     */
    List<Match> findByStade(String stade);
    
    /**
     * Recherche les matchs par ville
     * @param ville La ville
     * @return Liste des matchs dans cette ville
     */
    List<Match> findByVille(String ville);
    
    /**
     * Recherche les matchs dans une plage de dates
     * @param debut Date de début
     * @param fin Date de fin
     * @return Liste des matchs dans cette période
     */
    List<Match> findByDateRange(LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Recherche les matchs avec des tickets disponibles
     * @return Liste des matchs avec tickets disponibles
     */
    List<Match> findWithTicketsDisponibles();
    
    /**
     * Met à jour le score d'un match
     * @param matchId L'identifiant du match
     * @param scoreDomicile Score de l'équipe à domicile
     * @param scoreExterieur Score de l'équipe à l'extérieur
     */
    void updateScore(Long matchId, int scoreDomicile, int scoreExterieur);
    
    /**
     * Termine un match
     * @param matchId L'identifiant du match
     */
    void terminerMatch(Long matchId);
    
    /**
     * Décrémente le nombre de tickets disponibles
     * @param matchId L'identifiant du match
     */
    void decrementerTicketsDisponibles(Long matchId);
    
    /**
     * Récupère les matchs du jour
     * @return Liste des matchs du jour
     */
    List<Match> findMatchsDuJour();
}
