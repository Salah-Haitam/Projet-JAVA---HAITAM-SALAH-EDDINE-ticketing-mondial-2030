package com.mondial2030.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un match du Mondial 2030.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
@Entity
@Table(name = "match_football")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "numero_match", unique = true, length = 20)
    private String numeroMatch;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipe_domicile_id")
    private Equipe equipeDomicile;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipe_exterieur_id")
    private Equipe equipeExterieur;

    @Column(name = "date_heure")
    private LocalDateTime dateHeure;

    @Column(name = "stade", length = 150)
    private String stade;

    @Column(name = "ville", length = 100)
    private String ville;

    @Column(name = "pays", length = 100)
    private String pays;

    @Enumerated(EnumType.STRING)
    @Column(name = "phase")
    private PhaseMatch phase;

    @Column(name = "groupe", length = 1)
    private String groupe;

    @Column(name = "score_domicile")
    private Integer scoreDomicile;

    @Column(name = "score_exterieur")
    private Integer scoreExterieur;

    @Column(name = "termine")
    private Boolean termine;

    @Column(name = "capacite_stade")
    private Integer capaciteStade;

    @Column(name = "tickets_disponibles")
    private Integer ticketsDisponibles;

    @Column(name = "prix_base")
    private Double prixBase;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Zone> zones = new ArrayList<>();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FluxSpectateurs> fluxSpectateurs = new ArrayList<>();

    // Constructeur par défaut
    public Match() {
        this.termine = false;
        this.scoreDomicile = 0;
        this.scoreExterieur = 0;
    }

    // Constructeur avec paramètres
    public Match(Equipe equipeDomicile, Equipe equipeExterieur, LocalDateTime dateHeure, 
                 String stade, String ville, PhaseMatch phase, Double prixBase) {
        this();
        this.equipeDomicile = equipeDomicile;
        this.equipeExterieur = equipeExterieur;
        this.dateHeure = dateHeure;
        this.stade = stade;
        this.ville = ville;
        this.phase = phase;
        this.prixBase = prixBase;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroMatch() {
        return numeroMatch;
    }

    public void setNumeroMatch(String numeroMatch) {
        this.numeroMatch = numeroMatch;
    }

    public Equipe getEquipeDomicile() {
        return equipeDomicile;
    }

    public void setEquipeDomicile(Equipe equipeDomicile) {
        this.equipeDomicile = equipeDomicile;
    }

    public Equipe getEquipeExterieur() {
        return equipeExterieur;
    }

    public void setEquipeExterieur(Equipe equipeExterieur) {
        this.equipeExterieur = equipeExterieur;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public String getStade() {
        return stade;
    }

    public void setStade(String stade) {
        this.stade = stade;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public PhaseMatch getPhase() {
        return phase;
    }

    public void setPhase(PhaseMatch phase) {
        this.phase = phase;
    }

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public Integer getScoreDomicile() {
        return scoreDomicile;
    }

    public void setScoreDomicile(Integer scoreDomicile) {
        this.scoreDomicile = scoreDomicile;
    }

    public Integer getScoreExterieur() {
        return scoreExterieur;
    }

    public void setScoreExterieur(Integer scoreExterieur) {
        this.scoreExterieur = scoreExterieur;
    }

    public Boolean getTermine() {
        return termine;
    }

    public void setTermine(Boolean termine) {
        this.termine = termine;
    }

    public Integer getCapaciteStade() {
        return capaciteStade;
    }

    public void setCapaciteStade(Integer capaciteStade) {
        this.capaciteStade = capaciteStade;
        this.ticketsDisponibles = capaciteStade;
    }

    public Integer getTicketsDisponibles() {
        return ticketsDisponibles;
    }

    public void setTicketsDisponibles(Integer ticketsDisponibles) {
        this.ticketsDisponibles = ticketsDisponibles;
    }

    /**
     * Retourne le nombre de places disponibles (alias pour getTicketsDisponibles)
     */
    public int getPlacesDisponibles() {
        return ticketsDisponibles != null ? ticketsDisponibles : 0;
    }

    public Double getPrixBase() {
        return prixBase;
    }

    public void setPrixBase(Double prixBase) {
        this.prixBase = prixBase;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public List<Zone> getZones() {
        return zones;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }

    public List<FluxSpectateurs> getFluxSpectateurs() {
        return fluxSpectateurs;
    }

    public void setFluxSpectateurs(List<FluxSpectateurs> fluxSpectateurs) {
        this.fluxSpectateurs = fluxSpectateurs;
    }

    /**
     * Retourne le libellé du match
     */
    public String getLibelleMatch() {
        String domicile = equipeDomicile != null ? equipeDomicile.getNom() : "TBD";
        String exterieur = equipeExterieur != null ? equipeExterieur.getNom() : "TBD";
        return domicile + " vs " + exterieur;
    }

    /**
     * Vérifie si le match est terminé (méthode utilitaire)
     */
    public boolean isTermine() {
        return Boolean.TRUE.equals(termine);
    }

    /**
     * Retourne le score formaté
     */
    public String getScoreFormate() {
        if (termine) {
            return scoreDomicile + " - " + scoreExterieur;
        }
        return "- : -";
    }

    /**
     * Vérifie si le match est à venir
     */
    public boolean estAVenir() {
        return dateHeure != null && dateHeure.isAfter(LocalDateTime.now()) && !termine;
    }

    /**
     * Calcule le taux de remplissage
     */
    public double getTauxRemplissage() {
        if (capaciteStade == null || capaciteStade == 0) return 0;
        int ticketsVendus = capaciteStade - (ticketsDisponibles != null ? ticketsDisponibles : 0);
        return (double) ticketsVendus / capaciteStade * 100;
    }

    /**
     * Décrémente le nombre de tickets disponibles
     */
    public void decrementerTicketsDisponibles() {
        if (ticketsDisponibles != null && ticketsDisponibles > 0) {
            ticketsDisponibles--;
        }
    }

    /**
     * Incrémente le nombre de tickets disponibles
     */
    public void incrementerTicketsDisponibles() {
        if (ticketsDisponibles != null && capaciteStade != null && ticketsDisponibles < capaciteStade) {
            ticketsDisponibles++;
        }
    }

    @Override
    public String toString() {
        return getLibelleMatch() + " - " + (dateHeure != null ? dateHeure.toString() : "Date TBD");
    }
}
