package com.mondial2030.dao.impl;

import com.mondial2030.dao.interfaces.SiegeDAO;
import com.mondial2030.entity.Siege;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation Hibernate du DAO Siege.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class SiegeDAOImpl extends GenericDAOImpl<Siege, Long> implements SiegeDAO {
    
    @Override
    public Optional<Siege> findByNumeroAndZone(String numeroSiege, Long zoneId) {
        String hql = "FROM Siege s WHERE s.numeroSiege = :numero AND s.zone.id = :zoneId";
        return executeSingleResultQuery(hql, "numero", numeroSiege, "zoneId", zoneId);
    }
    
    @Override
    public List<Siege> findByZoneId(Long zoneId) {
        String hql = "FROM Siege s WHERE s.zone.id = :zoneId ORDER BY s.rangee, s.numeroDansRangee";
        return executeQuery(hql, "zoneId", zoneId);
    }
    
    @Override
    public List<Siege> findDisponiblesByZoneId(Long zoneId) {
        String hql = "FROM Siege s WHERE s.zone.id = :zoneId AND s.disponible = true ORDER BY s.rangee, s.numeroDansRangee";
        return executeQuery(hql, "zoneId", zoneId);
    }
    
    @Override
    public List<Siege> findByRangee(Long zoneId, String rangee) {
        String hql = "FROM Siege s WHERE s.zone.id = :zoneId AND s.rangee = :rangee ORDER BY s.numeroDansRangee";
        return executeQuery(hql, "zoneId", zoneId, "rangee", rangee);
    }
    
    @Override
    public List<Siege> findSiegesPMR(Long zoneId) {
        String hql = "FROM Siege s WHERE s.zone.id = :zoneId AND s.accessiblePmr = true";
        return executeQuery(hql, "zoneId", zoneId);
    }
    
    @Override
    public void reserver(Long siegeId) {
        String hql = "UPDATE Siege s SET s.disponible = false WHERE s.id = :id";
        executeUpdate(hql, "id", siegeId);
    }
    
    @Override
    public void liberer(Long siegeId) {
        String hql = "UPDATE Siege s SET s.disponible = true WHERE s.id = :id";
        executeUpdate(hql, "id", siegeId);
    }
    
    @Override
    public long countDisponibles(Long zoneId) {
        String hql = "SELECT COUNT(s) FROM Siege s WHERE s.zone.id = :zoneId AND s.disponible = true";
        return executeCountQuery(hql, "zoneId", zoneId);
    }
    
    @Override
    public boolean isDisponible(Long siegeId) {
        Optional<Siege> siege = findById(siegeId);
        return siege.map(Siege::getDisponible).orElse(false);
    }
}
