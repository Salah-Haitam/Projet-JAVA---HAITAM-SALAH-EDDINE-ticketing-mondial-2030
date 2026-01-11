package com.mondial2030.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité représentant un ticket pour un match.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "numero_ticket", unique = true, nullable = false, length = 50)
    private String numeroTicket;

    @Column(name = "code_qr", unique = true, length = 255)
    private String codeQR;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proprietaire_id")
    private Spectateur proprietaire;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "siege_id")
    private Siege siege;

    @Enumerated(EnumType.STRING)
    @Column(name = "categorie")
    private CategorieTicket categorie;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutTicket statut;

    @Column(name = "prix")
    private Double prix;

    @Column(name = "date_achat")
    private LocalDateTime dateAchat;

    @Column(name = "date_validation")
    private LocalDateTime dateValidation;

    @Column(name = "date_expiration")
    private LocalDateTime dateExpiration;

    @Column(name = "transferable")
    private Boolean transferable;

    @Column(name = "nombre_transferts")
    private Integer nombreTransferts;

    @Column(name = "porte_entree", length = 50)
    private String porteEntree;

    // Constructeur par défaut
    public Ticket() {
        this.numeroTicket = genererNumeroTicket();
        this.codeQR = genererCodeQR();
        this.statut = StatutTicket.RESERVE;
        this.transferable = true;
        this.nombreTransferts = 0;
    }

    // Constructeur avec paramètres
    public Ticket(Match match, Spectateur proprietaire, CategorieTicket categorie, Double prix) {
        this();
        this.match = match;
        this.proprietaire = proprietaire;
        this.categorie = categorie;
        this.prix = prix;
    }

    /**
     * Génère un numéro de ticket unique
     */
    private String genererNumeroTicket() {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        return "WC2030-" + uuid;
    }

    /**
     * Génère un code QR unique
     */
    private String genererCodeQR() {
        return UUID.randomUUID().toString();
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroTicket() {
        return numeroTicket;
    }

    public void setNumeroTicket(String numeroTicket) {
        this.numeroTicket = numeroTicket;
    }

    public String getCodeQR() {
        return codeQR;
    }

    public void setCodeQR(String codeQR) {
        this.codeQR = codeQR;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Spectateur getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Spectateur proprietaire) {
        this.proprietaire = proprietaire;
    }

    public Siege getSiege() {
        return siege;
    }

    public void setSiege(Siege siege) {
        this.siege = siege;
    }

    public CategorieTicket getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieTicket categorie) {
        this.categorie = categorie;
    }

    public StatutTicket getStatut() {
        return statut;
    }

    public void setStatut(StatutTicket statut) {
        this.statut = statut;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public LocalDateTime getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(LocalDateTime dateAchat) {
        this.dateAchat = dateAchat;
    }

    public LocalDateTime getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDateTime dateValidation) {
        this.dateValidation = dateValidation;
    }

    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Boolean getTransferable() {
        return transferable;
    }

    public void setTransferable(Boolean transferable) {
        this.transferable = transferable;
    }

    public Integer getNombreTransferts() {
        return nombreTransferts;
    }

    public void setNombreTransferts(Integer nombreTransferts) {
        this.nombreTransferts = nombreTransferts;
    }

    public String getPorteEntree() {
        return porteEntree;
    }

    public void setPorteEntree(String porteEntree) {
        this.porteEntree = porteEntree;
    }

    /**
     * Valide le ticket
     */
    public void valider() {
        this.statut = StatutTicket.VALIDE;
        this.dateValidation = LocalDateTime.now();
    }

    /**
     * Utilise le ticket (scanné à l'entrée)
     */
    public void utiliser() {
        this.statut = StatutTicket.UTILISE;
    }

    /**
     * Annule le ticket
     */
    public void annuler() {
        this.statut = StatutTicket.ANNULE;
    }

    /**
     * Vérifie si le ticket peut être transféré
     */
    public boolean peutEtreTransfere() {
        return transferable && 
               statut == StatutTicket.VALIDE && 
               nombreTransferts < 3 && 
               match != null && 
               match.estAVenir();
    }

    /**
     * Effectue un transfert vers un nouveau propriétaire
     */
    public void transfererVers(Spectateur nouveauProprietaire) {
        if (peutEtreTransfere()) {
            this.proprietaire = nouveauProprietaire;
            this.nombreTransferts++;
            this.codeQR = genererCodeQR(); // Nouveau QR code après transfert
        }
    }

    /**
     * Vérifie si le ticket est valide
     */
    public boolean estValide() {
        return statut == StatutTicket.VALIDE && 
               (dateExpiration == null || dateExpiration.isAfter(LocalDateTime.now()));
    }

    /**
     * Retourne la zone du ticket (via le siège s'il existe)
     */
    public Zone getZone() {
        return siege != null ? siege.getZone() : null;
    }

    /**
     * Vérifie si le ticket est transférable (alias)
     */
    public boolean isTransferable() {
        return Boolean.TRUE.equals(transferable);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "numero='" + numeroTicket + '\'' +
                ", match=" + (match != null ? match.getLibelleMatch() : "N/A") +
                ", statut=" + statut +
                ", prix=" + prix +
                '}';
    }
}
