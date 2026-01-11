package com.mondial2030.dao.impl;

import com.mondial2030.dao.interfaces.MatchDAO;
import com.mondial2030.entity.Match;
import com.mondial2030.entity.PhaseMatch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation Hibernate du DAO Match.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class MatchDAOImpl extends GenericDAOImpl<Match, Long> implements MatchDAO {
    
    @Override
    public Optional<Match> findByNumeroMatch(String numeroMatch) {
        String hql = "FROM Match m WHERE m.numeroMatch = :numero";
        return executeSingleResultQuery(hql, "numero", numeroMatch);
    }
    
    @Override
    public List<Match> findByPhase(PhaseMatch phase) {
        String hql = "FROM Match m WHERE m.phase = :phase ORDER BY m.dateHeure";
        return executeQuery(hql, "phase", phase);
    }
    
    @Override
    public List<Match> findByGroupe(String groupe) {
        String hql = "FROM Match m WHERE m.groupe = :groupe ORDER BY m.dateHeure";
        return executeQuery(hql, "groupe", groupe);
    }
    
    @Override
    public List<Match> findByEquipeId(Long equipeId) {
        String hql = "FROM Match m WHERE m.equipeDomicile.id = :equipeId OR m.equipeExterieur.id = :equipeId ORDER BY m.dateHeure";
        return executeQuery(hql, "equipeId", equipeId);
    }
    
    @Override
    public List<Match> findMatchsAVenir() {
        String hql = "FROM Match m WHERE m.dateHeure > :now AND m.termine = false ORDER BY m.dateHeure";
        return executeQuery(hql, "now", LocalDateTime.now());
    }
    
    @Override
    public List<Match> findMatchsTermines() {
        String hql = "FROM Match m WHERE m.termine = true ORDER BY m.dateHeure DESC";
        return executeQuery(hql);
    }
    
    @Override
    public List<Match> findByStade(String stade) {
        String hql = "FROM Match m WHERE LOWER(m.stade) LIKE LOWER(:stade) ORDER BY m.dateHeure";
        return executeQuery(hql, "stade", "%" + stade + "%");
    }
    
    @Override
    public List<Match> findByVille(String ville) {
        String hql = "FROM Match m WHERE LOWER(m.ville) LIKE LOWER(:ville) ORDER BY m.dateHeure";
        return executeQuery(hql, "ville", "%" + ville + "%");
    }
    
    @Override
    public List<Match> findByDateRange(LocalDateTime debut, LocalDateTime fin) {
        String hql = "FROM Match m WHERE m.dateHeure BETWEEN :debut AND :fin ORDER BY m.dateHeure";
        return executeQuery(hql, "debut", debut, "fin", fin);
    }
    
    @Override
    public List<Match> findWithTicketsDisponibles() {
        String hql = "FROM Match m WHERE m.ticketsDisponibles > 0 AND m.dateHeure > :now ORDER BY m.dateHeure";
        return executeQuery(hql, "now", LocalDateTime.now());
    }
    
    @Override
    public void updateScore(Long matchId, int scoreDomicile, int scoreExterieur) {
        String hql = "UPDATE Match m SET m.scoreDomicile = :scoreDom, m.scoreExterieur = :scoreExt WHERE m.id = :id";
        executeUpdate(hql, "scoreDom", scoreDomicile, "scoreExt", scoreExterieur, "id", matchId);
    }
    
    @Override
    public void terminerMatch(Long matchId) {
        String hql = "UPDATE Match m SET m.termine = true WHERE m.id = :id";
        executeUpdate(hql, "id", matchId);
    }
    
    @Override
    public void decrementerTicketsDisponibles(Long matchId) {
        String hql = "UPDATE Match m SET m.ticketsDisponibles = m.ticketsDisponibles - 1 WHERE m.id = :id AND m.ticketsDisponibles > 0";
        executeUpdate(hql, "id", matchId);
    }
    
    @Override
    public List<Match> findMatchsDuJour() {
        LocalDateTime debutJour = LocalDate.now().atStartOfDay();
        LocalDateTime finJour = LocalDate.now().atTime(LocalTime.MAX);
        return findByDateRange(debutJour, finJour);
    }
}
