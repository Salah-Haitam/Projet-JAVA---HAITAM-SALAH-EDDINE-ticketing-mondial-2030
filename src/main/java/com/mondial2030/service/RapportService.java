package com.mondial2030.service;

import com.mondial2030.dao.impl.*;
import com.mondial2030.dao.interfaces.*;
import com.mondial2030.entity.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service de génération de rapports.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class RapportService {
    
    private static RapportService instance;
    private final RapportDAO rapportDAO;
    private final TicketDAO ticketDAO;
    private final MatchDAO matchDAO;
    private final TransactionDAO transactionDAO;
    private final AlerteDAO alerteDAO;
    
    private RapportService() {
        this.rapportDAO = new RapportDAOImpl();
        this.ticketDAO = new TicketDAOImpl();
        this.matchDAO = new MatchDAOImpl();
        this.transactionDAO = new TransactionDAOImpl();
        this.alerteDAO = new AlerteDAOImpl();
    }
    
    public static synchronized RapportService getInstance() {
        if (instance == null) {
            instance = new RapportService();
        }
        return instance;
    }
    
    /**
     * Génère un rapport de ventes global
     */
    public Rapport genererRapportVentes(Administrateur admin, LocalDateTime debut, LocalDateTime fin) {
        Rapport rapport = new Rapport("Rapport de Ventes", "VENTES", admin);
        rapport.setDateDebutPeriode(debut);
        rapport.setDateFinPeriode(fin);
        
        // Calculer les statistiques
        Double revenus = transactionDAO.calculerRevenusPeriode(debut, fin);
        long ticketsVendus = ticketDAO.countByMatchIdAndStatut(null, StatutTicket.VALIDE);
        
        rapport.setTotalRevenus(revenus);
        rapport.setTotalTicketsVendus((int) ticketsVendus);
        
        StringBuilder contenu = new StringBuilder();
        contenu.append("=== RAPPORT DE VENTES ===\n\n");
        contenu.append("Période: ").append(debut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        contenu.append(" - ").append(fin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n\n");
        contenu.append("Total des revenus: ").append(String.format("%.2f EUR", revenus)).append("\n");
        contenu.append("Nombre de tickets vendus: ").append(ticketsVendus).append("\n\n");
        
        // Détail par match
        List<Match> matchs = matchDAO.findByDateRange(debut, fin);
        contenu.append("=== DÉTAIL PAR MATCH ===\n\n");
        for (Match match : matchs) {
            Double revenusMatch = ticketDAO.calculerRevenusMatch(match.getId());
            long ticketsMatch = ticketDAO.countByMatchIdAndStatut(match.getId(), StatutTicket.VALIDE);
            contenu.append(match.getLibelleMatch()).append("\n");
            contenu.append("  - Revenus: ").append(String.format("%.2f EUR", revenusMatch)).append("\n");
            contenu.append("  - Tickets vendus: ").append(ticketsMatch).append("\n");
            contenu.append("  - Taux de remplissage: ").append(String.format("%.1f%%", match.getTauxRemplissage())).append("\n\n");
        }
        
        rapport.setContenu(contenu.toString());
        rapportDAO.save(rapport);
        
        return rapport;
    }
    
    /**
     * Génère un rapport pour un match spécifique
     */
    public Rapport genererRapportMatch(Administrateur admin, Match match) {
        Rapport rapport = new Rapport("Rapport Match - " + match.getLibelleMatch(), "MATCH", admin);
        rapport.setMatch(match);
        
        Double revenus = ticketDAO.calculerRevenusMatch(match.getId());
        long ticketsVendus = ticketDAO.countByMatchIdAndStatut(match.getId(), StatutTicket.VALIDE);
        long ticketsUtilises = ticketDAO.countByMatchIdAndStatut(match.getId(), StatutTicket.UTILISE);
        
        rapport.setTotalRevenus(revenus);
        rapport.setTotalTicketsVendus((int) ticketsVendus);
        rapport.setTauxRemplissage(match.getTauxRemplissage());
        
        List<Alerte> alertes = alerteDAO.findByMatchId(match.getId());
        rapport.setNombreAlertes(alertes.size());
        
        StringBuilder contenu = new StringBuilder();
        contenu.append("=== RAPPORT DE MATCH ===\n\n");
        contenu.append("Match: ").append(match.getLibelleMatch()).append("\n");
        contenu.append("Date: ").append(match.getDateHeure().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        contenu.append("Stade: ").append(match.getStade()).append(", ").append(match.getVille()).append("\n");
        contenu.append("Phase: ").append(match.getPhase()).append("\n\n");
        
        if (match.getTermine()) {
            contenu.append("Score: ").append(match.getScoreFormate()).append("\n\n");
        }
        
        contenu.append("=== STATISTIQUES ===\n\n");
        contenu.append("Capacité: ").append(match.getCapaciteStade()).append("\n");
        contenu.append("Tickets vendus: ").append(ticketsVendus).append("\n");
        contenu.append("Tickets utilisés: ").append(ticketsUtilises).append("\n");
        contenu.append("Taux de remplissage: ").append(String.format("%.1f%%", match.getTauxRemplissage())).append("\n");
        contenu.append("Revenus totaux: ").append(String.format("%.2f EUR", revenus)).append("\n\n");
        
        contenu.append("=== ALERTES ===\n\n");
        contenu.append("Nombre d'alertes: ").append(alertes.size()).append("\n");
        for (Alerte alerte : alertes) {
            contenu.append("  - [").append(alerte.getNiveau()).append("] ").append(alerte.getTitre());
            contenu.append(" (").append(alerte.getResolue() ? "Résolue" : "Active").append(")\n");
        }
        
        rapport.setContenu(contenu.toString());
        rapportDAO.save(rapport);
        
        return rapport;
    }
    
    /**
     * Génère un rapport d'alertes
     */
    public Rapport genererRapportAlertes(Administrateur admin) {
        Rapport rapport = new Rapport("Rapport des Alertes", "ALERTES", admin);
        
        List<Alerte> alertesActives = alerteDAO.findNonResolues();
        List<Alerte> alertesCritiques = alerteDAO.findCritiquesNonResolues();
        
        rapport.setNombreAlertes(alertesActives.size());
        
        StringBuilder contenu = new StringBuilder();
        contenu.append("=== RAPPORT DES ALERTES ===\n\n");
        contenu.append("Date de génération: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n\n");
        
        contenu.append("=== RÉSUMÉ ===\n\n");
        contenu.append("Alertes actives: ").append(alertesActives.size()).append("\n");
        contenu.append("Alertes critiques: ").append(alertesCritiques.size()).append("\n\n");
        
        contenu.append("=== ALERTES CRITIQUES ===\n\n");
        for (Alerte alerte : alertesCritiques) {
            contenu.append("🚨 [").append(alerte.getNiveau()).append("] ").append(alerte.getTitre()).append("\n");
            contenu.append("   Type: ").append(alerte.getTypeAlerte()).append("\n");
            contenu.append("   Message: ").append(alerte.getMessage()).append("\n");
            contenu.append("   Date: ").append(alerte.getDateCreation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n\n");
        }
        
        contenu.append("=== TOUTES LES ALERTES ACTIVES ===\n\n");
        for (Alerte alerte : alertesActives) {
            contenu.append("[").append(alerte.getNiveau()).append("] ").append(alerte.getTitre()).append("\n");
            contenu.append("   Type: ").append(alerte.getTypeAlerte()).append("\n");
            contenu.append("   Date: ").append(alerte.getDateCreation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n\n");
        }
        
        rapport.setContenu(contenu.toString());
        rapportDAO.save(rapport);
        
        return rapport;
    }
    
    /**
     * Exporte un rapport en fichier texte
     */
    public boolean exporterRapport(Rapport rapport, String cheminFichier) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(cheminFichier))) {
            writer.println(rapport.getContenu());
            rapport.setCheminFichier(cheminFichier);
            rapport.setFormat("TXT");
            rapportDAO.update(rapport);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Récupère les derniers rapports
     */
    public List<Rapport> getDerniersRapports(int limite) {
        return rapportDAO.findDerniersRapports(limite);
    }
    
    /**
     * Récupère les rapports d'un administrateur
     */
    public List<Rapport> getRapportsAdmin(Long adminId) {
        return rapportDAO.findByAdminId(adminId);
    }
    
    /**
     * Récupère tous les rapports
     */
    public List<Rapport> getAllRapports() {
        return rapportDAO.findAll();
    }

    /**
     * Exporte un rapport en PDF
     * @param rapport Le rapport à exporter
     * @return Le chemin du fichier exporté ou null en cas d'erreur
     */
    public String exporterPDF(Rapport rapport) {
        try {
            String cheminFichier = "rapports/rapport_" + rapport.getId() + "_" + 
                System.currentTimeMillis() + ".txt"; // Simplifié en TXT pour l'instant
            if (exporterRapport(rapport, cheminFichier)) {
                return cheminFichier;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
