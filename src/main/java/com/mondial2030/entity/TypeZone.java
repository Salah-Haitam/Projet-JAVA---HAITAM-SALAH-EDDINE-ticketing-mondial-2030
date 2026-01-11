package com.mondial2030.entity;

/**
 * Énumération des types de zones dans le stade.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public enum TypeZone {
    /**
     * Tribune Nord
     */
    TRIBUNE_NORD("Tribune Nord"),
    
    /**
     * Tribune Sud
     */
    TRIBUNE_SUD("Tribune Sud"),
    
    /**
     * Tribune Est
     */
    TRIBUNE_EST("Tribune Est"),
    
    /**
     * Tribune Ouest
     */
    TRIBUNE_OUEST("Tribune Ouest"),
    
    /**
     * Zone VIP
     */
    VIP("Zone VIP"),
    
    /**
     * Zone Presse
     */
    PRESSE("Zone Presse"),
    
    /**
     * Zone PMR (Personnes à Mobilité Réduite)
     */
    PMR("Zone PMR"),
    
    /**
     * Zone Famille
     */
    FAMILLE("Zone Famille"),
    
    /**
     * Virage
     */
    VIRAGE("Virage"),
    
    /**
     * Tribune générique
     */
    TRIBUNE("Tribune"),
    
    /**
     * Zone Pelouse
     */
    PELOUSE("Pelouse");

    private final String libelle;

    TypeZone(String libelle) {
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
