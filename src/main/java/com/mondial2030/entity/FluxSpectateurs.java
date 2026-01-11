package com.mondial2030.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité représentant le flux de spectateurs dans une zone.
 * Utilisée pour l'analyse et l'optimisation des mouvements.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
@Entity
@Table(name = "flux_spectateurs")
public class FluxSpectateurs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @Column(name = "horodatage")
    private LocalDateTime horodatage;

    @Column(name = "nombre_entrees")
    private Integer nombreEntrees;

    @Column(name = "nombre_sorties")
    private Integer nombreSorties;

    @Column(name = "occupation_actuelle")
    private Integer occupationActuelle;

    @Column(name = "capacite_max")
    private Integer capaciteMax;

    @Column(name = "densite")
    private Double densite;

    @Column(name = "vitesse_flux")
    private Double vitesseFlux;

    @Column(name = "temps_attente_moyen")
    private Integer tempsAttenteMoyen;

    @Column(name = "porte_entree", length = 50)
    private String porteEntree;

    @Column(name = "porte_sortie", length = 50)
    private String porteSortie;

    // Constructeur par défaut
    public FluxSpectateurs() {
        this.horodatage = LocalDateTime.now();
        this.nombreEntrees = 0;
        this.nombreSorties = 0;
        this.occupationActuelle = 0;
    }

    // Constructeur avec paramètres
    public FluxSpectateurs(Match match, Zone zone, Integer capaciteMax) {
        this();
        this.match = match;
        this.zone = zone;
        this.capaciteMax = capaciteMax;
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

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public LocalDateTime getHorodatage() {
        return horodatage;
    }

    public void setHorodatage(LocalDateTime horodatage) {
        this.horodatage = horodatage;
    }

    public Integer getNombreEntrees() {
        return nombreEntrees;
    }

    public void setNombreEntrees(Integer nombreEntrees) {
        this.nombreEntrees = nombreEntrees;
    }

    public Integer getNombreSorties() {
        return nombreSorties;
    }

    public void setNombreSorties(Integer nombreSorties) {
        this.nombreSorties = nombreSorties;
    }

    public Integer getOccupationActuelle() {
        return occupationActuelle;
    }

    public void setOccupationActuelle(Integer occupationActuelle) {
        this.occupationActuelle = occupationActuelle;
    }

    public Integer getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(Integer capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public Double getDensite() {
        return densite;
    }

    public void setDensite(Double densite) {
        this.densite = densite;
    }

    public Double getVitesseFlux() {
        return vitesseFlux;
    }

    public void setVitesseFlux(Double vitesseFlux) {
        this.vitesseFlux = vitesseFlux;
    }

    public Integer getTempsAttenteMoyen() {
        return tempsAttenteMoyen;
    }

    public void setTempsAttenteMoyen(Integer tempsAttenteMoyen) {
        this.tempsAttenteMoyen = tempsAttenteMoyen;
    }

    public String getPorteEntree() {
        return porteEntree;
    }

    public void setPorteEntree(String porteEntree) {
        this.porteEntree = porteEntree;
    }

    public String getPorteSortie() {
        return porteSortie;
    }

    public void setPorteSortie(String porteSortie) {
        this.porteSortie = porteSortie;
    }

    /**
     * Enregistre une entrée de spectateur
     */
    public void enregistrerEntree() {
        nombreEntrees++;
        occupationActuelle++;
        calculerDensite();
    }

    /**
     * Enregistre une sortie de spectateur
     */
    public void enregistrerSortie() {
        nombreSorties++;
        if (occupationActuelle > 0) {
            occupationActuelle--;
        }
        calculerDensite();
    }

    /**
     * Calcule la densité actuelle
     */
    public void calculerDensite() {
        if (capaciteMax != null && capaciteMax > 0) {
            this.densite = (double) occupationActuelle / capaciteMax * 100;
        }
    }

    /**
     * Retourne le taux d'occupation
     */
    public double getTauxOccupation() {
        if (capaciteMax == null || capaciteMax == 0) return 0;
        return (double) occupationActuelle / capaciteMax * 100;
    }

    /**
     * Vérifie si la zone est en surpopulation
     */
    public boolean estEnSurpopulation() {
        return getTauxOccupation() > 90;
    }

    /**
     * Vérifie si la zone est à capacité critique
     */
    public boolean estCapaciteCritique() {
        return getTauxOccupation() > 95;
    }

    @Override
    public String toString() {
        return "FluxSpectateurs{" +
                "zone=" + (zone != null ? zone.getNom() : "N/A") +
                ", occupation=" + occupationActuelle + "/" + capaciteMax +
                ", densite=" + String.format("%.1f%%", densite) +
                '}';
    }
}
