package com.mondial2030.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité représentant un rapport généré par le système.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
@Entity
@Table(name = "rapport")
public class Rapport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titre", nullable = false, length = 200)
    private String titre;

    @Column(name = "type_rapport", length = 50)
    private String typeRapport;

    @Column(name = "date_generation")
    private LocalDateTime dateGeneration;

    @Column(name = "date_debut_periode")
    private LocalDateTime dateDebutPeriode;

    @Column(name = "date_fin_periode")
    private LocalDateTime dateFinPeriode;

    @Column(name = "contenu", columnDefinition = "TEXT")
    private String contenu;

    @Column(name = "chemin_fichier", length = 500)
    private String cheminFichier;

    @Column(name = "format", length = 20)
    private String format;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genere_par_id")
    private Administrateur generePar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    // Statistiques du rapport
    @Column(name = "total_tickets_vendus")
    private Integer totalTicketsVendus;

    @Column(name = "total_revenus")
    private Double totalRevenus;

    @Column(name = "taux_remplissage")
    private Double tauxRemplissage;

    @Column(name = "nombre_alertes")
    private Integer nombreAlertes;

    // Constructeur par défaut
    public Rapport() {
        this.dateGeneration = LocalDateTime.now();
        this.format = "PDF";
    }

    // Constructeur avec paramètres
    public Rapport(String titre, String typeRapport, Administrateur generePar) {
        this();
        this.titre = titre;
        this.typeRapport = typeRapport;
        this.generePar = generePar;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getTypeRapport() {
        return typeRapport;
    }

    public void setTypeRapport(String typeRapport) {
        this.typeRapport = typeRapport;
    }

    public LocalDateTime getDateGeneration() {
        return dateGeneration;
    }

    public void setDateGeneration(LocalDateTime dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public LocalDateTime getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    public void setDateDebutPeriode(LocalDateTime dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }

    public LocalDateTime getDateFinPeriode() {
        return dateFinPeriode;
    }

    public void setDateFinPeriode(LocalDateTime dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getCheminFichier() {
        return cheminFichier;
    }

    public void setCheminFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Administrateur getGenerePar() {
        return generePar;
    }

    public void setGenerePar(Administrateur generePar) {
        this.generePar = generePar;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Integer getTotalTicketsVendus() {
        return totalTicketsVendus;
    }

    public void setTotalTicketsVendus(Integer totalTicketsVendus) {
        this.totalTicketsVendus = totalTicketsVendus;
    }

    public Double getTotalRevenus() {
        return totalRevenus;
    }

    public void setTotalRevenus(Double totalRevenus) {
        this.totalRevenus = totalRevenus;
    }

    public Double getTauxRemplissage() {
        return tauxRemplissage;
    }

    public void setTauxRemplissage(Double tauxRemplissage) {
        this.tauxRemplissage = tauxRemplissage;
    }

    public Integer getNombreAlertes() {
        return nombreAlertes;
    }

    public void setNombreAlertes(Integer nombreAlertes) {
        this.nombreAlertes = nombreAlertes;
    }

    /**
     * Retourne les revenus formatés
     */
    public String getRevenusFormates() {
        return totalRevenus != null ? String.format("%.2f EUR", totalRevenus) : "N/A";
    }

    /**
     * Retourne le taux de remplissage formaté
     */
    public String getTauxRemplissageFormate() {
        return tauxRemplissage != null ? String.format("%.1f%%", tauxRemplissage) : "N/A";
    }

    @Override
    public String toString() {
        return "Rapport{" +
                "titre='" + titre + '\'' +
                ", type='" + typeRapport + '\'' +
                ", dateGeneration=" + dateGeneration +
                '}';
    }
}
