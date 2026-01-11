package com.mondial2030.dao.impl;

import com.mondial2030.dao.interfaces.TransactionDAO;
import com.mondial2030.entity.Transaction;
import com.mondial2030.entity.StatutTransaction;
import com.mondial2030.entity.TypeTransaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation Hibernate du DAO Transaction.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class TransactionDAOImpl extends GenericDAOImpl<Transaction, Long> implements TransactionDAO {
    
    @Override
    public Optional<Transaction> findByNumeroTransaction(String numeroTransaction) {
        String hql = "FROM Transaction t WHERE t.numeroTransaction = :numero";
        return executeSingleResultQuery(hql, "numero", numeroTransaction);
    }
    
    @Override
    public List<Transaction> findBySpectateurId(Long spectateurId) {
        String hql = "FROM Transaction t WHERE t.spectateur.id = :spectateurId ORDER BY t.dateCreation DESC";
        return executeQuery(hql, "spectateurId", spectateurId);
    }
    
    @Override
    public List<Transaction> findByStatut(StatutTransaction statut) {
        String hql = "FROM Transaction t WHERE t.statut = :statut ORDER BY t.dateCreation DESC";
        return executeQuery(hql, "statut", statut);
    }
    
    @Override
    public List<Transaction> findByType(TypeTransaction type) {
        String hql = "FROM Transaction t WHERE t.typeTransaction = :type ORDER BY t.dateCreation DESC";
        return executeQuery(hql, "type", type);
    }
    
    @Override
    public List<Transaction> findByTicketId(Long ticketId) {
        String hql = "FROM Transaction t WHERE t.ticket.id = :ticketId ORDER BY t.dateCreation DESC";
        return executeQuery(hql, "ticketId", ticketId);
    }
    
    @Override
    public List<Transaction> findByDateRange(LocalDateTime debut, LocalDateTime fin) {
        String hql = "FROM Transaction t WHERE t.dateCreation BETWEEN :debut AND :fin ORDER BY t.dateCreation DESC";
        return executeQuery(hql, "debut", debut, "fin", fin);
    }
    
    @Override
    public Double calculerTotalRevenus() {
        String hql = "SELECT SUM(t.montant) FROM Transaction t WHERE t.statut = :statut AND t.typeTransaction = :type";
        Double result = executeDoubleQuery(hql, "statut", StatutTransaction.VALIDEE, "type", TypeTransaction.ACHAT);
        return result != null ? result : 0.0;
    }
    
    @Override
    public Double calculerRevenusPeriode(LocalDateTime debut, LocalDateTime fin) {
        String hql = "SELECT SUM(t.montant) FROM Transaction t WHERE t.statut = :statut " +
                    "AND t.typeTransaction = :type AND t.dateCreation BETWEEN :debut AND :fin";
        Double result = executeDoubleQuery(hql, "statut", StatutTransaction.VALIDEE, 
                                          "type", TypeTransaction.ACHAT, "debut", debut, "fin", fin);
        return result != null ? result : 0.0;
    }
    
    @Override
    public void updateStatut(Long transactionId, StatutTransaction statut) {
        String hql = "UPDATE Transaction t SET t.statut = :statut WHERE t.id = :id";
        executeUpdate(hql, "statut", statut, "id", transactionId);
    }
    
    @Override
    public void validerTransaction(Long transactionId) {
        String hql = "UPDATE Transaction t SET t.statut = :statut, t.dateValidation = :date WHERE t.id = :id";
        executeUpdate(hql, "statut", StatutTransaction.VALIDEE, "date", LocalDateTime.now(), "id", transactionId);
    }
    
    @Override
    public long countByStatut(StatutTransaction statut) {
        String hql = "SELECT COUNT(t) FROM Transaction t WHERE t.statut = :statut";
        return executeCountQuery(hql, "statut", statut);
    }
    
    @Override
    public List<Transaction> findEnAttenteDepuis(int minutes) {
        LocalDateTime limite = LocalDateTime.now().minusMinutes(minutes);
        String hql = "FROM Transaction t WHERE t.statut = :statut AND t.dateCreation < :limite";
        return executeQuery(hql, "statut", StatutTransaction.EN_ATTENTE, "limite", limite);
    }
}
