package com.mondial2030.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité représentant une transaction financière.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
@Entity
@Table(name = "transaction_paiement")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "numero_transaction", unique = true, nullable = false, length = 50)
    private String numeroTransaction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "spectateur_id")
    private Spectateur spectateur;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_transaction")
    private TypeTransaction typeTransaction;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutTransaction statut;

    @Column(name = "montant")
    private Double montant;

    @Column(name = "devise", length = 3)
    private String devise;

    @Column(name = "methode_paiement", length = 50)
    private String methodePaiement;

    @Column(name = "reference_paiement", length = 100)
    private String referencePaiement;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "date_validation")
    private LocalDateTime dateValidation;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "ip_adresse", length = 50)
    private String ipAdresse;

    // Pour les transferts
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spectateur_source_id")
    private Spectateur spectateurSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spectateur_destination_id")
    private Spectateur spectateurDestination;

    // Constructeur par défaut
    public Transaction() {
        this.numeroTransaction = genererNumeroTransaction();
        this.dateCreation = LocalDateTime.now();
        this.statut = StatutTransaction.EN_ATTENTE;
        this.devise = "EUR";
    }

    // Constructeur avec paramètres
    public Transaction(Spectateur spectateur, Ticket ticket, TypeTransaction typeTransaction, Double montant) {
        this();
        this.spectateur = spectateur;
        this.ticket = ticket;
        this.typeTransaction = typeTransaction;
        this.montant = montant;
    }

    /**
     * Génère un numéro de transaction unique
     */
    private String genererNumeroTransaction() {
        return "TRX-" + System.currentTimeMillis() + "-" + 
               String.format("%04d", (int)(Math.random() * 10000));
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroTransaction() {
        return numeroTransaction;
    }

    public void setNumeroTransaction(String numeroTransaction) {
        this.numeroTransaction = numeroTransaction;
    }

    public Spectateur getSpectateur() {
        return spectateur;
    }

    public void setSpectateur(Spectateur spectateur) {
        this.spectateur = spectateur;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public TypeTransaction getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(TypeTransaction typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public StatutTransaction getStatut() {
        return statut;
    }

    public void setStatut(StatutTransaction statut) {
        this.statut = statut;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public String getMethodePaiement() {
        return methodePaiement;
    }

    public void setMethodePaiement(String methodePaiement) {
        this.methodePaiement = methodePaiement;
    }

    public String getReferencePaiement() {
        return referencePaiement;
    }

    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDateTime dateValidation) {
        this.dateValidation = dateValidation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIpAdresse() {
        return ipAdresse;
    }

    public void setIpAdresse(String ipAdresse) {
        this.ipAdresse = ipAdresse;
    }

    public Spectateur getSpectateurSource() {
        return spectateurSource;
    }

    public void setSpectateurSource(Spectateur spectateurSource) {
        this.spectateurSource = spectateurSource;
    }

    public Spectateur getSpectateurDestination() {
        return spectateurDestination;
    }

    public void setSpectateurDestination(Spectateur spectateurDestination) {
        this.spectateurDestination = spectateurDestination;
    }

    /**
     * Valide la transaction
     */
    public void valider() {
        this.statut = StatutTransaction.VALIDEE;
        this.dateValidation = LocalDateTime.now();
    }

    /**
     * Refuse la transaction
     */
    public void refuser() {
        this.statut = StatutTransaction.REFUSEE;
    }

    /**
     * Annule la transaction
     */
    public void annuler() {
        this.statut = StatutTransaction.ANNULEE;
    }

    /**
     * Rembourse la transaction
     */
    public void rembourser() {
        this.statut = StatutTransaction.REMBOURSEE;
    }

    /**
     * Vérifie si la transaction peut être remboursée
     */
    public boolean peutEtreRemboursee() {
        return statut == StatutTransaction.VALIDEE && 
               ticket != null && 
               ticket.getMatch() != null && 
               ticket.getMatch().estAVenir();
    }

    /**
     * Retourne le montant formaté
     */
    public String getMontantFormate() {
        return String.format("%.2f %s", montant, devise);
    }

    /**
     * Retourne la date de la transaction (alias pour getDateCreation)
     */
    public LocalDateTime getDateTransaction() {
        return dateCreation;
    }

    /**
     * Retourne le type de transaction (alias pour getTypeTransaction)
     */
    public TypeTransaction getType() {
        return typeTransaction;
    }

    /**
     * Retourne la référence externe (alias pour getReferencePaiement)
     */
    public String getReferenceExterne() {
        return referencePaiement;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "numero='" + numeroTransaction + '\'' +
                ", type=" + typeTransaction +
                ", statut=" + statut +
                ", montant=" + getMontantFormate() +
                '}';
    }
}
