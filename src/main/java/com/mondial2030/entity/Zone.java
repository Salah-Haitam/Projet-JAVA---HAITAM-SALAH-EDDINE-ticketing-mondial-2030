package com.mondial2030.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant une zone du stade.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
@Entity
@Table(name = "zone")
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_zone")
    private TypeZone typeZone;

    @Column(name = "capacite")
    private Integer capacite;

    @Column(name = "places_disponibles")
    private Integer placesDisponibles;

    @Column(name = "coefficient_prix")
    private Double coefficientPrix;

    @Column(name = "niveau")
    private Integer niveau;

    @Column(name = "accessible_pmr")
    private Boolean accessiblePmr;

    @Column(name = "description", length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Siege> sieges = new ArrayList<>();

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FluxSpectateurs> fluxSpectateurs = new ArrayList<>();

    // Constructeur par défaut
    public Zone() {
        this.coefficientPrix = 1.0;
        this.accessiblePmr = false;
    }

    // Constructeur avec paramètres
    public Zone(String nom, TypeZone typeZone, Integer capacite, Double coefficientPrix) {
        this();
        this.nom = nom;
        this.typeZone = typeZone;
        this.capacite = capacite;
        this.placesDisponibles = capacite;
        this.coefficientPrix = coefficientPrix;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public TypeZone getTypeZone() {
        return typeZone;
    }

    public void setTypeZone(TypeZone typeZone) {
        this.typeZone = typeZone;
    }

    public Integer getCapacite() {
        return capacite;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
        if (this.placesDisponibles == null) {
            this.placesDisponibles = capacite;
        }
    }

    public Integer getPlacesDisponibles() {
        return placesDisponibles;
    }

    public void setPlacesDisponibles(Integer placesDisponibles) {
        this.placesDisponibles = placesDisponibles;
    }

    public Double getCoefficientPrix() {
        return coefficientPrix;
    }

    public void setCoefficientPrix(Double coefficientPrix) {
        this.coefficientPrix = coefficientPrix;
    }

    public Integer getNiveau() {
        return niveau;
    }

    public void setNiveau(Integer niveau) {
        this.niveau = niveau;
    }

    public Boolean getAccessiblePmr() {
        return accessiblePmr;
    }

    public void setAccessiblePmr(Boolean accessiblePmr) {
        this.accessiblePmr = accessiblePmr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public List<Siege> getSieges() {
        return sieges;
    }

    public void setSieges(List<Siege> sieges) {
        this.sieges = sieges;
    }

    public List<FluxSpectateurs> getFluxSpectateurs() {
        return fluxSpectateurs;
    }

    public void setFluxSpectateurs(List<FluxSpectateurs> fluxSpectateurs) {
        this.fluxSpectateurs = fluxSpectateurs;
    }

    /**
     * Calcule le taux d'occupation de la zone
     */
    public double getTauxOccupation() {
        if (capacite == null || capacite == 0) return 0;
        int placesOccupees = capacite - (placesDisponibles != null ? placesDisponibles : 0);
        return (double) placesOccupees / capacite * 100;
    }

    /**
     * Vérifie si des places sont disponibles
     */
    public boolean aDesPlacesDisponibles() {
        return placesDisponibles != null && placesDisponibles > 0;
    }

    /**
     * Décrémente le nombre de places disponibles
     */
    public void decrementerPlaces() {
        if (placesDisponibles != null && placesDisponibles > 0) {
            placesDisponibles--;
        }
    }

    /**
     * Incrémente le nombre de places disponibles
     */
    public void incrementerPlaces() {
        if (placesDisponibles != null && capacite != null && placesDisponibles < capacite) {
            placesDisponibles++;
        }
    }

    /**
     * Retourne le prix de base de la zone (calculé à partir du coefficient)
     */
    public Double getPrixBase() {
        // Prix de base standard pour un match, multiplié par le coefficient
        double prixBaseStandard = 100.0;
        if (match != null && match.getPrixBase() != null) {
            prixBaseStandard = match.getPrixBase();
        }
        return prixBaseStandard * (coefficientPrix != null ? coefficientPrix : 1.0);
    }

    @Override
    public String toString() {
        return nom + " (" + typeZone + ")";
    }
}
