package com.mondial2030.entity;

/**
 * Énumération des statuts possibles d'un ticket.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public enum StatutTicket {
    /**
     * Ticket réservé mais non payé
     */
    RESERVE("Réservé"),
    
    /**
     * Ticket payé et valide
     */
    VALIDE("Valide"),
    
    /**
     * Ticket utilisé (scanné à l'entrée)
     */
    UTILISE("Utilisé"),
    
    /**
     * Ticket annulé
     */
    ANNULE("Annulé"),
    
    /**
     * Ticket expiré
     */
    EXPIRE("Expiré"),
    
    /**
     * Ticket en cours de transfert
     */
    EN_TRANSFERT("En transfert"),
    
    /**
     * Ticket remboursé
     */
    REMBOURSE("Remboursé");

    private final String libelle;

    StatutTicket(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
