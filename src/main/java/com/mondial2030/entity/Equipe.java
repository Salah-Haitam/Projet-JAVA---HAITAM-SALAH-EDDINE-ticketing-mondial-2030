package com.mondial2030.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant une équipe participant au Mondial 2030.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
@Entity
@Table(name = "equipe")
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "code_pays", length = 3)
    private String codePays;

    @Column(name = "groupe", length = 1)
    private String groupe;

    @Column(name = "confederation", length = 50)
    private String confederation;

    @Column(name = "classement_fifa")
    private Integer classementFifa;

    @Column(name = "entraineur", length = 100)
    private String entraineur;

    @Column(name = "drapeau_url", length = 255)
    private String drapeauUrl;

    @Column(name = "points")
    private Integer points;

    @Column(name = "matchs_joues")
    private Integer matchsJoues;

    @Column(name = "victoires")
    private Integer victoires;

    @Column(name = "nuls")
    private Integer nuls;

    @Column(name = "defaites")
    private Integer defaites;

    @Column(name = "buts_marques")
    private Integer butsMarques;

    @Column(name = "buts_encaisses")
    private Integer butsEncaisses;

    @OneToMany(mappedBy = "equipeDomicile", fetch = FetchType.LAZY)
    private List<Match> matchsDomicile = new ArrayList<>();

    @OneToMany(mappedBy = "equipeExterieur", fetch = FetchType.LAZY)
    private List<Match> matchsExterieur = new ArrayList<>();

    // Constructeur par défaut
    public Equipe() {
        this.points = 0;
        this.matchsJoues = 0;
        this.victoires = 0;
        this.nuls = 0;
        this.defaites = 0;
        this.butsMarques = 0;
        this.butsEncaisses = 0;
    }

    // Constructeur avec paramètres
    public Equipe(String nom, String codePays, String groupe) {
        this();
        this.nom = nom;
        this.codePays = codePays;
        this.groupe = groupe;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCodePays() {
        return codePays;
    }

    public void setCodePays(String codePays) {
        this.codePays = codePays;
    }

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public String getConfederation() {
        return confederation;
    }

    public void setConfederation(String confederation) {
        this.confederation = confederation;
    }

    public Integer getClassementFifa() {
        return classementFifa;
    }

    public void setClassementFifa(Integer classementFifa) {
        this.classementFifa = classementFifa;
    }

    public String getEntraineur() {
        return entraineur;
    }

    public void setEntraineur(String entraineur) {
        this.entraineur = entraineur;
    }

    public String getDrapeauUrl() {
        return drapeauUrl;
    }

    public void setDrapeauUrl(String drapeauUrl) {
        this.drapeauUrl = drapeauUrl;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getMatchsJoues() {
        return matchsJoues;
    }

    public void setMatchsJoues(Integer matchsJoues) {
        this.matchsJoues = matchsJoues;
    }

    public Integer getVictoires() {
        return victoires;
    }

    public void setVictoires(Integer victoires) {
        this.victoires = victoires;
    }

    public Integer getNuls() {
        return nuls;
    }

    public void setNuls(Integer nuls) {
        this.nuls = nuls;
    }

    public Integer getDefaites() {
        return defaites;
    }

    public void setDefaites(Integer defaites) {
        this.defaites = defaites;
    }

    public Integer getButsMarques() {
        return butsMarques;
    }

    public void setButsMarques(Integer butsMarques) {
        this.butsMarques = butsMarques;
    }

    public Integer getButsEncaisses() {
        return butsEncaisses;
    }

    public void setButsEncaisses(Integer butsEncaisses) {
        this.butsEncaisses = butsEncaisses;
    }

    public List<Match> getMatchsDomicile() {
        return matchsDomicile;
    }

    public void setMatchsDomicile(List<Match> matchsDomicile) {
        this.matchsDomicile = matchsDomicile;
    }

    public List<Match> getMatchsExterieur() {
        return matchsExterieur;
    }

    public void setMatchsExterieur(List<Match> matchsExterieur) {
        this.matchsExterieur = matchsExterieur;
    }

    /**
     * Calcule la différence de buts
     */
    public int getDifferenceButs() {
        return butsMarques - butsEncaisses;
    }

    /**
     * Retourne tous les matchs de l'équipe
     */
    public List<Match> getTousLesMatchs() {
        List<Match> tousMatchs = new ArrayList<>();
        tousMatchs.addAll(matchsDomicile);
        tousMatchs.addAll(matchsExterieur);
        return tousMatchs;
    }

    @Override
    public String toString() {
        return nom + " (" + codePays + ")";
    }
}
