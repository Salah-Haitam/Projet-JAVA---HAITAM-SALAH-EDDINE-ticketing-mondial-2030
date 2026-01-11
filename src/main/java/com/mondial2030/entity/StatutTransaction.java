package com.mondial2030.entity;

/**
 * Énumération des statuts possibles d'une transaction.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public enum StatutTransaction {
    /**
     * Transaction en attente de paiement
     */
    EN_ATTENTE("En attente"),
    
    /**
     * Transaction en cours de traitement
     */
    EN_COURS("En cours"),
    
    /**
     * Transaction validée et complétée
     */
    VALIDEE("Validée"),
    
    /**
     * Transaction refusée
     */
    REFUSEE("Refusée"),
    
    /**
     * Transaction annulée
     */
    ANNULEE("Annulée"),
    
    /**
     * Transaction remboursée
     */
    REMBOURSEE("Remboursée"),
    
    /**
     * Transaction expirée
     */
    EXPIREE("Expirée");

    private final String libelle;

    StatutTransaction(String libelle) {
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
