package com.mondial2030.dao.interfaces;

import com.mondial2030.entity.Utilisateur;
import java.util.Optional;
import java.util.List;

/**
 * Interface DAO pour la gestion des utilisateurs.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public interface UtilisateurDAO extends GenericDAO<Utilisateur, Long> {
    
    /**
     * Recherche un utilisateur par son email
     * @param email L'email de l'utilisateur
     * @return L'utilisateur trouvé ou empty
     */
    Optional<Utilisateur> findByEmail(String email);
    
    /**
     * Authentifie un utilisateur
     * @param email L'email de l'utilisateur
     * @param motDePasse Le mot de passe
     * @return L'utilisateur authentifié ou empty
     */
    Optional<Utilisateur> authentifier(String email, String motDePasse);
    
    /**
     * Vérifie si un email existe déjà
     * @param email L'email à vérifier
     * @return true si l'email existe
     */
    boolean emailExists(String email);
    
    /**
     * Recherche des utilisateurs par nom
     * @param nom Le nom à rechercher
     * @return Liste des utilisateurs correspondants
     */
    List<Utilisateur> findByNom(String nom);
    
    /**
     * Récupère tous les utilisateurs actifs
     * @return Liste des utilisateurs actifs
     */
    List<Utilisateur> findAllActifs();
    
    /**
     * Met à jour la dernière connexion d'un utilisateur
     * @param id L'identifiant de l'utilisateur
     */
    void updateDerniereConnexion(Long id);
    
    /**
     * Active ou désactive un utilisateur
     * @param id L'identifiant de l'utilisateur
     * @param actif L'état à définir
     */
    void setActif(Long id, boolean actif);
}
