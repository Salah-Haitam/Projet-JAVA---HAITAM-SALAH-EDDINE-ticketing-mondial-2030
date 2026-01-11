package com.mondial2030.dao.impl;

import com.mondial2030.dao.interfaces.RapportDAO;
import com.mondial2030.entity.Rapport;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation Hibernate du DAO Rapport.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class RapportDAOImpl extends GenericDAOImpl<Rapport, Long> implements RapportDAO {
    
    @Override
    public Optional<Rapport> findByTitre(String titre) {
        String hql = "FROM Rapport r WHERE r.titre = :titre";
        return executeSingleResultQuery(hql, "titre", titre);
    }
    
    @Override
    public List<Rapport> findByType(String typeRapport) {
        String hql = "FROM Rapport r WHERE r.typeRapport = :type ORDER BY r.dateGeneration DESC";
        return executeQuery(hql, "type", typeRapport);
    }
    
    @Override
    public List<Rapport> findByAdminId(Long adminId) {
        String hql = "FROM Rapport r WHERE r.generePar.id = :adminId ORDER BY r.dateGeneration DESC";
        return executeQuery(hql, "adminId", adminId);
    }
    
    @Override
    public List<Rapport> findByMatchId(Long matchId) {
        String hql = "FROM Rapport r WHERE r.match.id = :matchId ORDER BY r.dateGeneration DESC";
        return executeQuery(hql, "matchId", matchId);
    }
    
    @Override
    public List<Rapport> findByDateRange(LocalDateTime debut, LocalDateTime fin) {
        String hql = "FROM Rapport r WHERE r.dateGeneration BETWEEN :debut AND :fin ORDER BY r.dateGeneration DESC";
        return executeQuery(hql, "debut", debut, "fin", fin);
    }
    
    @Override
    public List<Rapport> findDerniersRapports(int limite) {
        try (Session session = getSession()) {
            String hql = "FROM Rapport r ORDER BY r.dateGeneration DESC";
            Query<Rapport> query = session.createQuery(hql, Rapport.class);
            query.setMaxResults(limite);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des derniers rapports: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Rapport> findByFormat(String format) {
        String hql = "FROM Rapport r WHERE r.format = :format ORDER BY r.dateGeneration DESC";
        return executeQuery(hql, "format", format);
    }
}
