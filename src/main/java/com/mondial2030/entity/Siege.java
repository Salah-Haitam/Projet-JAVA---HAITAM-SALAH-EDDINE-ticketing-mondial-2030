package com.mondial2030.entity;

import jakarta.persistence.*;

/**
 * Entité représentant un siège dans une zone du stade.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
@Entity
@Table(name = "siege")
public class Siege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "numero_siege", nullable = false, length = 20)
    private String numeroSiege;

    @Column(name = "rangee", length = 10)
    private String rangee;

    @Column(name = "numero_dans_rangee")
    private Integer numeroDansRangee;

    @Column(name = "disponible")
    private Boolean disponible;

    @Column(name = "type_siege", length = 50)
    private String typeSiege;

    @Column(name = "accessible_pmr")
    private Boolean accessiblePmr;

    @Column(name = "position_x")
    private Double positionX;

    @Column(name = "position_y")
    private Double positionY;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @OneToOne(mappedBy = "siege", fetch = FetchType.LAZY)
    private Ticket ticket;

    // Constructeur par défaut
    public Siege() {
        this.disponible = true;
        this.accessiblePmr = false;
    }

    // Constructeur avec paramètres
    public Siege(String numeroSiege, String rangee, Integer numeroDansRangee, Zone zone) {
        this();
        this.numeroSiege = numeroSiege;
        this.rangee = rangee;
        this.numeroDansRangee = numeroDansRangee;
        this.zone = zone;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroSiege() {
        return numeroSiege;
    }

    public void setNumeroSiege(String numeroSiege) {
        this.numeroSiege = numeroSiege;
    }

    public String getRangee() {
        return rangee;
    }

    public void setRangee(String rangee) {
        this.rangee = rangee;
    }

    public Integer getNumeroDansRangee() {
        return numeroDansRangee;
    }

    public void setNumeroDansRangee(Integer numeroDansRangee) {
        this.numeroDansRangee = numeroDansRangee;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public String getTypeSiege() {
        return typeSiege;
    }

    public void setTypeSiege(String typeSiege) {
        this.typeSiege = typeSiege;
    }

    public Boolean getAccessiblePmr() {
        return accessiblePmr;
    }

    public void setAccessiblePmr(Boolean accessiblePmr) {
        this.accessiblePmr = accessiblePmr;
    }

    public Double getPositionX() {
        return positionX;
    }

    public void setPositionX(Double positionX) {
        this.positionX = positionX;
    }

    public Double getPositionY() {
        return positionY;
    }

    public void setPositionY(Double positionY) {
        this.positionY = positionY;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    /**
     * Retourne la référence complète du siège
     */
    public String getReferenceComplete() {
        String zoneNom = zone != null ? zone.getNom() : "?";
        return zoneNom + " - " + rangee + numeroSiege;
    }

    /**
     * Retourne le numéro du siège (alias pour getNumeroSiege)
     */
    public String getNumero() {
        return numeroSiege;
    }

    /**
     * Réserve le siège
     */
    public void reserver() {
        this.disponible = false;
    }

    /**
     * Libère le siège
     */
    public void liberer() {
        this.disponible = true;
    }

    @Override
    public String toString() {
        return "Siège " + rangee + numeroSiege;
    }
}
