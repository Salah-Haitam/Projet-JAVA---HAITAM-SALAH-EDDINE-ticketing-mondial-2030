package com.mondial2030.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un administrateur du système.
 * Hérite de Utilisateur avec des privilèges étendus.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
@Entity
@Table(name = "administrateur")
@DiscriminatorValue("ADMIN")
@PrimaryKeyJoinColumn(name = "utilisateur_id")
public class Administrateur extends Utilisateur {

    @Column(name = "niveau_acces")
    private Integer niveauAcces;

    @Column(name = "departement", length = 100)
    private String departement;

    @Column(name = "peut_gerer_utilisateurs")
    private Boolean peutGererUtilisateurs;

    @Column(name = "peut_gerer_matchs")
    private Boolean peutGererMatchs;

    @Column(name = "peut_generer_rapports")
    private Boolean peutGenererRapports;

    @Column(name = "peut_voir_alertes")
    private Boolean peutVoirAlertes;

    @OneToMany(mappedBy = "generePar", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rapport> rapportsGeneres = new ArrayList<>();

    // Constructeur par défaut
    public Administrateur() {
        super();
        this.niveauAcces = 1;
        this.peutGererUtilisateurs = true;
        this.peutGererMatchs = true;
        this.peutGenererRapports = true;
        this.peutVoirAlertes = true;
    }

    // Constructeur avec paramètres
    public Administrateur(String nom, String prenom, String email, String motDePasse, String departement) {
        super(nom, prenom, email, motDePasse);
        this.departement = departement;
        this.niveauAcces = 1;
        this.peutGererUtilisateurs = true;
        this.peutGererMatchs = true;
        this.peutGenererRapports = true;
        this.peutVoirAlertes = true;
    }

    // Getters et Setters
    public Integer getNiveauAcces() {
        return niveauAcces;
    }

    public void setNiveauAcces(Integer niveauAcces) {
        this.niveauAcces = niveauAcces;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public Boolean getPeutGererUtilisateurs() {
        return peutGererUtilisateurs;
    }

    public void setPeutGererUtilisateurs(Boolean peutGererUtilisateurs) {
        this.peutGererUtilisateurs = peutGererUtilisateurs;
    }

    public Boolean getPeutGererMatchs() {
        return peutGererMatchs;
    }

    public void setPeutGererMatchs(Boolean peutGererMatchs) {
        this.peutGererMatchs = peutGererMatchs;
    }

    public Boolean getPeutGenererRapports() {
        return peutGenererRapports;
    }

    public void setPeutGenererRapports(Boolean peutGenererRapports) {
        this.peutGenererRapports = peutGenererRapports;
    }

    public Boolean getPeutVoirAlertes() {
        return peutVoirAlertes;
    }

    public void setPeutVoirAlertes(Boolean peutVoirAlertes) {
        this.peutVoirAlertes = peutVoirAlertes;
    }

    public List<Rapport> getRapportsGeneres() {
        return rapportsGeneres;
    }

    public void setRapportsGeneres(List<Rapport> rapportsGeneres) {
        this.rapportsGeneres = rapportsGeneres;
    }

    @Override
    public String getTypeUtilisateur() {
        return "ADMINISTRATEUR";
    }

    /**
     * Vérifie si l'administrateur a tous les droits
     */
    public boolean aAccesComplet() {
        return peutGererUtilisateurs && peutGererMatchs && peutGenererRapports && peutVoirAlertes;
    }

    @Override
    public String toString() {
        return "Administrateur{" +
                "id=" + getId() +
                ", nom='" + getNom() + '\'' +
                ", departement='" + departement + '\'' +
                ", niveauAcces=" + niveauAcces +
                '}';
    }
}
