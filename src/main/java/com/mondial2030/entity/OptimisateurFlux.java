package com.mondial2030.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Entité représentant l'optimisateur de flux de spectateurs.
 * Analyse et propose des recommandations pour améliorer la circulation.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
@Entity
@Table(name = "optimisateur_flux")
public class OptimisateurFlux {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @Column(name = "date_analyse")
    private LocalDateTime dateAnalyse;

    @Column(name = "score_efficacite")
    private Double scoreEfficacite;

    @Column(name = "temps_evacuation_estime")
    private Integer tempsEvacuationEstime;

    @Column(name = "zones_critiques", length = 500)
    private String zonesCritiques;

    @Column(name = "recommandations", columnDefinition = "TEXT")
    private String recommandations;

    @Column(name = "statut_analyse", length = 50)
    private String statutAnalyse;

    @Column(name = "densite_moyenne")
    private Double densiteMoyenne;

    @Column(name = "densite_max")
    private Double densiteMax;

    @Column(name = "flux_moyen_par_minute")
    private Double fluxMoyenParMinute;

    @Column(name = "points_congestion", length = 500)
    private String pointsCongestion;

    @Column(name = "suggestions_redistribution", columnDefinition = "TEXT")
    private String suggestionsRedistribution;

    // Constructeur par défaut
    public OptimisateurFlux() {
        this.dateAnalyse = LocalDateTime.now();
        this.statutAnalyse = "EN_COURS";
    }

    // Constructeur avec paramètres
    public OptimisateurFlux(Match match) {
        this();
        this.match = match;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public LocalDateTime getDateAnalyse() {
        return dateAnalyse;
    }

    public void setDateAnalyse(LocalDateTime dateAnalyse) {
        this.dateAnalyse = dateAnalyse;
    }

    public Double getScoreEfficacite() {
        return scoreEfficacite;
    }

    public void setScoreEfficacite(Double scoreEfficacite) {
        this.scoreEfficacite = scoreEfficacite;
    }

    public Integer getTempsEvacuationEstime() {
        return tempsEvacuationEstime;
    }

    public void setTempsEvacuationEstime(Integer tempsEvacuationEstime) {
        this.tempsEvacuationEstime = tempsEvacuationEstime;
    }

    public String getZonesCritiques() {
        return zonesCritiques;
    }

    public void setZonesCritiques(String zonesCritiques) {
        this.zonesCritiques = zonesCritiques;
    }

    public String getRecommandations() {
        return recommandations;
    }

    public void setRecommandations(String recommandations) {
        this.recommandations = recommandations;
    }

    public String getStatutAnalyse() {
        return statutAnalyse;
    }

    public void setStatutAnalyse(String statutAnalyse) {
        this.statutAnalyse = statutAnalyse;
    }

    public Double getDensiteMoyenne() {
        return densiteMoyenne;
    }

    public void setDensiteMoyenne(Double densiteMoyenne) {
        this.densiteMoyenne = densiteMoyenne;
    }

    public Double getDensiteMax() {
        return densiteMax;
    }

    public void setDensiteMax(Double densiteMax) {
        this.densiteMax = densiteMax;
    }

    public Double getFluxMoyenParMinute() {
        return fluxMoyenParMinute;
    }

    public void setFluxMoyenParMinute(Double fluxMoyenParMinute) {
        this.fluxMoyenParMinute = fluxMoyenParMinute;
    }

    public String getPointsCongestion() {
        return pointsCongestion;
    }

    public void setPointsCongestion(String pointsCongestion) {
        this.pointsCongestion = pointsCongestion;
    }

    public String getSuggestionsRedistribution() {
        return suggestionsRedistribution;
    }

    public void setSuggestionsRedistribution(String suggestionsRedistribution) {
        this.suggestionsRedistribution = suggestionsRedistribution;
    }

    /**
     * Analyse les flux de spectateurs et génère des recommandations
     */
    public void analyserFlux(List<FluxSpectateurs> fluxList) {
        if (fluxList == null || fluxList.isEmpty()) {
            this.statutAnalyse = "AUCUNE_DONNEE";
            return;
        }

        // Calcul de la densité moyenne
        double totalDensite = 0;
        double maxDensite = 0;
        List<String> zonesCritiquesListe = new ArrayList<>();

        for (FluxSpectateurs flux : fluxList) {
            double densite = flux.getDensite() != null ? flux.getDensite() : 0;
            totalDensite += densite;
            if (densite > maxDensite) {
                maxDensite = densite;
            }
            if (flux.estEnSurpopulation() && flux.getZone() != null) {
                zonesCritiquesListe.add(flux.getZone().getNom());
            }
        }

        this.densiteMoyenne = totalDensite / fluxList.size();
        this.densiteMax = maxDensite;
        this.zonesCritiques = String.join(", ", zonesCritiquesListe);

        // Calcul du score d'efficacité
        this.scoreEfficacite = calculerScoreEfficacite();

        // Génération des recommandations
        genererRecommandations();

        this.statutAnalyse = "TERMINE";
    }

    /**
     * Calcule le score d'efficacité (0-100)
     */
    private Double calculerScoreEfficacite() {
        if (densiteMoyenne == null) return 0.0;
        
        // Score basé sur la densité moyenne (plus c'est bas, mieux c'est)
        double score = 100 - densiteMoyenne;
        
        // Pénalité si densité max trop élevée
        if (densiteMax != null && densiteMax > 80) {
            score -= (densiteMax - 80) * 0.5;
        }
        
        return Math.max(0, Math.min(100, score));
    }

    /**
     * Génère des recommandations basées sur l'analyse
     */
    private void genererRecommandations() {
        StringBuilder reco = new StringBuilder();

        if (densiteMoyenne != null && densiteMoyenne > 70) {
            reco.append("⚠️ Densité moyenne élevée. Recommandation: Ouvrir des portes supplémentaires.\n");
        }

        if (densiteMax != null && densiteMax > 90) {
            reco.append("🚨 Zone(s) en surpopulation détectée(s). Action urgente requise.\n");
        }

        if (zonesCritiques != null && !zonesCritiques.isEmpty()) {
            reco.append("📍 Zones nécessitant une attention particulière: ").append(zonesCritiques).append("\n");
        }

        if (scoreEfficacite != null && scoreEfficacite < 50) {
            reco.append("📊 Score d'efficacité faible. Envisager une redistribution des spectateurs.\n");
        }

        if (reco.length() == 0) {
            reco.append("✅ Flux de spectateurs optimal. Aucune action requise.");
        }

        this.recommandations = reco.toString();
    }

    /**
     * Estime le temps d'évacuation en minutes
     */
    public void estimerTempsEvacuation(int nombreSpectateurs, int nombrePortes) {
        // Estimation: 50 personnes par minute par porte
        int capaciteEvacuation = nombrePortes * 50;
        this.tempsEvacuationEstime = (int) Math.ceil((double) nombreSpectateurs / capaciteEvacuation);
    }

    @Override
    public String toString() {
        return "OptimisateurFlux{" +
                "match=" + (match != null ? match.getLibelleMatch() : "N/A") +
                ", scoreEfficacite=" + scoreEfficacite +
                ", statutAnalyse='" + statutAnalyse + '\'' +
                '}';
    }
}
