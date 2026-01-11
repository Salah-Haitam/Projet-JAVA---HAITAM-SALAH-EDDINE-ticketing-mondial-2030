package com.mondial2030.entity;

/**
 * Énumération des types d'alertes du système.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public enum TypeAlerte {
    /**
     * Alerte de surpopulation dans une zone
     */
    SURPOPULATION("Surpopulation"),
    
    /**
     * Alerte de sécurité
     */
    SECURITE("Sécurité"),
    
    /**
     * Alerte technique
     */
    TECHNIQUE("Technique"),
    
    /**
     * Alerte de fraude détectée
     */
    FRAUDE("Fraude"),
    
    /**
     * Alerte d'urgence
     */
    URGENCE("Urgence"),
    
    /**
     * Alerte informative
     */
    INFORMATION("Information"),
    
    /**
     * Alerte de maintenance
     */
    MAINTENANCE("Maintenance"),
    
    /**
     * Signalement d'un utilisateur (spectateur)
     */
    SIGNALEMENT_UTILISATEUR("Signalement Utilisateur"),
    
    /**
     * Question d'un utilisateur (spectateur)
     */
    QUESTION_UTILISATEUR("Question Utilisateur"),
    
    /**
     * Réclamation d'un utilisateur
     */
    RECLAMATION("Réclamation"),
    
    /**
     * Demande d'assistance
     */
    ASSISTANCE("Demande d'assistance");

    private final String libelle;

    TypeAlerte(String libelle) {
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
