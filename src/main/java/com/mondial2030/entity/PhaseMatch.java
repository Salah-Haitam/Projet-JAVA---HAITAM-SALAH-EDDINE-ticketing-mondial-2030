package com.mondial2030.entity;

/**
 * Énumération des phases de la compétition.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public enum PhaseMatch {
    /**
     * Phase de groupes
     */
    PHASE_GROUPES("Phase de groupes"),
    
    /**
     * Huitièmes de finale
     */
    HUITIEMES("Huitièmes de finale"),
    
    /**
     * Quarts de finale
     */
    QUARTS("Quarts de finale"),
    
    /**
     * Demi-finales
     */
    DEMI_FINALES("Demi-finales"),
    
    /**
     * Match pour la troisième place
     */
    PETITE_FINALE("Petite finale"),
    
    /**
     * Finale
     */
    FINALE("Finale");

    private final String libelle;

    PhaseMatch(String libelle) {
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
