package com.mondial2030.entity;

/**
 * Énumération des catégories de tickets disponibles.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public enum CategorieTicket {
    /**
     * Catégorie standard - Prix de base
     */
    STANDARD("Standard", 1.0),
    
    /**
     * Catégorie Premium - Meilleures places
     */
    PREMIUM("Premium", 2.5),
    
    /**
     * Catégorie VIP - Accès exclusif
     */
    VIP("VIP", 5.0),
    
    /**
     * Catégorie Famille - Tarif réduit pour les familles
     */
    FAMILLE("Famille", 0.8),
    
    /**
     * Catégorie PMR - Places pour personnes à mobilité réduite
     */
    PMR("PMR", 0.7),
    
    /**
     * Catégorie Étudiant - Tarif réduit
     */
    ETUDIANT("Étudiant", 0.6);

    private final String libelle;
    private final double coefficientPrix;

    CategorieTicket(String libelle, double coefficientPrix) {
        this.libelle = libelle;
        this.coefficientPrix = coefficientPrix;
    }

    public String getLibelle() {
        return libelle;
    }

    public double getCoefficientPrix() {
        return coefficientPrix;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
