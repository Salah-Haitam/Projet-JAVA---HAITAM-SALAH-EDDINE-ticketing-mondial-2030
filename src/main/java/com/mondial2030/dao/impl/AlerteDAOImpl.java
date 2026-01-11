package com.mondial2030.dao.impl;

import com.mondial2030.dao.interfaces.AlerteDAO;
import com.mondial2030.entity.Alerte;
import com.mondial2030.entity.TypeAlerte;
import com.mondial2030.entity.NiveauAlerte;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation Hibernate du DAO Alerte.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class AlerteDAOImpl extends GenericDAOImpl<Alerte, Long> implements AlerteDAO {
    
    @Override
    public List<Alerte> findAll() {
        String hql = "FROM Alerte a LEFT JOIN FETCH a.creeePar ORDER BY a.dateCreation DESC";
        return executeQuery(hql);
    }
    
    @Override
    public List<Alerte> findByType(TypeAlerte type) {
        String hql = "FROM Alerte a WHERE a.typeAlerte = :type ORDER BY a.dateCreation DESC";
        return executeQuery(hql, "type", type);
    }
    
    @Override
    public List<Alerte> findByNiveau(NiveauAlerte niveau) {
        String hql = "FROM Alerte a WHERE a.niveau = :niveau ORDER BY a.dateCreation DESC";
        return executeQuery(hql, "niveau", niveau);
    }
    
    @Override
    public List<Alerte> findNonResolues() {
        String hql = "FROM Alerte a WHERE a.resolue = false ORDER BY a.niveau DESC, a.dateCreation DESC";
        return executeQuery(hql);
    }
    
    @Override
    public List<Alerte> findResolues() {
        String hql = "FROM Alerte a WHERE a.resolue = true ORDER BY a.dateResolution DESC";
        return executeQuery(hql);
    }
    
    @Override
    public List<Alerte> findByMatchId(Long matchId) {
        String hql = "FROM Alerte a WHERE a.match.id = :matchId ORDER BY a.dateCreation DESC";
        return executeQuery(hql, "matchId", matchId);
    }
    
    @Override
    public List<Alerte> findByZoneId(Long zoneId) {
        String hql = "FROM Alerte a WHERE a.zone.id = :zoneId ORDER BY a.dateCreation DESC";
        return executeQuery(hql, "zoneId", zoneId);
    }
    
    @Override
    public List<Alerte> findCritiquesNonResolues() {
        String hql = "FROM Alerte a WHERE a.resolue = false AND (a.niveau = :critique OR a.niveau = :eleve) ORDER BY a.niveau DESC, a.dateCreation DESC";
        return executeQuery(hql, "critique", NiveauAlerte.CRITIQUE, "eleve", NiveauAlerte.ELEVE);
    }
    
    @Override
    public List<Alerte> findByDateRange(LocalDateTime debut, LocalDateTime fin) {
        String hql = "FROM Alerte a WHERE a.dateCreation BETWEEN :debut AND :fin ORDER BY a.dateCreation DESC";
        return executeQuery(hql, "debut", debut, "fin", fin);
    }
    
    @Override
    public void resoudre(Long alerteId, Long adminId, String commentaire) {
        String hql = "UPDATE Alerte a SET a.resolue = true, a.dateResolution = :date, " +
                    "a.resoluePar.id = :adminId, a.commentaireResolution = :commentaire WHERE a.id = :id";
        executeUpdate(hql, "date", LocalDateTime.now(), "adminId", adminId, 
                     "commentaire", commentaire, "id", alerteId);
    }
    
    @Override
    public long countNonResolues() {
        String hql = "SELECT COUNT(a) FROM Alerte a WHERE a.resolue = false";
        return executeCountQuery(hql);
    }
    
    @Override
    public long countByNiveau(NiveauAlerte niveau) {
        String hql = "SELECT COUNT(a) FROM Alerte a WHERE a.niveau = :niveau AND a.resolue = false";
        return executeCountQuery(hql, "niveau", niveau);
    }
    
    @Override
    public List<Alerte> findDernieresAlertes(int limite) {
        try (Session session = getSession()) {
            String hql = "FROM Alerte a ORDER BY a.dateCreation DESC";
            Query<Alerte> query = session.createQuery(hql, Alerte.class);
            query.setMaxResults(limite);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des dernières alertes: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Alerte> findBySpectateurId(Long spectateurId) {
        String hql = "FROM Alerte a LEFT JOIN FETCH a.creeePar WHERE a.creeePar.id = :spectateurId ORDER BY a.dateCreation DESC";
        return executeQuery(hql, "spectateurId", spectateurId);
    }
    
    @Override
    public List<Alerte> findAlertesUtilisateursNonLues() {
        String hql = "FROM Alerte a LEFT JOIN FETCH a.creeePar WHERE a.creeePar IS NOT NULL AND (a.lueParAdmin = false OR a.lueParAdmin IS NULL) ORDER BY a.dateCreation DESC";
        return executeQuery(hql);
    }
    
    @Override
    public List<Alerte> findAlertesUtilisateurs() {
        String hql = "FROM Alerte a LEFT JOIN FETCH a.creeePar WHERE a.creeePar IS NOT NULL ORDER BY a.dateCreation DESC";
        return executeQuery(hql);
    }
    
    @Override
    public void marquerCommeLue(Long alerteId, Long adminId) {
        String hql = "UPDATE Alerte a SET a.lueParAdmin = true, a.dateLecture = :date WHERE a.id = :id";
        executeUpdate(hql, "date", LocalDateTime.now(), "id", alerteId);
    }
    
    @Override
    public void repondre(Long alerteId, Long adminId, String reponse) {
        String hql = "UPDATE Alerte a SET a.reponseAdmin = :reponse, a.dateReponse = :date, " +
                    "a.reponduPar.id = :adminId, a.resolue = true, a.dateResolution = :date, " +
                    "a.resoluePar.id = :adminId, a.lueParAdmin = true WHERE a.id = :id";
        executeUpdate(hql, "reponse", reponse, "date", LocalDateTime.now(), 
                     "adminId", adminId, "id", alerteId);
    }
    
    @Override
    public long countAlertesUtilisateursNonLues() {
        String hql = "SELECT COUNT(a) FROM Alerte a WHERE a.creeePar IS NOT NULL AND (a.lueParAdmin = false OR a.lueParAdmin IS NULL)";
        return executeCountQuery(hql);
    }
}
