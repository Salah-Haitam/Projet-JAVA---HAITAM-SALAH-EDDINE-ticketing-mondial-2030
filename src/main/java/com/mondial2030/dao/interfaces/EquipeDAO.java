package com.mondial2030.dao.interfaces;

import com.mondial2030.entity.Equipe;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour la gestion des équipes.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public interface EquipeDAO extends GenericDAO<Equipe, Long> {
    
    /**
     * Recherche une équipe par son nom
     * @param nom Le nom de l'équipe
     * @return L'équipe trouvée ou empty
     */
    Optional<Equipe> findByNom(String nom);
    
    /**
     * Recherche une équipe par son code pays
     * @param codePays Le code pays (ex: FRA, ESP)
     * @return L'équipe trouvée ou empty
     */
    Optional<Equipe> findByCodePays(String codePays);
    
    /**
     * Recherche les équipes d'un groupe
     * @param groupe Le groupe (A, B, C, etc.)
     * @return Liste des équipes du groupe
     */
    List<Equipe> findByGroupe(String groupe);
    
    /**
     * Recherche les équipes par confédération
     * @param confederation La confédération (UEFA, CONMEBOL, etc.)
     * @return Liste des équipes de cette confédération
     */
    List<Equipe> findByConfederation(String confederation);
    
    /**
     * Récupère le classement d'un groupe
     * @param groupe Le groupe
     * @return Liste des équipes triées par points
     */
    List<Equipe> getClassementGroupe(String groupe);
    
    /**
     * Met à jour les statistiques d'une équipe après un match
     * @param equipeId L'identifiant de l'équipe
     * @param victoire true si victoire, false si défaite, null si nul
     * @param butsMarques Nombre de buts marqués
     * @param butsEncaisses Nombre de buts encaissés
     */
    void updateStatistiques(Long equipeId, Boolean victoire, int butsMarques, int butsEncaisses);
    
    /**
     * Récupère les équipes qualifiées pour la phase suivante
     * @param groupe Le groupe
     * @param nombreQualifies Nombre d'équipes à qualifier
     * @return Liste des équipes qualifiées
     */
    List<Equipe> getEquipesQualifiees(String groupe, int nombreQualifies);
    
    /**
     * Recherche les équipes par classement FIFA
     * @param classementMax Le classement FIFA maximum
     * @return Liste des équipes avec ce classement ou meilleur
     */
    List<Equipe> findByClassementFifaMax(int classementMax);
}
