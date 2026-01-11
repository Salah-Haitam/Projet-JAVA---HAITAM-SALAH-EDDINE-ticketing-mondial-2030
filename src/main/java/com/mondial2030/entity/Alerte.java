package com.mondial2030.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité représentant une alerte du système.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
@Entity
@Table(name = "alerte")
public class Alerte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titre", nullable = false, length = 200)
    private String titre;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_alerte")
    private TypeAlerte typeAlerte;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau")
    private NiveauAlerte niveau;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "date_resolution")
    private LocalDateTime dateResolution;

    @Column(name = "resolue")
    private Boolean resolue;

    @Column(name = "source", length = 100)
    private String source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolue_par_id")
    private Administrateur resoluePar;

    @Column(name = "commentaire_resolution", length = 500)
    private String commentaireResolution;

    // Champs pour les alertes créées par les utilisateurs
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creee_par_id")
    private Spectateur creeePar;

    @Column(name = "reponse_admin", columnDefinition = "TEXT")
    private String reponseAdmin;

    @Column(name = "lue_par_admin")
    private Boolean lueParAdmin;

    @Column(name = "date_lecture")
    private LocalDateTime dateLecture;

    @Column(name = "date_reponse")
    private LocalDateTime dateReponse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repondu_par_id")
    private Administrateur reponduPar;

    // Constructeur par défaut
    public Alerte() {
        this.dateCreation = LocalDateTime.now();
        this.resolue = false;
        this.lueParAdmin = false;
    }

    // Constructeur avec paramètres
    public Alerte(String titre, String message, TypeAlerte typeAlerte, NiveauAlerte niveau) {
        this();
        this.titre = titre;
        this.message = message;
        this.typeAlerte = typeAlerte;
        this.niveau = niveau;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TypeAlerte getTypeAlerte() {
        return typeAlerte;
    }

    public void setTypeAlerte(TypeAlerte typeAlerte) {
        this.typeAlerte = typeAlerte;
    }

    public NiveauAlerte getNiveau() {
        return niveau;
    }

    public void setNiveau(NiveauAlerte niveau) {
        this.niveau = niveau;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateResolution() {
        return dateResolution;
    }

    public void setDateResolution(LocalDateTime dateResolution) {
        this.dateResolution = dateResolution;
    }

    public Boolean getResolue() {
        return resolue;
    }

    public void setResolue(Boolean resolue) {
        this.resolue = resolue;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public Administrateur getResoluePar() {
        return resoluePar;
    }

    public void setResoluePar(Administrateur resoluePar) {
        this.resoluePar = resoluePar;
    }

    public String getCommentaireResolution() {
        return commentaireResolution;
    }

    public void setCommentaireResolution(String commentaireResolution) {
        this.commentaireResolution = commentaireResolution;
    }

    // Getters et Setters pour les nouveaux champs
    public Spectateur getCreeePar() {
        return creeePar;
    }

    public void setCreeePar(Spectateur creeePar) {
        this.creeePar = creeePar;
    }

    public String getReponseAdmin() {
        return reponseAdmin;
    }

    public void setReponseAdmin(String reponseAdmin) {
        this.reponseAdmin = reponseAdmin;
    }

    public Boolean getLueParAdmin() {
        return lueParAdmin;
    }

    public void setLueParAdmin(Boolean lueParAdmin) {
        this.lueParAdmin = lueParAdmin;
    }

    public LocalDateTime getDateLecture() {
        return dateLecture;
    }

    public void setDateLecture(LocalDateTime dateLecture) {
        this.dateLecture = dateLecture;
    }

    public LocalDateTime getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse(LocalDateTime dateReponse) {
        this.dateReponse = dateReponse;
    }

    public Administrateur getReponduPar() {
        return reponduPar;
    }

    public void setReponduPar(Administrateur reponduPar) {
        this.reponduPar = reponduPar;
    }

    /**
     * Vérifie si l'alerte a été créée par un utilisateur
     */
    public boolean estCreeeParUtilisateur() {
        return creeePar != null;
    }

    /**
     * Vérifie si l'alerte a été lue par l'admin
     */
    public boolean estLue() {
        return Boolean.TRUE.equals(lueParAdmin);
    }

    /**
     * Vérifie si l'alerte a une réponse
     */
    public boolean aReponse() {
        return reponseAdmin != null && !reponseAdmin.trim().isEmpty();
    }

    /**
     * Marque l'alerte comme lue par l'admin
     */
    public void marquerCommeLue(Administrateur admin) {
        this.lueParAdmin = true;
        this.dateLecture = LocalDateTime.now();
    }

    /**
     * Ajoute une réponse de l'administrateur
     */
    public void repondre(Administrateur admin, String reponse) {
        this.reponseAdmin = reponse;
        this.dateReponse = LocalDateTime.now();
        this.reponduPar = admin;
        this.resolue = true;
        this.dateResolution = LocalDateTime.now();
        this.resoluePar = admin;
    }

    /**
     * Résout l'alerte
     */
    public void resoudre(Administrateur admin, String commentaire) {
        this.resolue = true;
        this.dateResolution = LocalDateTime.now();
        this.resoluePar = admin;
        this.commentaireResolution = commentaire;
    }

    /**
     * Vérifie si l'alerte est critique
     */
    public boolean estCritique() {
        return niveau == NiveauAlerte.CRITIQUE || niveau == NiveauAlerte.ELEVE;
    }

    /**
     * Vérifie si l'alerte est active (non résolue)
     */
    public boolean estActive() {
        return !Boolean.TRUE.equals(resolue);
    }

    /**
     * Vérifie si l'alerte est résolue (méthode utilitaire)
     */
    public boolean isResolue() {
        return Boolean.TRUE.equals(resolue);
    }

    /**
     * Retourne le type d'alerte (alias pour getTypeAlerte)
     */
    public TypeAlerte getType() {
        return typeAlerte;
    }

    @Override
    public String toString() {
        return "Alerte{" +
                "titre='" + titre + '\'' +
                ", type=" + typeAlerte +
                ", niveau=" + niveau +
                ", resolue=" + resolue +
                '}';
    }
}
