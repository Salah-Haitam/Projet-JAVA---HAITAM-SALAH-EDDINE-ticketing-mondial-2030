package com.mondial2030.dao.impl;

import com.mondial2030.dao.interfaces.OptimisateurFluxDAO;
import com.mondial2030.entity.OptimisateurFlux;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation Hibernate du DAO OptimisateurFlux.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class OptimisateurFluxDAOImpl extends GenericDAOImpl<OptimisateurFlux, Long> implements OptimisateurFluxDAO {
    
    @Override
    public List<OptimisateurFlux> findByMatchId(Long matchId) {
        String hql = "FROM OptimisateurFlux o WHERE o.match.id = :matchId ORDER BY o.dateAnalyse DESC";
        return executeQuery(hql, "matchId", matchId);
    }
    
    @Override
    public Optional<OptimisateurFlux> findDerniereAnalyse(Long matchId) {
        try (Session session = getSession()) {
            String hql = "FROM OptimisateurFlux o WHERE o.match.id = :matchId ORDER BY o.dateAnalyse DESC";
            Query<OptimisateurFlux> query = session.createQuery(hql, OptimisateurFlux.class);
            query.setParameter("matchId", matchId);
            query.setMaxResults(1);
            List<OptimisateurFlux> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de la dernière analyse: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<OptimisateurFlux> findByStatut(String statut) {
        String hql = "FROM OptimisateurFlux o WHERE o.statutAnalyse = :statut ORDER BY o.dateAnalyse DESC";
        return executeQuery(hql, "statut", statut);
    }
    
    @Override
    public List<OptimisateurFlux> findByDateRange(LocalDateTime debut, LocalDateTime fin) {
        String hql = "FROM OptimisateurFlux o WHERE o.dateAnalyse BETWEEN :debut AND :fin ORDER BY o.dateAnalyse DESC";
        return executeQuery(hql, "debut", debut, "fin", fin);
    }
    
    @Override
    public List<OptimisateurFlux> findByScoreInferieur(double scoreMax) {
        String hql = "FROM OptimisateurFlux o WHERE o.scoreEfficacite < :score ORDER BY o.scoreEfficacite ASC";
        return executeQuery(hql, "score", scoreMax);
    }
    
    @Override
    public List<OptimisateurFlux> findDernieresAnalyses(int limite) {
        try (Session session = getSession()) {
            String hql = "FROM OptimisateurFlux o ORDER BY o.dateAnalyse DESC";
            Query<OptimisateurFlux> query = session.createQuery(hql, OptimisateurFlux.class);
            query.setMaxResults(limite);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des dernières analyses: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Double calculerScoreMoyen() {
        String hql = "SELECT AVG(o.scoreEfficacite) FROM OptimisateurFlux o WHERE o.scoreEfficacite IS NOT NULL";
        Double result = executeDoubleQuery(hql);
        return result != null ? result : 0.0;
    }
}
