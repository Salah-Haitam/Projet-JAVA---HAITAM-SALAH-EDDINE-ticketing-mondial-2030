package com.mondial2030.service;

import com.mondial2030.dao.impl.*;
import com.mondial2030.dao.interfaces.*;
import com.mondial2030.entity.*;

import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des flux de spectateurs.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class FluxService {
    
    private static FluxService instance;
    private final FluxSpectateurDAO fluxDAO;
    private final OptimisateurFluxDAO optimisateurDAO;
    private final ZoneDAO zoneDAO;
    private final AlerteService alerteService;
    
    private FluxService() {
        this.fluxDAO = new FluxSpectateurDAOImpl();
        this.optimisateurDAO = new OptimisateurFluxDAOImpl();
        this.zoneDAO = new ZoneDAOImpl();
        this.alerteService = AlerteService.getInstance();
    }
    
    public static synchronized FluxService getInstance() {
        if (instance == null) {
            instance = new FluxService();
        }
        return instance;
    }
    
    /**
     * Initialise le suivi des flux pour un match
     */
    public void initialiserFluxMatch(Match match) {
        List<Zone> zones = zoneDAO.findByMatchId(match.getId());
        
        for (Zone zone : zones) {
            FluxSpectateurs flux = new FluxSpectateurs(match, zone, zone.getCapacite());
            flux.setPorteEntree("Porte " + zone.getTypeZone().name().substring(0, 1));
            fluxDAO.save(flux);
        }
    }
    
    /**
     * Enregistre une entrée de spectateur
     */
    public void enregistrerEntree(Long matchId, Long zoneId) {
        fluxDAO.enregistrerEntree(matchId, zoneId);
        
        // Vérifier si une alerte doit être générée
        FluxSpectateurs flux = fluxDAO.findDernierFluxByZone(zoneId);
        if (flux != null && flux.estEnSurpopulation()) {
            alerteService.creerAlerteZone(
                flux.getZone(),
                "Surpopulation détectée",
                String.format("La zone %s atteint %.1f%% de sa capacité", 
                              flux.getZone().getNom(), flux.getDensite()),
                TypeAlerte.SURPOPULATION,
                flux.estCapaciteCritique() ? NiveauAlerte.CRITIQUE : NiveauAlerte.ELEVE
            );
        }
    }
    
    /**
     * Enregistre une sortie de spectateur
     */
    public void enregistrerSortie(Long matchId, Long zoneId) {
        fluxDAO.enregistrerSortie(matchId, zoneId);
    }
    
    /**
     * Récupère les flux d'un match
     */
    public List<FluxSpectateurs> getFluxMatch(Long matchId) {
        return fluxDAO.findByMatchId(matchId);
    }
    
    /**
     * Récupère les flux d'une zone
     */
    public List<FluxSpectateurs> getFluxZone(Long zoneId) {
        return fluxDAO.findByZoneId(zoneId);
    }
    
    /**
     * Calcule la densité moyenne d'un match
     */
    public Double calculerDensiteMoyenne(Long matchId) {
        return fluxDAO.calculerDensiteMoyenne(matchId);
    }
    
    /**
     * Calcule l'occupation totale d'un match
     */
    public Integer calculerOccupationTotale(Long matchId) {
        return fluxDAO.calculerOccupationTotale(matchId);
    }
    
    /**
     * Lance une analyse d'optimisation des flux
     */
    public OptimisateurFlux analyserFlux(Match match) {
        OptimisateurFlux optimisateur = new OptimisateurFlux(match);
        
        List<FluxSpectateurs> fluxList = fluxDAO.findByMatchId(match.getId());
        optimisateur.analyserFlux(fluxList);
        
        // Estimer le temps d'évacuation
        Integer occupation = calculerOccupationTotale(match.getId());
        if (occupation != null) {
            optimisateur.estimerTempsEvacuation(occupation, 8); // 8 portes par défaut
        }
        
        optimisateurDAO.save(optimisateur);
        
        return optimisateur;
    }
    
    /**
     * Récupère la dernière analyse d'un match
     */
    public Optional<OptimisateurFlux> getDerniereAnalyse(Long matchId) {
        return optimisateurDAO.findDerniereAnalyse(matchId);
    }
    
    /**
     * Récupère les zones en surpopulation
     */
    public List<FluxSpectateurs> getZonesEnSurpopulation(double seuilDensite) {
        return fluxDAO.findEnSurpopulation(seuilDensite);
    }
    
    /**
     * Récupère toutes les analyses
     */
    public List<OptimisateurFlux> getAllAnalyses() {
        return optimisateurDAO.findAll();
    }
    
    /**
     * Récupère les analyses avec score faible
     */
    public List<OptimisateurFlux> getAnalysesProblematiques(double scoreMax) {
        return optimisateurDAO.findByScoreInferieur(scoreMax);
    }
}
