package com.mondial2030.dao.impl;

import com.mondial2030.dao.interfaces.FluxSpectateurDAO;
import com.mondial2030.entity.FluxSpectateurs;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation Hibernate du DAO FluxSpectateurs.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class FluxSpectateurDAOImpl extends GenericDAOImpl<FluxSpectateurs, Long> implements FluxSpectateurDAO {
    
    @Override
    public List<FluxSpectateurs> findByMatchId(Long matchId) {
        String hql = "FROM FluxSpectateurs f WHERE f.match.id = :matchId ORDER BY f.horodatage DESC";
        return executeQuery(hql, "matchId", matchId);
    }
    
    @Override
    public List<FluxSpectateurs> findByZoneId(Long zoneId) {
        String hql = "FROM FluxSpectateurs f WHERE f.zone.id = :zoneId ORDER BY f.horodatage DESC";
        return executeQuery(hql, "zoneId", zoneId);
    }
    
    @Override
    public List<FluxSpectateurs> findByMatchAndZone(Long matchId, Long zoneId) {
        String hql = "FROM FluxSpectateurs f WHERE f.match.id = :matchId AND f.zone.id = :zoneId ORDER BY f.horodatage DESC";
        return executeQuery(hql, "matchId", matchId, "zoneId", zoneId);
    }
    
    @Override
    public List<FluxSpectateurs> findByHorodatageRange(LocalDateTime debut, LocalDateTime fin) {
        String hql = "FROM FluxSpectateurs f WHERE f.horodatage BETWEEN :debut AND :fin ORDER BY f.horodatage";
        return executeQuery(hql, "debut", debut, "fin", fin);
    }
    
    @Override
    public List<FluxSpectateurs> findEnSurpopulation(double seuilDensite) {
        String hql = "FROM FluxSpectateurs f WHERE f.densite > :seuil ORDER BY f.densite DESC";
        return executeQuery(hql, "seuil", seuilDensite);
    }
    
    @Override
    public Integer calculerOccupationTotale(Long matchId) {
        try (Session session = getSession()) {
            String hql = "SELECT SUM(f.occupationActuelle) FROM FluxSpectateurs f WHERE f.match.id = :matchId";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("matchId", matchId);
            Long result = query.getSingleResult();
            return result != null ? result.intValue() : 0;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du calcul de l'occupation totale: " + e.getMessage(), e);
        }
    }
    
    @Override
    public FluxSpectateurs findDernierFluxByZone(Long zoneId) {
        try (Session session = getSession()) {
            String hql = "FROM FluxSpectateurs f WHERE f.zone.id = :zoneId ORDER BY f.horodatage DESC";
            Query<FluxSpectateurs> query = session.createQuery(hql, FluxSpectateurs.class);
            query.setParameter("zoneId", zoneId);
            query.setMaxResults(1);
            List<FluxSpectateurs> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération du dernier flux: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Double calculerDensiteMoyenne(Long matchId) {
        String hql = "SELECT AVG(f.densite) FROM FluxSpectateurs f WHERE f.match.id = :matchId";
        Double result = executeDoubleQuery(hql, "matchId", matchId);
        return result != null ? result : 0.0;
    }
    
    @Override
    public void enregistrerEntree(Long matchId, Long zoneId) {
        FluxSpectateurs flux = findDernierFluxByZone(zoneId);
        if (flux != null && flux.getMatch().getId().equals(matchId)) {
            flux.enregistrerEntree();
            flux.setHorodatage(LocalDateTime.now());
            update(flux);
        }
    }
    
    @Override
    public void enregistrerSortie(Long matchId, Long zoneId) {
        FluxSpectateurs flux = findDernierFluxByZone(zoneId);
        if (flux != null && flux.getMatch().getId().equals(matchId)) {
            flux.enregistrerSortie();
            flux.setHorodatage(LocalDateTime.now());
            update(flux);
        }
    }
}
