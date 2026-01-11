package com.mondial2030.dao.interfaces;

import com.mondial2030.entity.Transaction;
import com.mondial2030.entity.StatutTransaction;
import com.mondial2030.entity.TypeTransaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour la gestion des transactions.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public interface TransactionDAO extends GenericDAO<Transaction, Long> {
    
    /**
     * Recherche une transaction par son numéro
     * @param numeroTransaction Le numéro de transaction
     * @return La transaction trouvée ou empty
     */
    Optional<Transaction> findByNumeroTransaction(String numeroTransaction);
    
    /**
     * Recherche les transactions d'un spectateur
     * @param spectateurId L'identifiant du spectateur
     * @return Liste des transactions du spectateur
     */
    List<Transaction> findBySpectateurId(Long spectateurId);
    
    /**
     * Recherche les transactions par statut
     * @param statut Le statut recherché
     * @return Liste des transactions avec ce statut
     */
    List<Transaction> findByStatut(StatutTransaction statut);
    
    /**
     * Recherche les transactions par type
     * @param type Le type de transaction
     * @return Liste des transactions de ce type
     */
    List<Transaction> findByType(TypeTransaction type);
    
    /**
     * Recherche les transactions d'un ticket
     * @param ticketId L'identifiant du ticket
     * @return Liste des transactions liées au ticket
     */
    List<Transaction> findByTicketId(Long ticketId);
    
    /**
     * Recherche les transactions dans une plage de dates
     * @param debut Date de début
     * @param fin Date de fin
     * @return Liste des transactions dans cette période
     */
    List<Transaction> findByDateRange(LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Calcule le total des revenus
     * @return Le total des revenus
     */
    Double calculerTotalRevenus();
    
    /**
     * Calcule le total des revenus pour une période
     * @param debut Date de début
     * @param fin Date de fin
     * @return Le total des revenus
     */
    Double calculerRevenusPeriode(LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Met à jour le statut d'une transaction
     * @param transactionId L'identifiant de la transaction
     * @param statut Le nouveau statut
     */
    void updateStatut(Long transactionId, StatutTransaction statut);
    
    /**
     * Valide une transaction
     * @param transactionId L'identifiant de la transaction
     */
    void validerTransaction(Long transactionId);
    
    /**
     * Compte le nombre de transactions par statut
     * @param statut Le statut
     * @return Le nombre de transactions
     */
    long countByStatut(StatutTransaction statut);
    
    /**
     * Recherche les transactions en attente depuis plus de X minutes
     * @param minutes Nombre de minutes
     * @return Liste des transactions en attente
     */
    List<Transaction> findEnAttenteDepuis(int minutes);
}
