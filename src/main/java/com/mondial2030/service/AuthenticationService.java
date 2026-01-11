package com.mondial2030.service;

import com.mondial2030.dao.impl.AdministrateurDAOImpl;
import com.mondial2030.dao.impl.SpectateurDAOImpl;
import com.mondial2030.dao.interfaces.AdministrateurDAO;
import com.mondial2030.dao.interfaces.SpectateurDAO;
import com.mondial2030.entity.Administrateur;
import com.mondial2030.entity.Spectateur;
import com.mondial2030.entity.Utilisateur;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service de gestion de l'authentification.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class AuthenticationService {
    
    private static AuthenticationService instance;
    private final AdministrateurDAO adminDAO;
    private final SpectateurDAO spectateurDAO;
    private Utilisateur utilisateurConnecte;
    
    private AuthenticationService() {
        this.adminDAO = new AdministrateurDAOImpl();
        this.spectateurDAO = new SpectateurDAOImpl();
    }
    
    /**
     * Retourne l'instance singleton du service
     */
    public static synchronized AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }
    
    /**
     * Authentifie un utilisateur (admin ou spectateur)
     * @param email Email de l'utilisateur
     * @param motDePasse Mot de passe
     * @return L'utilisateur connecté ou empty
     */
    public Optional<Utilisateur> authentifier(String email, String motDePasse) {
        // D'abord essayer comme administrateur
        Optional<Administrateur> admin = adminDAO.authentifier(email, motDePasse);
        if (admin.isPresent()) {
            utilisateurConnecte = admin.get();
            utilisateurConnecte.setDerniereConnexion(LocalDateTime.now());
            return Optional.of(utilisateurConnecte);
        }
        
        // Sinon essayer comme spectateur
        Optional<Spectateur> spectateur = spectateurDAO.authentifier(email, motDePasse);
        if (spectateur.isPresent()) {
            utilisateurConnecte = spectateur.get();
            utilisateurConnecte.setDerniereConnexion(LocalDateTime.now());
            return Optional.of(utilisateurConnecte);
        }
        
        return Optional.empty();
    }
    
    /**
     * Inscrit un nouveau spectateur
     * @param spectateur Le spectateur à inscrire
     * @return true si l'inscription réussit
     */
    public boolean inscrireSpectateur(Spectateur spectateur) {
        try {
            // Vérifier si l'email existe déjà
            if (spectateurDAO.findByEmail(spectateur.getEmail()).isPresent() ||
                adminDAO.findByEmail(spectateur.getEmail()).isPresent()) {
                return false;
            }
            spectateurDAO.save(spectateur);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Inscrit un nouvel administrateur
     * @param admin L'administrateur à inscrire
     * @return true si l'inscription réussit
     */
    public boolean inscrireAdministrateur(Administrateur admin) {
        try {
            // Vérifier si l'email existe déjà
            if (spectateurDAO.findByEmail(admin.getEmail()).isPresent() ||
                adminDAO.findByEmail(admin.getEmail()).isPresent()) {
                return false;
            }
            adminDAO.save(admin);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Déconnecte l'utilisateur courant
     */
    public void deconnecter() {
        utilisateurConnecte = null;
    }
    
    /**
     * Retourne l'utilisateur actuellement connecté
     */
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }
    
    /**
     * Vérifie si un utilisateur est connecté
     */
    public boolean estConnecte() {
        return utilisateurConnecte != null;
    }
    
    /**
     * Vérifie si l'utilisateur connecté est un administrateur
     */
    public boolean estAdministrateur() {
        return utilisateurConnecte instanceof Administrateur;
    }
    
    /**
     * Vérifie si l'utilisateur connecté est un spectateur
     */
    public boolean estSpectateur() {
        return utilisateurConnecte instanceof Spectateur;
    }
    
    /**
     * Retourne l'administrateur connecté ou null
     */
    public Administrateur getAdministrateurConnecte() {
        if (utilisateurConnecte instanceof Administrateur) {
            return (Administrateur) utilisateurConnecte;
        }
        return null;
    }
    
    /**
     * Retourne le spectateur connecté ou null
     */
    public Spectateur getSpectateurConnecte() {
        if (utilisateurConnecte instanceof Spectateur) {
            return (Spectateur) utilisateurConnecte;
        }
        return null;
    }
    
    /**
     * Vérifie si un email est déjà utilisé
     */
    public boolean emailExiste(String email) {
        return spectateurDAO.findByEmail(email).isPresent() ||
               adminDAO.findByEmail(email).isPresent();
    }

    /**
     * Récupère tous les spectateurs
     */
    public java.util.List<Spectateur> getAllSpectateurs() {
        return spectateurDAO.findAll();
    }

    /**
     * Met à jour le profil d'un spectateur
     */
    public boolean mettreAJourProfil(Spectateur spectateur) {
        try {
            spectateurDAO.update(spectateur);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Met à jour le profil d'un utilisateur (surcharge)
     */
    public boolean mettreAJourProfil(Utilisateur utilisateur) {
        try {
            if (utilisateur instanceof Spectateur) {
                spectateurDAO.update((Spectateur) utilisateur);
            } else if (utilisateur instanceof Administrateur) {
                adminDAO.update((Administrateur) utilisateur);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Change le mot de passe d'un utilisateur
     */
    public boolean changerMotDePasse(Long userId, String ancienMdp, String nouveauMdp) {
        try {
            // Trouver l'utilisateur
            Optional<Spectateur> spectateurOpt = spectateurDAO.findById(userId);
            if (spectateurOpt.isPresent()) {
                Spectateur spectateur = spectateurOpt.get();
                // Vérifier l'ancien mot de passe
                if (org.mindrot.jbcrypt.BCrypt.checkpw(ancienMdp, spectateur.getMotDePasse())) {
                    spectateur.setMotDePasse(org.mindrot.jbcrypt.BCrypt.hashpw(nouveauMdp, org.mindrot.jbcrypt.BCrypt.gensalt()));
                    spectateurDAO.update(spectateur);
                    return true;
                }
            }
            
            Optional<Administrateur> adminOpt = adminDAO.findById(userId);
            if (adminOpt.isPresent()) {
                Administrateur admin = adminOpt.get();
                if (org.mindrot.jbcrypt.BCrypt.checkpw(ancienMdp, admin.getMotDePasse())) {
                    admin.setMotDePasse(org.mindrot.jbcrypt.BCrypt.hashpw(nouveauMdp, org.mindrot.jbcrypt.BCrypt.gensalt()));
                    adminDAO.update(admin);
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Change le mot de passe d'un utilisateur (surcharge avec Utilisateur)
     */
    public boolean changerMotDePasse(Utilisateur utilisateur, String ancienMdp, String nouveauMdp) {
        if (utilisateur == null) return false;
        return changerMotDePasse(utilisateur.getId(), ancienMdp, nouveauMdp);
    }
}
