package com.mondial2030.dao.interfaces;

import com.mondial2030.entity.Rapport;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour la gestion des rapports.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public interface RapportDAO extends GenericDAO<Rapport, Long> {
    
    /**
     * Recherche un rapport par son titre
     * @param titre Le titre du rapport
     * @return Le rapport trouvé ou empty
     */
    Optional<Rapport> findByTitre(String titre);
    
    /**
     * Recherche les rapports par type
     * @param typeRapport Le type de rapport
     * @return Liste des rapports de ce type
     */
    List<Rapport> findByType(String typeRapport);
    
    /**
     * Recherche les rapports générés par un administrateur
     * @param adminId L'identifiant de l'administrateur
     * @return Liste des rapports générés
     */
    List<Rapport> findByAdminId(Long adminId);
    
    /**
     * Recherche les rapports d'un match
     * @param matchId L'identifiant du match
     * @return Liste des rapports du match
     */
    List<Rapport> findByMatchId(Long matchId);
    
    /**
     * Recherche les rapports dans une plage de dates
     * @param debut Date de début
     * @param fin Date de fin
     * @return Liste des rapports dans cette période
     */
    List<Rapport> findByDateRange(LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Recherche les derniers rapports générés
     * @param limite Nombre maximum de rapports
     * @return Liste des derniers rapports
     */
    List<Rapport> findDerniersRapports(int limite);
    
    /**
     * Recherche les rapports par format
     * @param format Le format (PDF, TXT, etc.)
     * @return Liste des rapports dans ce format
     */
    List<Rapport> findByFormat(String format);
}
