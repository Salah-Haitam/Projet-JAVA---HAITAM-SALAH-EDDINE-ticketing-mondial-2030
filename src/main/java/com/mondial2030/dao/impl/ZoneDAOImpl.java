package com.mondial2030.dao.impl;

import com.mondial2030.dao.interfaces.ZoneDAO;
import com.mondial2030.entity.Zone;
import com.mondial2030.entity.TypeZone;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation Hibernate du DAO Zone.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class ZoneDAOImpl extends GenericDAOImpl<Zone, Long> implements ZoneDAO {
    
    @Override
    public Optional<Zone> findByNom(String nom) {
        String hql = "FROM Zone z WHERE z.nom = :nom";
        return executeSingleResultQuery(hql, "nom", nom);
    }
    
    @Override
    public List<Zone> findByType(TypeZone type) {
        String hql = "FROM Zone z WHERE z.typeZone = :type";
        return executeQuery(hql, "type", type);
    }
    
    @Override
    public List<Zone> findByMatchId(Long matchId) {
        String hql = "FROM Zone z WHERE z.match.id = :matchId";
        return executeQuery(hql, "matchId", matchId);
    }
    
    @Override
    public List<Zone> findAccessiblesPMR() {
        String hql = "FROM Zone z WHERE z.accessiblePmr = true";
        return executeQuery(hql);
    }
    
    @Override
    public List<Zone> findWithPlacesDisponibles(Long matchId) {
        String hql = "FROM Zone z WHERE z.match.id = :matchId AND z.placesDisponibles > 0";
        return executeQuery(hql, "matchId", matchId);
    }
    
    @Override
    public void updatePlacesDisponibles(Long zoneId, int places) {
        String hql = "UPDATE Zone z SET z.placesDisponibles = :places WHERE z.id = :id";
        executeUpdate(hql, "places", places, "id", zoneId);
    }
    
    @Override
    public void decrementerPlaces(Long zoneId) {
        String hql = "UPDATE Zone z SET z.placesDisponibles = z.placesDisponibles - 1 WHERE z.id = :id AND z.placesDisponibles > 0";
        executeUpdate(hql, "id", zoneId);
    }
    
    @Override
    public void incrementerPlaces(Long zoneId) {
        String hql = "UPDATE Zone z SET z.placesDisponibles = z.placesDisponibles + 1 WHERE z.id = :id";
        executeUpdate(hql, "id", zoneId);
    }
    
    @Override
    public Double calculerTauxOccupation(Long zoneId) {
        Optional<Zone> zone = findById(zoneId);
        if (zone.isPresent()) {
            Zone z = zone.get();
            if (z.getCapacite() != null && z.getCapacite() > 0) {
                int occupees = z.getCapacite() - (z.getPlacesDisponibles() != null ? z.getPlacesDisponibles() : 0);
                return (double) occupees / z.getCapacite() * 100;
            }
        }
        return 0.0;
    }
    
    @Override
    public List<Zone> findZonesEnSurpopulation(double seuilPourcentage) {
        // Calculer le seuil en fonction du pourcentage
        String hql = "FROM Zone z WHERE ((z.capacite - z.placesDisponibles) * 100.0 / z.capacite) > :seuil";
        return executeQuery(hql, "seuil", seuilPourcentage);
    }
}
