package com.mondial2030.entity;

/**
 * Énumération des niveaux de priorité des alertes.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public enum NiveauAlerte {
    /**
     * Niveau faible - Information
     */
    FAIBLE("Faible", 1),
    
    /**
     * Niveau moyen - Attention requise
     */
    MOYEN("Moyen", 2),
    
    /**
     * Niveau élevé - Action nécessaire
     */
    ELEVE("Élevé", 3),
    
    /**
     * Niveau critique - Urgence
     */
    CRITIQUE("Critique", 4);

    private final String libelle;
    private final int niveau;

    NiveauAlerte(String libelle, int niveau) {
        this.libelle = libelle;
        this.niveau = niveau;
    }

    public String getLibelle() {
        return libelle;
    }

    public int getNiveau() {
        return niveau;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
