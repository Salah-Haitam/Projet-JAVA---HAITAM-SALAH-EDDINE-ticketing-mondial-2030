package com.mondial2030.dao.interfaces;

import java.util.List;
import java.util.Optional;

/**
 * Interface générique pour les opérations CRUD de base.
 * 
 * @param <T> Type de l'entité
 * @param <ID> Type de l'identifiant
 * @author Mondial 2030 Team
 * @version 1.0
 */
public interface GenericDAO<T, ID> {
    
    /**
     * Sauvegarde une entité
     * @param entity L'entité à sauvegarder
     */
    void save(T entity);
    
    /**
     * Met à jour une entité
     * @param entity L'entité à mettre à jour
     */
    void update(T entity);
    
    /**
     * Supprime une entité par son identifiant
     * @param id L'identifiant de l'entité
     */
    void delete(ID id);
    
    /**
     * Recherche une entité par son identifiant
     * @param id L'identifiant de l'entité
     * @return L'entité trouvée ou empty
     */
    Optional<T> findById(ID id);
    
    /**
     * Récupère toutes les entités
     * @return Liste de toutes les entités
     */
    List<T> findAll();
    
    /**
     * Compte le nombre total d'entités
     * @return Le nombre d'entités
     */
    long count();
    
    /**
     * Vérifie si une entité existe par son identifiant
     * @param id L'identifiant à vérifier
     * @return true si l'entité existe
     */
    boolean existsById(ID id);
}
