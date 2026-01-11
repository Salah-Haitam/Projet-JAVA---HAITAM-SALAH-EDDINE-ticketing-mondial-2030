package com.mondial2030.entity;

/**
 * Énumération des types de transaction.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public enum TypeTransaction {
    /**
     * Achat d'un nouveau ticket
     */
    ACHAT("Achat"),
    
    /**
     * Transfert de ticket à un autre spectateur
     */
    TRANSFERT("Transfert"),
    
    /**
     * Remboursement d'un ticket
     */
    REMBOURSEMENT("Remboursement"),
    
    /**
     * Annulation de transaction
     */
    ANNULATION("Annulation"),
    
    /**
     * Modification de ticket
     */
    MODIFICATION("Modification");

    private final String libelle;

    TypeTransaction(String libelle) {
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
