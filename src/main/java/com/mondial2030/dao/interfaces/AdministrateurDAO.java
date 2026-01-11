package com.mondial2030.dao.interfaces;

import com.mondial2030.entity.Administrateur;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour la gestion des administrateurs.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public interface AdministrateurDAO extends GenericDAO<Administrateur, Long> {
    
    /**
     * Recherche un administrateur par son email
     * @param email L'email de l'administrateur
     * @return L'administrateur trouvé ou empty
     */
    Optional<Administrateur> findByEmail(String email);
    
    /**
     * Recherche les administrateurs par département
     * @param departement Le département
     * @return Liste des administrateurs du département
     */
    List<Administrateur> findByDepartement(String departement);
    
    /**
     * Recherche les administrateurs par niveau d'accès
     * @param niveauAcces Le niveau d'accès minimum
     * @return Liste des administrateurs avec ce niveau ou supérieur
     */
    List<Administrateur> findByNiveauAccesMinimum(int niveauAcces);
    
    /**
     * Recherche les administrateurs avec tous les droits
     * @return Liste des administrateurs avec accès complet
     */
    List<Administrateur> findWithAccesComplet();
    
    /**
     * Authentifie un administrateur
     * @param email L'email
     * @param motDePasse Le mot de passe
     * @return L'administrateur authentifié ou empty
     */
    Optional<Administrateur> authentifier(String email, String motDePasse);
}
