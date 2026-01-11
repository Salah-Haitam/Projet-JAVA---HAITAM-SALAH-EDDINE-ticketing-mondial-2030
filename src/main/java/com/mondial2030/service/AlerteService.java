package com.mondial2030.service;

import com.mondial2030.dao.impl.AlerteDAOImpl;
import com.mondial2030.dao.impl.ZoneDAOImpl;
import com.mondial2030.dao.impl.FluxSpectateurDAOImpl;
import com.mondial2030.dao.interfaces.AlerteDAO;
import com.mondial2030.dao.interfaces.ZoneDAO;
import com.mondial2030.dao.interfaces.FluxSpectateurDAO;
import com.mondial2030.entity.*;

import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des alertes.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class AlerteService {
    
    private static AlerteService instance;
    private final AlerteDAO alerteDAO;
    private final ZoneDAO zoneDAO;
    private final FluxSpectateurDAO fluxDAO;
    
    private AlerteService() {
        this.alerteDAO = new AlerteDAOImpl();
        this.zoneDAO = new ZoneDAOImpl();
        this.fluxDAO = new FluxSpectateurDAOImpl();
    }
    
    public static synchronized AlerteService getInstance() {
        if (instance == null) {
            instance = new AlerteService();
        }
        return instance;
    }
    
    /**
     * Crée une nouvelle alerte
     */
    public Alerte creerAlerte(String titre, String message, TypeAlerte type, NiveauAlerte niveau) {
        Alerte alerte = new Alerte(titre, message, type, niveau);
        alerteDAO.save(alerte);
        return alerte;
    }
    
    /**
     * Crée une alerte pour une zone
     */
    public Alerte creerAlerteZone(Zone zone, String titre, String message, TypeAlerte type, NiveauAlerte niveau) {
        Alerte alerte = new Alerte(titre, message, type, niveau);
        alerte.setZone(zone);
        if (zone.getMatch() != null) {
            alerte.setMatch(zone.getMatch());
        }
        alerteDAO.save(alerte);
        return alerte;
    }
    
    /**
     * Crée une alerte pour un match
     */
    public Alerte creerAlerteMatch(Match match, String titre, String message, TypeAlerte type, NiveauAlerte niveau) {
        Alerte alerte = new Alerte(titre, message, type, niveau);
        alerte.setMatch(match);
        alerteDAO.save(alerte);
        return alerte;
    }
    
    /**
     * Résout une alerte
     */
    public void resoudreAlerte(Long alerteId, Administrateur admin, String commentaire) {
        alerteDAO.resoudre(alerteId, admin.getId(), commentaire);
    }
    
    /**
     * Récupère toutes les alertes non résolues
     */
    public List<Alerte> getAlertesActives() {
        return alerteDAO.findNonResolues();
    }

    /**
     * Récupère toutes les alertes non résolues (alias)
     */
    public List<Alerte> getAlertesNonResolues() {
        return alerteDAO.findNonResolues();
    }
    
    /**
     * Récupère les alertes critiques non résolues
     */
    public List<Alerte> getAlertesCritiques() {
        return alerteDAO.findCritiquesNonResolues();
    }
    
    /**
     * Récupère les alertes d'un match
     */
    public List<Alerte> getAlertesMatch(Long matchId) {
        return alerteDAO.findByMatchId(matchId);
    }
    
    /**
     * Récupère les alertes d'une zone
     */
    public List<Alerte> getAlertesZone(Long zoneId) {
        return alerteDAO.findByZoneId(zoneId);
    }
    
    /**
     * Vérifie la surpopulation dans les zones et crée des alertes si nécessaire
     */
    public void verifierSurpopulation(Long matchId) {
        List<Zone> zones = zoneDAO.findByMatchId(matchId);
        
        for (Zone zone : zones) {
            double tauxOccupation = zone.getTauxOccupation();
            
            if (tauxOccupation > 95) {
                // Alerte critique
                String titre = "Surpopulation critique - " + zone.getNom();
                String message = String.format("La zone %s a atteint %.1f%% de sa capacité. Action immédiate requise!", 
                                               zone.getNom(), tauxOccupation);
                creerAlerteZone(zone, titre, message, TypeAlerte.SURPOPULATION, NiveauAlerte.CRITIQUE);
            } else if (tauxOccupation > 85) {
                // Alerte élevée
                String titre = "Surpopulation - " + zone.getNom();
                String message = String.format("La zone %s a atteint %.1f%% de sa capacité.", 
                                               zone.getNom(), tauxOccupation);
                creerAlerteZone(zone, titre, message, TypeAlerte.SURPOPULATION, NiveauAlerte.ELEVE);
            } else if (tauxOccupation > 75) {
                // Alerte moyenne
                String titre = "Zone densément peuplée - " + zone.getNom();
                String message = String.format("La zone %s a atteint %.1f%% de sa capacité.", 
                                               zone.getNom(), tauxOccupation);
                creerAlerteZone(zone, titre, message, TypeAlerte.SURPOPULATION, NiveauAlerte.MOYEN);
            }
        }
    }
    
    /**
     * Vérifie les flux de spectateurs et génère des alertes
     */
    public void verifierFluxSpectateurs(Long matchId) {
        List<FluxSpectateurs> flux = fluxDAO.findByMatchId(matchId);
        
        for (FluxSpectateurs f : flux) {
            if (f.estCapaciteCritique()) {
                String titre = "Flux critique détecté";
                String message = String.format("Zone %s: densité %.1f%%, temps d'attente estimé: %d minutes",
                                               f.getZone() != null ? f.getZone().getNom() : "Inconnue",
                                               f.getDensite(),
                                               f.getTempsAttenteMoyen() != null ? f.getTempsAttenteMoyen() : 0);
                Alerte alerte = new Alerte(titre, message, TypeAlerte.SURPOPULATION, NiveauAlerte.CRITIQUE);
                alerte.setMatch(f.getMatch());
                alerte.setZone(f.getZone());
                alerteDAO.save(alerte);
            }
        }
    }
    
    /**
     * Compte le nombre d'alertes actives
     */
    public long compterAlertesActives() {
        return alerteDAO.countNonResolues();
    }
    
    /**
     * Compte les alertes par niveau
     */
    public long compterAlertesParNiveau(NiveauAlerte niveau) {
        return alerteDAO.countByNiveau(niveau);
    }
    
    /**
     * Récupère les dernières alertes
     */
    public List<Alerte> getDernieresAlertes(int limite) {
        return alerteDAO.findDernieresAlertes(limite);
    }
    
    /**
     * Récupère toutes les alertes
     */
    public List<Alerte> getAllAlertes() {
        return alerteDAO.findAll();
    }
    
    /**
     * Trouve une alerte par ID
     */
    public Optional<Alerte> findById(Long id) {
        return alerteDAO.findById(id);
    }
    
    // ===== MÉTHODES POUR LES ALERTES UTILISATEURS =====
    
    /**
     * Crée une alerte soumise par un spectateur
     */
    public Alerte creerAlerteUtilisateur(Spectateur spectateur, String titre, String message, TypeAlerte type, NiveauAlerte niveau) {
        Alerte alerte = new Alerte(titre, message, type, niveau);
        alerte.setCreeePar(spectateur);
        alerte.setSource("Utilisateur: " + spectateur.getNom() + " " + spectateur.getPrenom());
        alerteDAO.save(alerte);
        return alerte;
    }
    
    /**
     * Récupère les alertes d'un spectateur
     */
    public List<Alerte> getAlertesSpectateur(Long spectateurId) {
        return alerteDAO.findBySpectateurId(spectateurId);
    }
    
    /**
     * Récupère toutes les alertes créées par des utilisateurs
     */
    public List<Alerte> getAlertesUtilisateurs() {
        return alerteDAO.findAlertesUtilisateurs();
    }
    
    /**
     * Récupère les alertes utilisateurs non lues
     */
    public List<Alerte> getAlertesUtilisateursNonLues() {
        return alerteDAO.findAlertesUtilisateursNonLues();
    }
    
    /**
     * Marque une alerte comme lue par l'administrateur
     */
    public void marquerCommeLue(Long alerteId, Administrateur admin) {
        alerteDAO.marquerCommeLue(alerteId, admin.getId());
    }
    
    /**
     * Répond à une alerte utilisateur
     */
    public void repondreAlerte(Long alerteId, Administrateur admin, String reponse) {
        alerteDAO.repondre(alerteId, admin.getId(), reponse);
    }
    
    /**
     * Compte les alertes utilisateurs non lues
     */
    public long compterAlertesUtilisateursNonLues() {
        return alerteDAO.countAlertesUtilisateursNonLues();
    }
}
