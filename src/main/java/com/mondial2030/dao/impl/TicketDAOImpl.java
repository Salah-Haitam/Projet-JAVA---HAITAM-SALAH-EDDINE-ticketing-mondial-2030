package com.mondial2030.dao.impl;

import com.mondial2030.dao.interfaces.TicketDAO;
import com.mondial2030.entity.Ticket;
import com.mondial2030.entity.StatutTicket;
import com.mondial2030.entity.CategorieTicket;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation Hibernate du DAO Ticket.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class TicketDAOImpl extends GenericDAOImpl<Ticket, Long> implements TicketDAO {
    
    @Override
    public Optional<Ticket> findByNumeroTicket(String numeroTicket) {
        String hql = "FROM Ticket t WHERE t.numeroTicket = :numero";
        return executeSingleResultQuery(hql, "numero", numeroTicket);
    }
    
    @Override
    public Optional<Ticket> findByCodeQR(String codeQR) {
        String hql = "FROM Ticket t WHERE t.codeQR = :codeQR";
        return executeSingleResultQuery(hql, "codeQR", codeQR);
    }
    
    @Override
    public List<Ticket> findBySpectateurId(Long spectateurId) {
        String hql = "FROM Ticket t WHERE t.proprietaire.id = :spectateurId ORDER BY t.dateAchat DESC";
        return executeQuery(hql, "spectateurId", spectateurId);
    }
    
    @Override
    public List<Ticket> findByMatchId(Long matchId) {
        String hql = "FROM Ticket t WHERE t.match.id = :matchId";
        return executeQuery(hql, "matchId", matchId);
    }
    
    @Override
    public List<Ticket> findByStatut(StatutTicket statut) {
        String hql = "FROM Ticket t WHERE t.statut = :statut";
        return executeQuery(hql, "statut", statut);
    }
    
    @Override
    public List<Ticket> findByCategorie(CategorieTicket categorie) {
        String hql = "FROM Ticket t WHERE t.categorie = :categorie";
        return executeQuery(hql, "categorie", categorie);
    }
    
    @Override
    public List<Ticket> findDisponiblesByMatchId(Long matchId) {
        String hql = "FROM Ticket t WHERE t.match.id = :matchId AND t.proprietaire IS NULL AND t.statut = :statut";
        return executeQuery(hql, "matchId", matchId, "statut", StatutTicket.RESERVE);
    }
    
    @Override
    public long countByMatchId(Long matchId) {
        String hql = "SELECT COUNT(t) FROM Ticket t WHERE t.match.id = :matchId";
        return executeCountQuery(hql, "matchId", matchId);
    }
    
    @Override
    public long countByMatchIdAndStatut(Long matchId, StatutTicket statut) {
        String hql = "SELECT COUNT(t) FROM Ticket t WHERE t.match.id = :matchId AND t.statut = :statut";
        return executeCountQuery(hql, "matchId", matchId, "statut", statut);
    }
    
    @Override
    public void updateStatut(Long ticketId, StatutTicket statut) {
        String hql = "UPDATE Ticket t SET t.statut = :statut WHERE t.id = :id";
        executeUpdate(hql, "statut", statut, "id", ticketId);
    }
    
    @Override
    public void validerTicket(Long ticketId) {
        String hql = "UPDATE Ticket t SET t.statut = :statut, t.dateValidation = :date WHERE t.id = :id";
        executeUpdate(hql, "statut", StatutTicket.VALIDE, "date", LocalDateTime.now(), "id", ticketId);
    }
    
    @Override
    public List<Ticket> findTransferables(Long spectateurId) {
        String hql = "FROM Ticket t WHERE t.proprietaire.id = :spectateurId " +
                    "AND t.transferable = true AND t.statut = :statut " +
                    "AND t.nombreTransferts < 3 AND t.match.dateHeure > :now";
        return executeQuery(hql, "spectateurId", spectateurId, "statut", StatutTicket.VALIDE, "now", LocalDateTime.now());
    }
    
    @Override
    public Double calculerRevenusMatch(Long matchId) {
        String hql = "SELECT SUM(t.prix) FROM Ticket t WHERE t.match.id = :matchId AND t.statut = :statut";
        Double result = executeDoubleQuery(hql, "matchId", matchId, "statut", StatutTicket.VALIDE);
        return result != null ? result : 0.0;
    }
}
