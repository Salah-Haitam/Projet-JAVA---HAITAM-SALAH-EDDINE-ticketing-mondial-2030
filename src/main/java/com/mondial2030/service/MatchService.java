package com.mondial2030.service;

import com.mondial2030.dao.impl.MatchDAOImpl;
import com.mondial2030.dao.impl.EquipeDAOImpl;
import com.mondial2030.dao.impl.ZoneDAOImpl;
import com.mondial2030.dao.interfaces.MatchDAO;
import com.mondial2030.dao.interfaces.EquipeDAO;
import com.mondial2030.dao.interfaces.ZoneDAO;
import com.mondial2030.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des matchs.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class MatchService {
    
    private static MatchService instance;
    private final MatchDAO matchDAO;
    private final EquipeDAO equipeDAO;
    private final ZoneDAO zoneDAO;
    
    private MatchService() {
        this.matchDAO = new MatchDAOImpl();
        this.equipeDAO = new EquipeDAOImpl();
        this.zoneDAO = new ZoneDAOImpl();
    }
    
    public static synchronized MatchService getInstance() {
        if (instance == null) {
            instance = new MatchService();
        }
        return instance;
    }
    
    /**
     * Crée un nouveau match
     */
    public Match creerMatch(Equipe equipeDomicile, Equipe equipeExterieur,
                            LocalDateTime dateHeure, String stade, String ville,
                            PhaseMatch phase, Double prixBase, Integer capacite) {
        Match match = new Match(equipeDomicile, equipeExterieur, dateHeure, stade, ville, phase, prixBase);
        match.setCapaciteStade(capacite);
        match.setTicketsDisponibles(capacite);
        match.setNumeroMatch(genererNumeroMatch(phase));
        
        if (phase == PhaseMatch.PHASE_GROUPES && equipeDomicile != null) {
            match.setGroupe(equipeDomicile.getGroupe());
        }
        
        matchDAO.save(match);
        
        // Créer les zones du stade
        creerZonesStade(match, capacite);
        
        return match;
    }
    
    /**
     * Met à jour le score d'un match
     */
    public void mettreAJourScore(Long matchId, int scoreDomicile, int scoreExterieur) {
        matchDAO.updateScore(matchId, scoreDomicile, scoreExterieur);
    }
    
    /**
     * Termine un match et met à jour les statistiques des équipes
     */
    public void terminerMatch(Long matchId, int scoreDomicile, int scoreExterieur) {
        Optional<Match> matchOpt = matchDAO.findById(matchId);
        if (matchOpt.isEmpty()) return;
        
        Match match = matchOpt.get();
        match.setScoreDomicile(scoreDomicile);
        match.setScoreExterieur(scoreExterieur);
        match.setTermine(true);
        matchDAO.update(match);
        
        // Mettre à jour les statistiques des équipes
        if (match.getEquipeDomicile() != null && match.getEquipeExterieur() != null) {
            Boolean victDomicile = null;
            if (scoreDomicile > scoreExterieur) {
                victDomicile = true;
            } else if (scoreDomicile < scoreExterieur) {
                victDomicile = false;
            }
            
            // Équipe domicile
            equipeDAO.updateStatistiques(
                match.getEquipeDomicile().getId(),
                victDomicile,
                scoreDomicile,
                scoreExterieur
            );
            
            // Équipe extérieur (inverse)
            Boolean victExterieur = victDomicile == null ? null : !victDomicile;
            equipeDAO.updateStatistiques(
                match.getEquipeExterieur().getId(),
                victExterieur,
                scoreExterieur,
                scoreDomicile
            );
        }
    }
    
    /**
     * Récupère tous les matchs à venir
     */
    public List<Match> getMatchsAVenir() {
        return matchDAO.findMatchsAVenir();
    }
    
    /**
     * Récupère tous les matchs terminés
     */
    public List<Match> getMatchsTermines() {
        return matchDAO.findMatchsTermines();
    }
    
    /**
     * Récupère les matchs d'un groupe
     */
    public List<Match> getMatchsGroupe(String groupe) {
        return matchDAO.findByGroupe(groupe);
    }
    
    /**
     * Récupère les matchs d'une phase
     */
    public List<Match> getMatchsPhase(PhaseMatch phase) {
        return matchDAO.findByPhase(phase);
    }
    
    /**
     * Récupère les matchs d'une équipe
     */
    public List<Match> getMatchsEquipe(Long equipeId) {
        return matchDAO.findByEquipeId(equipeId);
    }
    
    /**
     * Récupère les matchs avec tickets disponibles
     */
    public List<Match> getMatchsAvecTicketsDisponibles() {
        return matchDAO.findWithTicketsDisponibles();
    }
    
    /**
     * Récupère les matchs du jour
     */
    public List<Match> getMatchsDuJour() {
        return matchDAO.findMatchsDuJour();
    }
    
    /**
     * Recherche un match par son ID
     */
    public Optional<Match> findById(Long id) {
        return matchDAO.findById(id);
    }
    
    /**
     * Récupère tous les matchs
     */
    public List<Match> getAllMatchs() {
        return matchDAO.findAll();
    }
    
    /**
     * Met à jour un match
     */
    public void updateMatch(Match match) {
        matchDAO.update(match);
    }
    
    /**
     * Supprime un match
     */
    public void deleteMatch(Long id) {
        matchDAO.delete(id);
    }

    /**
     * Supprime un match (alias pour deleteMatch)
     */
    public void supprimerMatch(Long id) {
        matchDAO.delete(id);
    }
    
    /**
     * Récupère toutes les équipes
     */
    public List<Equipe> getAllEquipes() {
        return equipeDAO.findAll();
    }
    
    /**
     * Génère un numéro de match unique
     */
    private String genererNumeroMatch(PhaseMatch phase) {
        String prefix = switch (phase) {
            case PHASE_GROUPES -> "GR";
            case HUITIEMES -> "R16";
            case QUARTS -> "QF";
            case DEMI_FINALES -> "SF";
            case PETITE_FINALE -> "PF";
            case FINALE -> "FIN";
        };
        return prefix + "-" + System.currentTimeMillis() % 10000;
    }
    
    /**
     * Crée les zones par défaut pour un stade
     */
    private void creerZonesStade(Match match, Integer capaciteTotale) {
        if (capaciteTotale == null || capaciteTotale <= 0) return;
        
        // Distribution approximative
        int capaciteZone = capaciteTotale / 8;
        
        TypeZone[] types = {
            TypeZone.TRIBUNE_NORD, TypeZone.TRIBUNE_SUD,
            TypeZone.TRIBUNE_EST, TypeZone.TRIBUNE_OUEST,
            TypeZone.VIP, TypeZone.VIRAGE
        };
        
        double[] coefficients = {1.0, 1.0, 1.2, 1.2, 3.0, 0.8};
        
        for (int i = 0; i < types.length; i++) {
            Zone zone = new Zone(types[i].getLibelle(), types[i], capaciteZone, coefficients[i]);
            zone.setMatch(match);
            zone.setNiveau(1);
            if (types[i] == TypeZone.VIP) {
                zone.setCapacite(capaciteZone / 4);
                zone.setPlacesDisponibles(capaciteZone / 4);
            }
            zoneDAO.save(zone);
        }
    }
}
