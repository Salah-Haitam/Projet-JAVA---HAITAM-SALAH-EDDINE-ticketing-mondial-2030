package com.mondial2030.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un spectateur du système.
 * Hérite de Utilisateur et peut acheter/transférer des tickets.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
@Entity
@Table(name = "spectateur")
@DiscriminatorValue("SPECTATEUR")
@PrimaryKeyJoinColumn(name = "utilisateur_id")
public class Spectateur extends Utilisateur {

    @Column(name = "nationalite", length = 50)
    private String nationalite;

    @Column(name = "numero_passeport", length = 50)
    private String numeroPasseport;

    @Column(name = "date_naissance")
    private String dateNaissance;

    @Column(name = "adresse", length = 255)
    private String adresse;

    @Column(name = "ville", length = 100)
    private String ville;

    @Column(name = "pays", length = 100)
    private String pays;

    @Column(name = "code_postal", length = 20)
    private String codePostal;

    @Column(name = "nombre_tickets_achetes")
    private Integer nombreTicketsAchetes;

    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "spectateur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    // Constructeur par défaut
    public Spectateur() {
        super();
        this.nombreTicketsAchetes = 0;
    }

    // Constructeur avec paramètres
    public Spectateur(String nom, String prenom, String email, String motDePasse, String nationalite) {
        super(nom, prenom, email, motDePasse);
        this.nationalite = nationalite;
        this.nombreTicketsAchetes = 0;
    }

    // Getters et Setters
    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getNumeroPasseport() {
        return numeroPasseport;
    }

    public void setNumeroPasseport(String numeroPasseport) {
        this.numeroPasseport = numeroPasseport;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
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

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public Integer getNombreTicketsAchetes() {
        return nombreTicketsAchetes;
    }

    public void setNombreTicketsAchetes(Integer nombreTicketsAchetes) {
        this.nombreTicketsAchetes = nombreTicketsAchetes;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String getTypeUtilisateur() {
        return "SPECTATEUR";
    }

    /**
     * Ajoute un ticket à la liste des tickets du spectateur
     */
    public void ajouterTicket(Ticket ticket) {
        tickets.add(ticket);
        ticket.setProprietaire(this);
        nombreTicketsAchetes++;
    }

    /**
     * Retire un ticket de la liste des tickets du spectateur
     */
    public void retirerTicket(Ticket ticket) {
        tickets.remove(ticket);
        ticket.setProprietaire(null);
    }

    /**
     * Retourne le nombre de tickets actifs
     */
    public int getNombreTicketsActifs() {
        return (int) tickets.stream()
                .filter(t -> t.getStatut() == StatutTicket.VALIDE)
                .count();
    }

    @Override
    public String toString() {
        return "Spectateur{" +
                "id=" + getId() +
                ", nom='" + getNom() + '\'' +
                ", nationalite='" + nationalite + '\'' +
                ", ticketsAchetes=" + nombreTicketsAchetes +
                '}';
    }
}
