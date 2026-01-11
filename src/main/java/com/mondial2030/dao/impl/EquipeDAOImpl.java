package com.mondial2030.dao.impl;

import com.mondial2030.dao.interfaces.EquipeDAO;
import com.mondial2030.entity.Equipe;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation Hibernate du DAO Equipe.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class EquipeDAOImpl extends GenericDAOImpl<Equipe, Long> implements EquipeDAO {
    
    @Override
    public Optional<Equipe> findByNom(String nom) {
        String hql = "FROM Equipe e WHERE LOWER(e.nom) = LOWER(:nom)";
        return executeSingleResultQuery(hql, "nom", nom);
    }
    
    @Override
    public Optional<Equipe> findByCodePays(String codePays) {
        String hql = "FROM Equipe e WHERE UPPER(e.codePays) = UPPER(:code)";
        return executeSingleResultQuery(hql, "code", codePays);
    }
    
    @Override
    public List<Equipe> findByGroupe(String groupe) {
        String hql = "FROM Equipe e WHERE e.groupe = :groupe ORDER BY e.points DESC, e.butsMarques DESC";
        return executeQuery(hql, "groupe", groupe);
    }
    
    @Override
    public List<Equipe> findByConfederation(String confederation) {
        String hql = "FROM Equipe e WHERE e.confederation = :confederation ORDER BY e.classementFifa";
        return executeQuery(hql, "confederation", confederation);
    }
    
    @Override
    public List<Equipe> getClassementGroupe(String groupe) {
        // Tri par points, puis différence de buts, puis buts marqués
        String hql = "FROM Equipe e WHERE e.groupe = :groupe " +
                    "ORDER BY e.points DESC, (e.butsMarques - e.butsEncaisses) DESC, e.butsMarques DESC";
        return executeQuery(hql, "groupe", groupe);
    }
    
    @Override
    public void updateStatistiques(Long equipeId, Boolean victoire, int butsMarques, int butsEncaisses) {
        int points = 0;
        int v = 0, n = 0, d = 0;
        
        if (victoire == null) {
            // Match nul
            points = 1;
            n = 1;
        } else if (victoire) {
            // Victoire
            points = 3;
            v = 1;
        } else {
            // Défaite
            d = 1;
        }
        
        String hql = "UPDATE Equipe e SET " +
                    "e.matchsJoues = e.matchsJoues + 1, " +
                    "e.points = e.points + :points, " +
                    "e.victoires = e.victoires + :v, " +
                    "e.nuls = e.nuls + :n, " +
                    "e.defaites = e.defaites + :d, " +
                    "e.butsMarques = e.butsMarques + :bm, " +
                    "e.butsEncaisses = e.butsEncaisses + :be " +
                    "WHERE e.id = :id";
        
        executeUpdate(hql, "points", points, "v", v, "n", n, "d", d, 
                     "bm", butsMarques, "be", butsEncaisses, "id", equipeId);
    }
    
    @Override
    public List<Equipe> getEquipesQualifiees(String groupe, int nombreQualifies) {
        List<Equipe> classement = getClassementGroupe(groupe);
        return classement.subList(0, Math.min(nombreQualifies, classement.size()));
    }
    
    @Override
    public List<Equipe> findByClassementFifaMax(int classementMax) {
        String hql = "FROM Equipe e WHERE e.classementFifa <= :classement ORDER BY e.classementFifa";
        return executeQuery(hql, "classement", classementMax);
    }
}
