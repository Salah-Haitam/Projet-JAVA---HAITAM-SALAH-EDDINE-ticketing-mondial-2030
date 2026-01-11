package com.mondial2030.service;

import com.mondial2030.dao.impl.TicketDAOImpl;
import com.mondial2030.dao.impl.MatchDAOImpl;
import com.mondial2030.dao.impl.TransactionDAOImpl;
import com.mondial2030.dao.impl.SpectateurDAOImpl;
import com.mondial2030.dao.interfaces.TicketDAO;
import com.mondial2030.dao.interfaces.MatchDAO;
import com.mondial2030.dao.interfaces.TransactionDAO;
import com.mondial2030.dao.interfaces.SpectateurDAO;
import com.mondial2030.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des tickets.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class TicketService {
    
    private static TicketService instance;
    private final TicketDAO ticketDAO;
    private final MatchDAO matchDAO;
    private final TransactionDAO transactionDAO;
    private final SpectateurDAO spectateurDAO;
    
    private TicketService() {
        this.ticketDAO = new TicketDAOImpl();
        this.matchDAO = new MatchDAOImpl();
        this.transactionDAO = new TransactionDAOImpl();
        this.spectateurDAO = new SpectateurDAOImpl();
    }
    
    public static synchronized TicketService getInstance() {
        if (instance == null) {
            instance = new TicketService();
        }
        return instance;
    }
    
    /**
     * Achète un ticket pour un match
     */
    public Optional<Ticket> acheterTicket(Spectateur spectateur, Match match, 
                                           CategorieTicket categorie, String methodePaiement) {
        try {
            // Recharger le match depuis la base de données pour avoir les données à jour
            Optional<Match> matchFrais = matchDAO.findById(match.getId());
            if (matchFrais.isEmpty()) {
                System.out.println("DEBUG - Match non trouvé: " + match.getId());
                return Optional.empty();
            }
            Match matchActuel = matchFrais.get();
            
            // Vérifier la disponibilité
            if (matchActuel.getTicketsDisponibles() == null || matchActuel.getTicketsDisponibles() <= 0) {
                System.out.println("DEBUG - Plus de tickets disponibles: " + matchActuel.getTicketsDisponibles());
                return Optional.empty();
            }
            
            // Recharger le spectateur aussi
            Optional<Spectateur> specFrais = spectateurDAO.findById(spectateur.getId());
            if (specFrais.isEmpty()) {
                System.out.println("DEBUG - Spectateur non trouvé: " + spectateur.getId());
                return Optional.empty();
            }
            Spectateur spectateurActuel = specFrais.get();
            
            // Calculer le prix
            double prix = calculerPrix(matchActuel, categorie);
            
            // Créer le ticket
            Ticket ticket = new Ticket(matchActuel, spectateurActuel, categorie, prix);
            ticket.setDateAchat(LocalDateTime.now());
            ticket.setStatut(StatutTicket.RESERVE);
            
            // Simuler le paiement
            Transaction transaction = new Transaction(spectateurActuel, ticket, TypeTransaction.ACHAT, prix);
            transaction.setMethodePaiement(methodePaiement);
            
            // Traiter le paiement (simulation)
            boolean paiementReussi = simulerPaiement(transaction);
            
            if (paiementReussi) {
                ticket.valider();
                ticketDAO.save(ticket);
                
                transaction.valider();
                transactionDAO.save(transaction);
                
                // Mettre à jour le match
                matchDAO.decrementerTicketsDisponibles(match.getId());
                
                // Mettre à jour le spectateur
                spectateurDAO.incrementerNombreTickets(spectateur.getId());
                
                return Optional.of(ticket);
            } else {
                transaction.refuser();
                transactionDAO.save(transaction);
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
    /**
     * Transfère un ticket à un autre spectateur
     */
    public boolean transfererTicket(Ticket ticket, Spectateur nouveauProprietaire) {
        try {
            if (!ticket.peutEtreTransfere()) {
                return false;
            }
            
            Spectateur ancienProprietaire = ticket.getProprietaire();
            
            // Créer la transaction de transfert
            Transaction transaction = new Transaction();
            transaction.setTypeTransaction(TypeTransaction.TRANSFERT);
            transaction.setSpectateur(nouveauProprietaire);
            transaction.setTicket(ticket);
            transaction.setMontant(0.0); // Transfert gratuit
            transaction.setSpectateurSource(ancienProprietaire);
            transaction.setSpectateurDestination(nouveauProprietaire);
            
            // Effectuer le transfert
            ticket.transfererVers(nouveauProprietaire);
            ticketDAO.update(ticket);
            
            transaction.valider();
            transactionDAO.save(transaction);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Valide un ticket (scan à l'entrée)
     */
    public boolean validerTicket(String codeQR) {
        try {
            Optional<Ticket> ticketOpt = ticketDAO.findByCodeQR(codeQR);
            
            if (ticketOpt.isEmpty()) {
                return false;
            }
            
            Ticket ticket = ticketOpt.get();
            
            if (ticket.getStatut() != StatutTicket.VALIDE) {
                return false;
            }
            
            // Vérifier que le match n'est pas terminé
            if (ticket.getMatch().getTermine()) {
                return false;
            }
            
            ticket.utiliser();
            ticketDAO.update(ticket);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Annule un ticket avec remboursement
     */
    public boolean annulerTicket(Ticket ticket) {
        try {
            if (ticket.getStatut() == StatutTicket.UTILISE || 
                ticket.getStatut() == StatutTicket.ANNULE) {
                return false;
            }
            
            // Vérifier si le match n'a pas encore eu lieu
            if (!ticket.getMatch().estAVenir()) {
                return false;
            }
            
            // Créer la transaction de remboursement
            Transaction transaction = new Transaction();
            transaction.setTypeTransaction(TypeTransaction.REMBOURSEMENT);
            transaction.setSpectateur(ticket.getProprietaire());
            transaction.setTicket(ticket);
            transaction.setMontant(ticket.getPrix());
            
            ticket.annuler();
            ticketDAO.update(ticket);
            
            transaction.valider();
            transactionDAO.save(transaction);
            
            // Remettre le ticket disponible
            Match match = ticket.getMatch();
            match.incrementerTicketsDisponibles();
            matchDAO.update(match);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Récupère les tickets d'un spectateur
     */
    public List<Ticket> getTicketsSpectateur(Long spectateurId) {
        return ticketDAO.findBySpectateurId(spectateurId);
    }
    
    /**
     * Récupère les tickets d'un spectateur (alias)
     */
    public List<Ticket> getTicketsBySpectateur(Long spectateurId) {
        return ticketDAO.findBySpectateurId(spectateurId);
    }
    
    /**
     * Récupère les tickets d'un match
     */
    public List<Ticket> getTicketsMatch(Long matchId) {
        return ticketDAO.findByMatchId(matchId);
    }
    
    /**
     * Récupère les tickets transférables d'un spectateur
     */
    public List<Ticket> getTicketsTransferables(Long spectateurId) {
        return ticketDAO.findTransferables(spectateurId);
    }
    
    /**
     * Recherche un ticket par son numéro
     */
    public Optional<Ticket> findByNumero(String numeroTicket) {
        return ticketDAO.findByNumeroTicket(numeroTicket);
    }
    
    /**
     * Calcule le prix d'un ticket
     */
    public double calculerPrix(Match match, CategorieTicket categorie) {
        double prixBase = match.getPrixBase() != null ? match.getPrixBase() : 100.0;
        double coefficient = categorie.getCoefficientPrix();
        
        // Appliquer un supplément pour les phases avancées
        double supplementPhase = 1.0;
        if (match.getPhase() != null) {
            switch (match.getPhase()) {
                case HUITIEMES -> supplementPhase = 1.2;
                case QUARTS -> supplementPhase = 1.5;
                case DEMI_FINALES -> supplementPhase = 2.0;
                case PETITE_FINALE -> supplementPhase = 1.8;
                case FINALE -> supplementPhase = 3.0;
                default -> supplementPhase = 1.0;
            }
        }
        
        return prixBase * coefficient * supplementPhase;
    }

    /**
     * Calcule le prix d'un ticket avec prix de base personnalisé
     */
    public double calculerPrix(Double prixBase, CategorieTicket categorie, PhaseMatch phase) {
        double base = prixBase != null ? prixBase : 100.0;
        double coefficient = categorie.getCoefficientPrix();
        
        double supplementPhase = 1.0;
        if (phase != null) {
            switch (phase) {
                case HUITIEMES -> supplementPhase = 1.2;
                case QUARTS -> supplementPhase = 1.5;
                case DEMI_FINALES -> supplementPhase = 2.0;
                case PETITE_FINALE -> supplementPhase = 1.8;
                case FINALE -> supplementPhase = 3.0;
                default -> supplementPhase = 1.0;
            }
        }
        
        return base * coefficient * supplementPhase;
    }

    /**
     * Achète plusieurs tickets pour un match
     */
    public java.util.List<Ticket> acheterTickets(Spectateur spectateur, Match match, Zone zone,
                                                  CategorieTicket categorie, int quantite) {
        java.util.List<Ticket> tickets = new java.util.ArrayList<>();
        for (int i = 0; i < quantite; i++) {
            Optional<Ticket> ticketOpt = acheterTicket(spectateur, match, categorie, "CARTE_BANCAIRE");
            ticketOpt.ifPresent(tickets::add);
        }
        return tickets;
    }
    
    /**
     * Simule un paiement
     */
    private boolean simulerPaiement(Transaction transaction) {
        // Simulation: 95% de réussite
        return Math.random() > 0.05;
    }
    
    /**
     * Compte les tickets vendus pour un match
     */
    public long compterTicketsVendus(Long matchId) {
        return ticketDAO.countByMatchIdAndStatut(matchId, StatutTicket.VALIDE);
    }
    
    /**
     * Calcule les revenus d'un match
     */
    public Double calculerRevenusMatch(Long matchId) {
        return ticketDAO.calculerRevenusMatch(matchId);
    }

    /**
     * Récupère tous les tickets
     */
    public List<Ticket> getAllTickets() {
        return ticketDAO.findAll();
    }

    /**
     * Récupère les tickets par match (si matchId est null, retourne tous les tickets)
     */
    public List<Ticket> getTicketsParMatch(Long matchId) {
        if (matchId == null) {
            return ticketDAO.findAll();
        }
        return ticketDAO.findByMatchId(matchId);
    }

    /**
     * Transfère un ticket par ID et email
     */
    public boolean transfererTicket(Long ticketId, String emailDestination) {
        try {
            Optional<Ticket> ticketOpt = ticketDAO.findById(ticketId);
            Optional<Spectateur> destinataireOpt = spectateurDAO.findByEmail(emailDestination);
            
            if (ticketOpt.isEmpty() || destinataireOpt.isEmpty()) {
                return false;
            }
            
            return transfererTicket(ticketOpt.get(), destinataireOpt.get());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Annule un ticket par ID
     */
    public boolean annulerTicket(Long ticketId) {
        Optional<Ticket> ticketOpt = ticketDAO.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            return false;
        }
        return annulerTicket(ticketOpt.get());
    }
}
