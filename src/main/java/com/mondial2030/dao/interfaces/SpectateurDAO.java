package com.mondial2030.dao.interfaces;

import com.mondial2030.entity.Spectateur;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour la gestion des spectateurs.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public interface SpectateurDAO extends GenericDAO<Spectateur, Long> {
    
    /**
     * Recherche un spectateur par son email
     * @param email L'email du spectateur
     * @return Le spectateur trouvé ou empty
     */
    Optional<Spectateur> findByEmail(String email);
    
    /**
     * Recherche les spectateurs par nationalité
     * @param nationalite La nationalité
     * @return Liste des spectateurs de cette nationalité
     */
    List<Spectateur> findByNationalite(String nationalite);
    
    /**
     * Recherche un spectateur par numéro de passeport
     * @param numeroPasseport Le numéro de passeport
     * @return Le spectateur trouvé ou empty
     */
    Optional<Spectateur> findByNumeroPasseport(String numeroPasseport);
    
    /**
     * Recherche les spectateurs ayant acheté un certain nombre de tickets
     * @param nombreMinimum Le nombre minimum de tickets
     * @return Liste des spectateurs correspondants
     */
    List<Spectateur> findByNombreTicketsMinimum(int nombreMinimum);
    
    /**
     * Authentifie un spectateur
     * @param email L'email
     * @param motDePasse Le mot de passe
     * @return Le spectateur authentifié ou empty
     */
    Optional<Spectateur> authentifier(String email, String motDePasse);
    
    /**
     * Recherche des spectateurs par ville
     * @param ville La ville
     * @return Liste des spectateurs de cette ville
     */
    List<Spectateur> findByVille(String ville);
    
    /**
     * Incrémente le nombre de tickets achetés
     * @param id L'identifiant du spectateur
     */
    void incrementerNombreTickets(Long id);
}
