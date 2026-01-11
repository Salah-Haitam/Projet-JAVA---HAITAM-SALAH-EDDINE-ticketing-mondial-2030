package com.mondial2030.dao.interfaces;

import com.mondial2030.entity.Ticket;
import com.mondial2030.entity.StatutTicket;
import com.mondial2030.entity.CategorieTicket;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour la gestion des tickets.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public interface TicketDAO extends GenericDAO<Ticket, Long> {
    
    /**
     * Recherche un ticket par son numéro unique
     * @param numeroTicket Le numéro du ticket
     * @return Le ticket trouvé ou empty
     */
    Optional<Ticket> findByNumeroTicket(String numeroTicket);
    
    /**
     * Recherche un ticket par son code QR
     * @param codeQR Le code QR du ticket
     * @return Le ticket trouvé ou empty
     */
    Optional<Ticket> findByCodeQR(String codeQR);
    
    /**
     * Recherche les tickets d'un spectateur
     * @param spectateurId L'identifiant du spectateur
     * @return Liste des tickets du spectateur
     */
    List<Ticket> findBySpectateurId(Long spectateurId);
    
    /**
     * Recherche les tickets d'un match
     * @param matchId L'identifiant du match
     * @return Liste des tickets du match
     */
    List<Ticket> findByMatchId(Long matchId);
    
    /**
     * Recherche les tickets par statut
     * @param statut Le statut recherché
     * @return Liste des tickets avec ce statut
     */
    List<Ticket> findByStatut(StatutTicket statut);
    
    /**
     * Recherche les tickets par catégorie
     * @param categorie La catégorie recherchée
     * @return Liste des tickets de cette catégorie
     */
    List<Ticket> findByCategorie(CategorieTicket categorie);
    
    /**
     * Recherche les tickets disponibles pour un match
     * @param matchId L'identifiant du match
     * @return Liste des tickets disponibles
     */
    List<Ticket> findDisponiblesByMatchId(Long matchId);
    
    /**
     * Compte le nombre de tickets vendus pour un match
     * @param matchId L'identifiant du match
     * @return Le nombre de tickets vendus
     */
    long countByMatchId(Long matchId);
    
    /**
     * Compte le nombre de tickets par statut pour un match
     * @param matchId L'identifiant du match
     * @param statut Le statut
     * @return Le nombre de tickets
     */
    long countByMatchIdAndStatut(Long matchId, StatutTicket statut);
    
    /**
     * Met à jour le statut d'un ticket
     * @param ticketId L'identifiant du ticket
     * @param statut Le nouveau statut
     */
    void updateStatut(Long ticketId, StatutTicket statut);
    
    /**
     * Valide un ticket
     * @param ticketId L'identifiant du ticket
     */
    void validerTicket(Long ticketId);
    
    /**
     * Recherche les tickets transférables
     * @param spectateurId L'identifiant du spectateur
     * @return Liste des tickets transférables
     */
    List<Ticket> findTransferables(Long spectateurId);
    
    /**
     * Calcule le total des revenus pour un match
     * @param matchId L'identifiant du match
     * @return Le total des revenus
     */
    Double calculerRevenusMatch(Long matchId);
}
