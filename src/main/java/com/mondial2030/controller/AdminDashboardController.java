package com.mondial2030.controller;

import com.mondial2030.entity.*;
import com.mondial2030.service.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Contrôleur du tableau de bord administrateur.
 * Gère toutes les fonctionnalités d'administration.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class AdminDashboardController extends BaseController implements Initializable {
    
    // Services
    private final MatchService matchService = MatchService.getInstance();
    private final TicketService ticketService = TicketService.getInstance();
    private final AlerteService alerteService = AlerteService.getInstance();
    private final RapportService rapportService = RapportService.getInstance();
    private final FluxService fluxService = FluxService.getInstance();
    private final AuthenticationService authService = AuthenticationService.getInstance();
    
    // ===== COMPOSANTS HEADER =====
    @FXML private Label lblUserName;
    @FXML private Label lblDate;
    
    // ===== COMPOSANTS NAVIGATION =====
    @FXML private Button btnDashboard;
    @FXML private Button btnMatchs;
    @FXML private Button btnUtilisateurs;
    @FXML private Button btnTickets;
    @FXML private Button btnAlertes;
    @FXML private Button btnRapports;
    @FXML private Button btnFlux;
    
    // ===== PANNEAUX DE CONTENU =====
    @FXML private StackPane contentPane;
    @FXML private VBox dashboardPane;
    @FXML private VBox matchsPane;
    @FXML private VBox utilisateursPane;
    @FXML private VBox ticketsPane;
    @FXML private VBox alertesPane;
    @FXML private VBox rapportsPane;
    @FXML private VBox fluxPane;
    
    // ===== DASHBOARD - STATISTIQUES =====
    @FXML private Label lblTotalMatchs;
    @FXML private Label lblTotalTickets;
    @FXML private Label lblTotalSpectateurs;
    @FXML private Label lblTotalAlertes;
    @FXML private Label lblChiffreAffaires;
    @FXML private PieChart chartTicketsByCategory;
    @FXML private BarChart<String, Number> chartTicketsByMatch;
    @FXML private LineChart<String, Number> chartVentesTendance;
    
    // ===== GESTION DES MATCHS =====
    @FXML private TableView<Match> tableMatchs;
    @FXML private TableColumn<Match, Long> colMatchId;
    @FXML private TableColumn<Match, String> colMatchEquipes;
    @FXML private TableColumn<Match, String> colMatchDate;
    @FXML private TableColumn<Match, String> colMatchStade;
    @FXML private TableColumn<Match, String> colMatchPhase;
    @FXML private TableColumn<Match, String> colMatchStatut;
    @FXML private TextField txtSearchMatch;
    @FXML private ComboBox<PhaseMatch> cmbFilterPhase;
    
    // ===== GESTION DES UTILISATEURS =====
    @FXML private TableView<Spectateur> tableUtilisateurs;
    @FXML private TableColumn<Spectateur, Long> colUserId;
    @FXML private TableColumn<Spectateur, String> colUserNom;
    @FXML private TableColumn<Spectateur, String> colUserEmail;
    @FXML private TableColumn<Spectateur, String> colUserTelephone;
    @FXML private TableColumn<Spectateur, Integer> colUserNbTickets;
    @FXML private TableColumn<Spectateur, Boolean> colUserActif;
    @FXML private TextField txtSearchUser;
    
    // ===== GESTION DES TICKETS =====
    @FXML private TableView<Ticket> tableTickets;
    @FXML private TableColumn<Ticket, Long> colTicketId;
    @FXML private TableColumn<Ticket, String> colTicketCode;
    @FXML private TableColumn<Ticket, String> colTicketMatch;
    @FXML private TableColumn<Ticket, String> colTicketSpectateur;
    @FXML private TableColumn<Ticket, String> colTicketCategorie;
    @FXML private TableColumn<Ticket, String> colTicketStatut;
    @FXML private TableColumn<Ticket, String> colTicketPrix;
    @FXML private ComboBox<StatutTicket> cmbFilterStatut;
    
    // ===== GESTION DES ALERTES =====
    @FXML private TableView<Alerte> tableAlertes;
    @FXML private TableColumn<Alerte, Long> colAlerteId;
    @FXML private TableColumn<Alerte, String> colAlerteTitre;
    @FXML private TableColumn<Alerte, String> colAlerteType;
    @FXML private TableColumn<Alerte, String> colAlerteNiveau;
    @FXML private TableColumn<Alerte, String> colAlerteDate;
    @FXML private TableColumn<Alerte, String> colAlerteSource;
    @FXML private TableColumn<Alerte, String> colAlerteResolue;
    @FXML private Label lblAlertesNonResolues;
    @FXML private Label lblAlertesUtilisateurs;
    @FXML private Button btnTabToutesAlertes;
    @FXML private Button btnTabAlertesUtilisateurs;
    @FXML private VBox detailAlerteUtilisateurPane;
    @FXML private Label lblMessageUtilisateur;
    @FXML private Label lblInfosUtilisateur;
    
    // Mode d'affichage des alertes
    private boolean afficherAlertesUtilisateurs = false;
    
    // ===== RAPPORTS =====
    @FXML private DatePicker dpDateDebut;
    @FXML private DatePicker dpDateFin;
    @FXML private ComboBox<String> cmbTypeRapport;
    @FXML private TableView<Rapport> tableRapports;
    @FXML private TableColumn<Rapport, Long> colRapportId;
    @FXML private TableColumn<Rapport, String> colRapportTitre;
    @FXML private TableColumn<Rapport, String> colRapportType;
    @FXML private TableColumn<Rapport, String> colRapportDate;
    
    // ===== FLUX SPECTATEURS =====
    @FXML private TableView<FluxSpectateurs> tableFlux;
    @FXML private TableColumn<FluxSpectateurs, String> colFluxMatch;
    @FXML private TableColumn<FluxSpectateurs, String> colFluxZone;
    @FXML private TableColumn<FluxSpectateurs, Integer> colFluxEntrees;
    @FXML private TableColumn<FluxSpectateurs, Integer> colFluxSorties;
    @FXML private TableColumn<FluxSpectateurs, String> colFluxDensite;
    @FXML private Label lblOccupationGlobale;
    @FXML private ProgressBar progressOccupation;
    
    // Listes observables
    private ObservableList<Match> matchsList = FXCollections.observableArrayList();
    private ObservableList<Spectateur> utilisateursList = FXCollections.observableArrayList();
    private ObservableList<Ticket> ticketsList = FXCollections.observableArrayList();
    private ObservableList<Alerte> alertesList = FXCollections.observableArrayList();
    private ObservableList<Rapport> rapportsList = FXCollections.observableArrayList();
    private ObservableList<FluxSpectateurs> fluxList = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Initialisation du dashboard administrateur");
        
        // Initialiser la date
        if (lblDate != null) {
            lblDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH)));
        }
        
        // Initialiser les tableaux
        initializeMatchsTable();
        initializeUtilisateursTable();
        initializeTicketsTable();
        initializeAlertesTable();
        initializeRapportsTable();
        initializeFluxTable();
        
        // Initialiser les filtres
        initializeFilters();
        
        // Afficher le dashboard par défaut
        showPane(dashboardPane);
    }
    
    @Override
    protected void onUserSet() {
        if (currentUser != null && lblUserName != null) {
            lblUserName.setText(currentUser.getNom() + " " + currentUser.getPrenom());
        }
        // Charger les données
        refreshAllData();
    }
    
    // ===== INITIALISATION DES TABLEAUX =====
    
    private void initializeMatchsTable() {
        if (tableMatchs == null) return;
        
        colMatchId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMatchEquipes.setCellValueFactory(cellData -> {
            Match m = cellData.getValue();
            String equipes = m.getEquipeDomicile().getNom() + " vs " + m.getEquipeExterieur().getNom();
            return new SimpleStringProperty(equipes);
        });
        colMatchDate.setCellValueFactory(cellData -> 
            new SimpleStringProperty(formatDate(cellData.getValue().getDateHeure())));
        colMatchStade.setCellValueFactory(new PropertyValueFactory<>("stade"));
        colMatchPhase.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getPhase().name()));
        colMatchStatut.setCellValueFactory(cellData -> {
            Match m = cellData.getValue();
            String statut = m.isTermine() ? "Terminé" : (m.getDateHeure().isBefore(LocalDateTime.now()) ? "En cours" : "À venir");
            return new SimpleStringProperty(statut);
        });
        
        tableMatchs.setItems(matchsList);
    }
    
    private void initializeUtilisateursTable() {
        if (tableUtilisateurs == null) return;
        
        colUserId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUserNom.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getNom() + " " + cellData.getValue().getPrenom()));
        colUserEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colUserTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colUserNbTickets.setCellValueFactory(cellData -> {
            // Utiliser le nombre de tickets stocké pour éviter LazyInitializationException
            Integer nbTickets = cellData.getValue().getNombreTicketsAchetes();
            return new javafx.beans.property.SimpleIntegerProperty(nbTickets != null ? nbTickets : 0).asObject();
        });
        colUserActif.setCellValueFactory(new PropertyValueFactory<>("actif"));
        
        // Double-clic pour voir les tickets de l'utilisateur
        tableUtilisateurs.setRowFactory(tv -> {
            TableRow<Spectateur> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showTicketsUtilisateur(row.getItem());
                }
            });
            return row;
        });
        
        tableUtilisateurs.setItems(utilisateursList);
    }
    
    private void initializeTicketsTable() {
        if (tableTickets == null) return;
        
        colTicketId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTicketCode.setCellValueFactory(new PropertyValueFactory<>("codeQR"));
        colTicketMatch.setCellValueFactory(cellData -> {
            Ticket t = cellData.getValue();
            if (t.getMatch() != null) {
                return new SimpleStringProperty(t.getMatch().getEquipeDomicile().getNom() + " vs " + 
                    t.getMatch().getEquipeExterieur().getNom());
            }
            return new SimpleStringProperty("");
        });
        colTicketSpectateur.setCellValueFactory(cellData -> {
            Ticket t = cellData.getValue();
            if (t.getProprietaire() != null) {
                return new SimpleStringProperty(t.getProprietaire().getNom() + " " + t.getProprietaire().getPrenom());
            }
            return new SimpleStringProperty("");
        });
        colTicketCategorie.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getCategorie().name()));
        colTicketStatut.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getStatut().name()));
        colTicketPrix.setCellValueFactory(cellData -> 
            new SimpleStringProperty(formatMontant(cellData.getValue().getPrix())));
        
        tableTickets.setItems(ticketsList);
    }
    
    private void initializeAlertesTable() {
        if (tableAlertes == null) return;
        
        colAlerteId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAlerteTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colAlerteType.setCellValueFactory(cellData -> {
            TypeAlerte type = cellData.getValue().getTypeAlerte();
            return new SimpleStringProperty(type != null ? type.getLibelle() : "");
        });
        colAlerteNiveau.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getNiveau().getLibelle()));
        colAlerteDate.setCellValueFactory(cellData -> 
            new SimpleStringProperty(formatDate(cellData.getValue().getDateCreation())));
        
        // Colonne source
        if (colAlerteSource != null) {
            colAlerteSource.setCellValueFactory(cellData -> {
                Alerte a = cellData.getValue();
                if (a.estCreeeParUtilisateur() && a.getCreeePar() != null) {
                    return new SimpleStringProperty("👤 " + a.getCreeePar().getNom() + " " + a.getCreeePar().getPrenom());
                }
                return new SimpleStringProperty(a.getSource() != null ? a.getSource() : "Système");
            });
        }
        
        // Colonne statut améliorée
        colAlerteResolue.setCellValueFactory(cellData -> {
            Alerte a = cellData.getValue();
            String statut;
            if (a.isResolue()) {
                statut = "✅ Résolue";
            } else if (a.estCreeeParUtilisateur()) {
                if (a.estLue()) {
                    statut = "👁️ Lue";
                } else {
                    statut = "🔴 Non lue";
                }
            } else {
                statut = "⏳ En attente";
            }
            return new SimpleStringProperty(statut);
        });
        
        tableAlertes.setItems(alertesList);
        
        // Sélection pour afficher le détail
        tableAlertes.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSel, newSel) -> {
                if (newSel != null && newSel.estCreeeParUtilisateur()) {
                    showDetailAlerteUtilisateur(newSel);
                } else {
                    hideDetailAlerteUtilisateur();
                }
            }
        );
    }
    
    private void showDetailAlerteUtilisateur(Alerte alerte) {
        if (detailAlerteUtilisateurPane != null) {
            detailAlerteUtilisateurPane.setVisible(true);
            detailAlerteUtilisateurPane.setManaged(true);
        }
        if (lblMessageUtilisateur != null) {
            lblMessageUtilisateur.setText(alerte.getMessage());
        }
        if (lblInfosUtilisateur != null && alerte.getCreeePar() != null) {
            Spectateur user = alerte.getCreeePar();
            String infos = "Utilisateur: " + user.getNom() + " " + user.getPrenom() + "\n" +
                          "Email: " + user.getEmail() + "\n" +
                          "Date: " + formatDate(alerte.getDateCreation());
            if (alerte.aReponse()) {
                infos += "\n\n✅ Réponse envoyée le " + formatDate(alerte.getDateReponse());
            }
            lblInfosUtilisateur.setText(infos);
        }
    }
    
    private void hideDetailAlerteUtilisateur() {
        if (detailAlerteUtilisateurPane != null) {
            detailAlerteUtilisateurPane.setVisible(false);
            detailAlerteUtilisateurPane.setManaged(false);
        }
    }
    
    private void initializeRapportsTable() {
        if (tableRapports == null) return;
        
        colRapportId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRapportTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colRapportType.setCellValueFactory(new PropertyValueFactory<>("typeRapport"));
        colRapportDate.setCellValueFactory(cellData -> 
            new SimpleStringProperty(formatDate(cellData.getValue().getDateGeneration())));
        
        tableRapports.setItems(rapportsList);
    }
    
    private void initializeFluxTable() {
        if (tableFlux == null) return;
        
        colFluxMatch.setCellValueFactory(cellData -> {
            FluxSpectateurs f = cellData.getValue();
            if (f.getMatch() != null) {
                return new SimpleStringProperty(f.getMatch().getEquipeDomicile().getNom() + " vs " + 
                    f.getMatch().getEquipeExterieur().getNom());
            }
            return new SimpleStringProperty("");
        });
        colFluxZone.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getZone().getNom()));
        colFluxEntrees.setCellValueFactory(new PropertyValueFactory<>("nombreEntrees"));
        colFluxSorties.setCellValueFactory(new PropertyValueFactory<>("nombreSorties"));
        colFluxDensite.setCellValueFactory(cellData -> 
            new SimpleStringProperty(String.format("%.1f%%", cellData.getValue().getDensite())));
        
        tableFlux.setItems(fluxList);
    }
    
    private void initializeFilters() {
        if (cmbFilterPhase != null) {
            cmbFilterPhase.setItems(FXCollections.observableArrayList(PhaseMatch.values()));
            cmbFilterPhase.getItems().add(0, null);
        }
        
        if (cmbFilterStatut != null) {
            cmbFilterStatut.setItems(FXCollections.observableArrayList(StatutTicket.values()));
            cmbFilterStatut.getItems().add(0, null);
        }
        
        if (cmbTypeRapport != null) {
            cmbTypeRapport.setItems(FXCollections.observableArrayList(
                "Rapport de ventes", "Rapport par match", "Rapport d'alertes"
            ));
        }
        
        if (dpDateDebut != null) {
            dpDateDebut.setValue(LocalDate.now().minusMonths(1));
        }
        if (dpDateFin != null) {
            dpDateFin.setValue(LocalDate.now());
        }
    }
    
    // ===== NAVIGATION =====
    
    @FXML
    private void showDashboard() {
        showPane(dashboardPane);
        refreshDashboardStats();
    }
    
    @FXML
    private void showMatchs() {
        showPane(matchsPane);
        refreshMatchs();
    }
    
    @FXML
    private void showUtilisateurs() {
        showPane(utilisateursPane);
        refreshUtilisateurs();
    }
    
    @FXML
    private void showTickets() {
        showPane(ticketsPane);
        refreshTickets();
    }
    
    @FXML
    private void showAlertes() {
        showPane(alertesPane);
        refreshAlertes();
    }
    
    @FXML
    private void showRapports() {
        showPane(rapportsPane);
        refreshRapports();
    }
    
    @FXML
    private void showFlux() {
        showPane(fluxPane);
        refreshFlux();
    }
    
    private void showPane(VBox pane) {
        // Cacher tous les panneaux
        if (dashboardPane != null) { dashboardPane.setVisible(false); dashboardPane.setManaged(false); }
        if (matchsPane != null) { matchsPane.setVisible(false); matchsPane.setManaged(false); }
        if (utilisateursPane != null) { utilisateursPane.setVisible(false); utilisateursPane.setManaged(false); }
        if (ticketsPane != null) { ticketsPane.setVisible(false); ticketsPane.setManaged(false); }
        if (alertesPane != null) { alertesPane.setVisible(false); alertesPane.setManaged(false); }
        if (rapportsPane != null) { rapportsPane.setVisible(false); rapportsPane.setManaged(false); }
        if (fluxPane != null) { fluxPane.setVisible(false); fluxPane.setManaged(false); }
        
        // Afficher le panneau sélectionné
        if (pane != null) {
            pane.setVisible(true);
            pane.setManaged(true);
        }
    }
    
    // ===== RAFRAÎCHISSEMENT DES DONNÉES =====
    
    private void refreshAllData() {
        refreshDashboardStats();
        refreshMatchs();
        refreshUtilisateurs();
        refreshTickets();
        refreshAlertes();
        refreshRapports();
        refreshFlux();
    }
    
    private void refreshDashboardStats() {
        // Statistiques de base
        List<Match> allMatchs = matchService.getAllMatchs();
        List<Ticket> allTickets = ticketService.getAllTickets();
        List<Spectateur> allSpectateurs = authService.getAllSpectateurs();
        List<Alerte> alertesNonResolues = alerteService.getAlertesNonResolues();
        
        if (lblTotalMatchs != null) lblTotalMatchs.setText(String.valueOf(allMatchs.size()));
        if (lblTotalTickets != null) lblTotalTickets.setText(String.valueOf(allTickets.size()));
        if (lblTotalSpectateurs != null) lblTotalSpectateurs.setText(String.valueOf(allSpectateurs.size()));
        if (lblTotalAlertes != null) lblTotalAlertes.setText(String.valueOf(alertesNonResolues.size()));
        
        // Chiffre d'affaires
        double ca = allTickets.stream()
                .filter(t -> t.getStatut() == StatutTicket.VALIDE || t.getStatut() == StatutTicket.UTILISE)
                .mapToDouble(Ticket::getPrix)
                .sum();
        if (lblChiffreAffaires != null) lblChiffreAffaires.setText(formatMontant(ca));
        
        // Graphiques
        updateCharts(allTickets, allMatchs);
    }
    
    private void updateCharts(List<Ticket> tickets, List<Match> matchs) {
        // PieChart - Tickets par catégorie
        if (chartTicketsByCategory != null) {
            Map<CategorieTicket, Long> ticketsByCategory = tickets.stream()
                    .collect(Collectors.groupingBy(Ticket::getCategorie, Collectors.counting()));
            
            chartTicketsByCategory.getData().clear();
            ticketsByCategory.forEach((cat, count) -> 
                chartTicketsByCategory.getData().add(new PieChart.Data(cat.name(), count)));
        }
        
        // BarChart - Tickets par match (top 5)
        if (chartTicketsByMatch != null) {
            chartTicketsByMatch.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Tickets vendus");
            
            Map<Match, Long> ticketsByMatch = tickets.stream()
                    .filter(t -> t.getMatch() != null)
                    .collect(Collectors.groupingBy(Ticket::getMatch, Collectors.counting()));
            
            ticketsByMatch.entrySet().stream()
                    .sorted(Map.Entry.<Match, Long>comparingByValue().reversed())
                    .limit(5)
                    .forEach(entry -> {
                        String label = entry.getKey().getEquipeDomicile().getNom().substring(0, 3) + " vs " +
                                      entry.getKey().getEquipeExterieur().getNom().substring(0, 3);
                        series.getData().add(new XYChart.Data<>(label, entry.getValue()));
                    });
            
            chartTicketsByMatch.getData().add(series);
        }
    }
    
    private void refreshMatchs() {
        matchsList.clear();
        matchsList.addAll(matchService.getAllMatchs());
    }
    
    private void refreshUtilisateurs() {
        utilisateursList.clear();
        utilisateursList.addAll(authService.getAllSpectateurs());
    }
    
    private void refreshTickets() {
        ticketsList.clear();
        ticketsList.addAll(ticketService.getAllTickets());
    }
    
    private void refreshAlertes() {
        alertesList.clear();
        
        if (afficherAlertesUtilisateurs) {
            // Afficher uniquement les alertes des utilisateurs
            alertesList.addAll(alerteService.getAlertesUtilisateurs());
        } else {
            // Afficher toutes les alertes
            alertesList.addAll(alerteService.getAllAlertes());
        }
        
        long nonResolues = alerteService.compterAlertesActives();
        if (lblAlertesNonResolues != null) {
            lblAlertesNonResolues.setText(nonResolues + " alerte(s) non résolue(s)");
        }
        
        // Compteur des alertes utilisateurs non lues
        long alertesUtilisateursNonLues = alerteService.compterAlertesUtilisateursNonLues();
        if (lblAlertesUtilisateurs != null) {
            if (alertesUtilisateursNonLues > 0) {
                lblAlertesUtilisateurs.setText("🔔 " + alertesUtilisateursNonLues + " message(s) utilisateur(s)");
                lblAlertesUtilisateurs.setStyle("-fx-background-color: #f56565; -fx-text-fill: white; -fx-padding: 5 10; -fx-background-radius: 15;");
            } else {
                lblAlertesUtilisateurs.setText("");
            }
        }
        
        hideDetailAlerteUtilisateur();
    }
    
    @FXML
    private void showToutesAlertes() {
        afficherAlertesUtilisateurs = false;
        if (btnTabToutesAlertes != null) {
            btnTabToutesAlertes.getStyleClass().add("active");
        }
        if (btnTabAlertesUtilisateurs != null) {
            btnTabAlertesUtilisateurs.getStyleClass().remove("active");
        }
        refreshAlertes();
    }
    
    @FXML
    private void showAlertesUtilisateurs() {
        afficherAlertesUtilisateurs = true;
        if (btnTabAlertesUtilisateurs != null) {
            btnTabAlertesUtilisateurs.getStyleClass().add("active");
        }
        if (btnTabToutesAlertes != null) {
            btnTabToutesAlertes.getStyleClass().remove("active");
        }
        refreshAlertes();
    }
    
    private void refreshRapports() {
        rapportsList.clear();
        rapportsList.addAll(rapportService.getAllRapports());
    }
    
    private void refreshFlux() {
        // Pour l'instant, afficher tous les flux
        // Dans une vraie implémentation, filtrer par match en cours
    }
    
    // ===== ACTIONS UTILISATEURS =====
    
    /**
     * Affiche les tickets d'un utilisateur dans une popup
     */
    private void showTicketsUtilisateur(Spectateur spectateur) {
        List<Ticket> tickets = ticketService.getTicketsBySpectateur(spectateur.getId());
        
        // Créer une boîte de dialogue personnalisée
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Tickets de " + spectateur.getNom() + " " + spectateur.getPrenom());
        dialog.setHeaderText("Liste des tickets achetés par " + spectateur.getEmail());
        
        // Créer un TableView pour les tickets
        TableView<Ticket> ticketTable = new TableView<>();
        ticketTable.setPrefHeight(300);
        ticketTable.setPrefWidth(600);
        
        TableColumn<Ticket, String> colCode = new TableColumn<>("Code");
        colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCodeQR()));
        colCode.setPrefWidth(150);
        
        TableColumn<Ticket, String> colMatch = new TableColumn<>("Match");
        colMatch.setCellValueFactory(data -> {
            Ticket t = data.getValue();
            if (t.getMatch() != null) {
                return new SimpleStringProperty(t.getMatch().getEquipeDomicile().getNom() + 
                    " vs " + t.getMatch().getEquipeExterieur().getNom());
            }
            return new SimpleStringProperty("");
        });
        colMatch.setPrefWidth(180);
        
        TableColumn<Ticket, String> colCateg = new TableColumn<>("Catégorie");
        colCateg.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategorie().name()));
        colCateg.setPrefWidth(100);
        
        TableColumn<Ticket, String> colPrix = new TableColumn<>("Prix");
        colPrix.setCellValueFactory(data -> new SimpleStringProperty(formatMontant(data.getValue().getPrix())));
        colPrix.setPrefWidth(80);
        
        TableColumn<Ticket, String> colStatut = new TableColumn<>("Statut");
        colStatut.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatut().name()));
        colStatut.setPrefWidth(90);
        
        ticketTable.getColumns().addAll(colCode, colMatch, colCateg, colPrix, colStatut);
        ticketTable.setItems(FXCollections.observableArrayList(tickets));
        
        // Info résumé
        double totalDepense = tickets.stream().mapToDouble(Ticket::getPrix).sum();
        Label lblResume = new Label(String.format("Total: %d ticket(s) - Dépense totale: %s", 
            tickets.size(), formatMontant(totalDepense)));
        lblResume.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 0 0;");
        
        VBox content = new VBox(10, ticketTable, lblResume);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        
        dialog.showAndWait();
    }
    
    // ===== ACTIONS MATCHS =====
    
    @FXML
    private void handleNouveauMatch() {
        // Récupérer les équipes disponibles
        List<Equipe> equipes = matchService.getAllEquipes();
        if (equipes.isEmpty()) {
            showWarning("Aucune équipe", "Aucune équipe n'est disponible dans la base de données.");
            return;
        }
        
        // Créer le dialogue
        Dialog<Match> dialog = new Dialog<>();
        dialog.setTitle("Nouveau match");
        dialog.setHeaderText("Créer un nouveau match");
        
        // Boutons
        ButtonType btnCreer = new ButtonType("Créer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnCreer, ButtonType.CANCEL);
        
        // Formulaire
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        ComboBox<Equipe> cmbEquipeDom = new ComboBox<>(FXCollections.observableArrayList(equipes));
        cmbEquipeDom.setPromptText("Équipe domicile");
        cmbEquipeDom.setPrefWidth(200);
        cmbEquipeDom.setConverter(new javafx.util.StringConverter<Equipe>() {
            @Override
            public String toString(Equipe e) { return e != null ? e.getNom() : ""; }
            @Override
            public Equipe fromString(String s) { return null; }
        });
        
        ComboBox<Equipe> cmbEquipeExt = new ComboBox<>(FXCollections.observableArrayList(equipes));
        cmbEquipeExt.setPromptText("Équipe extérieur");
        cmbEquipeExt.setPrefWidth(200);
        cmbEquipeExt.setConverter(cmbEquipeDom.getConverter());
        
        DatePicker dpDate = new DatePicker(LocalDate.now().plusDays(7));
        Spinner<Integer> spHeure = new Spinner<>(0, 23, 20);
        spHeure.setPrefWidth(80);
        Spinner<Integer> spMinute = new Spinner<>(0, 59, 0, 15);
        spMinute.setPrefWidth(80);
        
        TextField txtStade = new TextField();
        txtStade.setPromptText("Nom du stade");
        
        TextField txtVille = new TextField();
        txtVille.setPromptText("Ville");
        
        ComboBox<PhaseMatch> cmbPhase = new ComboBox<>(FXCollections.observableArrayList(PhaseMatch.values()));
        cmbPhase.setPromptText("Phase");
        cmbPhase.setValue(PhaseMatch.PHASE_GROUPES);
        
        Spinner<Integer> spCapacite = new Spinner<>(10000, 100000, 72500, 5000);
        spCapacite.setEditable(true);
        
        Spinner<Double> spPrix = new Spinner<>(50.0, 500.0, 100.0, 10.0);
        spPrix.setEditable(true);
        
        grid.add(new Label("Équipe domicile:"), 0, 0);
        grid.add(cmbEquipeDom, 1, 0);
        grid.add(new Label("Équipe extérieur:"), 0, 1);
        grid.add(cmbEquipeExt, 1, 1);
        grid.add(new Label("Date:"), 0, 2);
        grid.add(dpDate, 1, 2);
        grid.add(new Label("Heure:"), 0, 3);
        HBox hboxHeure = new HBox(5, spHeure, new Label(":"), spMinute);
        grid.add(hboxHeure, 1, 3);
        grid.add(new Label("Stade:"), 0, 4);
        grid.add(txtStade, 1, 4);
        grid.add(new Label("Ville:"), 0, 5);
        grid.add(txtVille, 1, 5);
        grid.add(new Label("Phase:"), 0, 6);
        grid.add(cmbPhase, 1, 6);
        grid.add(new Label("Capacité:"), 0, 7);
        grid.add(spCapacite, 1, 7);
        grid.add(new Label("Prix de base (€):"), 0, 8);
        grid.add(spPrix, 1, 8);
        
        dialog.getDialogPane().setContent(grid);
        
        // Validation et création
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnCreer) {
                Equipe dom = cmbEquipeDom.getValue();
                Equipe ext = cmbEquipeExt.getValue();
                
                if (dom == null || ext == null) {
                    showWarning("Équipes requises", "Veuillez sélectionner les deux équipes.");
                    return null;
                }
                if (dom.equals(ext)) {
                    showWarning("Équipes identiques", "Les deux équipes doivent être différentes.");
                    return null;
                }
                if (txtStade.getText().trim().isEmpty() || txtVille.getText().trim().isEmpty()) {
                    showWarning("Champs requis", "Veuillez remplir le stade et la ville.");
                    return null;
                }
                
                LocalDateTime dateHeure = dpDate.getValue().atTime(spHeure.getValue(), spMinute.getValue());
                
                return matchService.creerMatch(
                    dom, ext, dateHeure,
                    txtStade.getText().trim(),
                    txtVille.getText().trim(),
                    cmbPhase.getValue(),
                    spPrix.getValue(),
                    spCapacite.getValue()
                );
            }
            return null;
        });
        
        Optional<Match> result = dialog.showAndWait();
        result.ifPresent(match -> {
            showSuccess("Match créé", "Le match " + match.getEquipeDomicile().getNom() + 
                " vs " + match.getEquipeExterieur().getNom() + " a été créé avec succès.");
            refreshMatchs();
            refreshDashboardStats();
        });
    }
    
    @FXML
    private void handleModifierMatch() {
        Match selected = tableMatchs.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Sélection requise", "Veuillez sélectionner un match à modifier.");
            return;
        }
        showInfo("Modifier match", "Modification du match: " + selected.getId());
    }
    
    @FXML
    private void handleSupprimerMatch() {
        Match selected = tableMatchs.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Sélection requise", "Veuillez sélectionner un match à supprimer.");
            return;
        }
        
        Optional<ButtonType> result = showConfirmation("Supprimer match", 
                "Êtes-vous sûr de vouloir supprimer ce match ?");
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            matchService.supprimerMatch(selected.getId());
            refreshMatchs();
            showSuccess("Succès", "Match supprimé avec succès.");
        }
    }
    
    @FXML
    private void handleRechercherMatch() {
        String search = txtSearchMatch.getText().trim().toLowerCase();
        if (search.isEmpty()) {
            refreshMatchs();
            return;
        }
        
        List<Match> filtered = matchService.getAllMatchs().stream()
                .filter(m -> m.getEquipeDomicile().getNom().toLowerCase().contains(search) ||
                            m.getEquipeExterieur().getNom().toLowerCase().contains(search) ||
                            m.getStade().toLowerCase().contains(search))
                .collect(Collectors.toList());
        
        matchsList.clear();
        matchsList.addAll(filtered);
    }
    
    // ===== ACTIONS ALERTES =====
    
    @FXML
    private void handleResoudreAlerte() {
        Alerte selected = tableAlertes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Sélection requise", "Veuillez sélectionner une alerte à résoudre.");
            return;
        }
        
        if (selected.isResolue()) {
            showInfo("Information", "Cette alerte est déjà résolue.");
            return;
        }
        
        // Dialogue pour la solution
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Résoudre l'alerte");
        dialog.setHeaderText("Alerte: " + selected.getTitre());
        dialog.setContentText("Solution appliquée:");
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(solution -> {
            if (currentUser instanceof Administrateur) {
                alerteService.resoudreAlerte(selected.getId(), (Administrateur) currentUser, solution);
            }
            refreshAlertes();
            showSuccess("Succès", "Alerte résolue avec succès.");
        });
    }
    
    @FXML
    private void handleMarquerLue() {
        Alerte selected = tableAlertes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Sélection requise", "Veuillez sélectionner une alerte.");
            return;
        }
        
        if (!selected.estCreeeParUtilisateur()) {
            showInfo("Information", "Cette action est réservée aux alertes des utilisateurs.");
            return;
        }
        
        if (selected.estLue()) {
            showInfo("Information", "Cette alerte est déjà marquée comme lue.");
            return;
        }
        
        if (currentUser instanceof Administrateur) {
            alerteService.marquerCommeLue(selected.getId(), (Administrateur) currentUser);
            refreshAlertes();
            showSuccess("Succès", "L'alerte a été marquée comme lue.");
        }
    }
    
    @FXML
    private void handleRepondreAlerte() {
        Alerte selected = tableAlertes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Sélection requise", "Veuillez sélectionner une alerte à laquelle répondre.");
            return;
        }
        
        if (!selected.estCreeeParUtilisateur()) {
            showInfo("Information", "Cette action est réservée aux alertes des utilisateurs.");
            return;
        }
        
        if (selected.aReponse()) {
            showInfo("Information", "Cette alerte a déjà reçu une réponse.");
            return;
        }
        
        // Dialogue pour la réponse
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Répondre à l'utilisateur");
        
        // Info utilisateur
        String headerText = "Alerte de: ";
        if (selected.getCreeePar() != null) {
            headerText += selected.getCreeePar().getNom() + " " + selected.getCreeePar().getPrenom();
            headerText += "\nType: " + (selected.getTypeAlerte() != null ? selected.getTypeAlerte().getLibelle() : "");
            headerText += "\nSujet: " + selected.getTitre();
        }
        dialog.setHeaderText(headerText);
        
        // Boutons
        ButtonType btnEnvoyer = new ButtonType("Envoyer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnEnvoyer, ButtonType.CANCEL);
        
        // Contenu
        VBox content = new VBox(10);
        content.setPadding(new javafx.geometry.Insets(10));
        
        Label lblMessage = new Label("Message de l'utilisateur:");
        lblMessage.setStyle("-fx-font-weight: bold;");
        
        TextArea txtUserMessage = new TextArea(selected.getMessage());
        txtUserMessage.setEditable(false);
        txtUserMessage.setWrapText(true);
        txtUserMessage.setPrefRowCount(3);
        txtUserMessage.setStyle("-fx-background-color: #f7fafc;");
        
        Label lblReponse = new Label("Votre réponse:");
        lblReponse.setStyle("-fx-font-weight: bold;");
        
        TextArea txtReponse = new TextArea();
        txtReponse.setPromptText("Saisissez votre réponse à l'utilisateur...");
        txtReponse.setWrapText(true);
        txtReponse.setPrefRowCount(5);
        
        content.getChildren().addAll(lblMessage, txtUserMessage, lblReponse, txtReponse);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setPrefWidth(500);
        
        // Focus sur la réponse
        txtReponse.requestFocus();
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnEnvoyer) {
                return txtReponse.getText().trim();
            }
            return null;
        });
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(reponse -> {
            if (reponse.isEmpty()) {
                showWarning("Réponse vide", "Veuillez saisir une réponse.");
                return;
            }
            
            if (currentUser instanceof Administrateur) {
                alerteService.repondreAlerte(selected.getId(), (Administrateur) currentUser, reponse);
                refreshAlertes();
                showSuccess("Réponse envoyée", "Votre réponse a été envoyée à l'utilisateur.");
            }
        });
    }
    
    // ===== ACTIONS UTILISATEURS =====
    
    @FXML
    private void handleRechercherUser() {
        String search = txtSearchUser != null ? txtSearchUser.getText().trim().toLowerCase() : "";
        if (search.isEmpty()) {
            refreshUtilisateurs();
            return;
        }
        
        List<Spectateur> filtered = authService.getAllSpectateurs().stream()
                .filter(s -> s.getNom().toLowerCase().contains(search) ||
                            s.getPrenom().toLowerCase().contains(search) ||
                            s.getEmail().toLowerCase().contains(search))
                .collect(Collectors.toList());
        
        utilisateursList.clear();
        utilisateursList.addAll(filtered);
    }
    
    @FXML
    private void handleVoirTicketsUser() {
        Spectateur selected = tableUtilisateurs.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Sélection requise", "Veuillez sélectionner un utilisateur.");
            return;
        }
        showTicketsUtilisateur(selected);
    }
    
    @FXML
    private void handleDesactiverUser() {
        Spectateur selected = tableUtilisateurs.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Sélection requise", "Veuillez sélectionner un utilisateur.");
            return;
        }
        
        boolean estActif = selected.getActif() != null && selected.getActif();
        String action = estActif ? "désactiver" : "réactiver";
        Optional<ButtonType> result = showConfirmation("Confirmer", 
            "Voulez-vous vraiment " + action + " l'utilisateur " + selected.getEmail() + " ?");
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            selected.setActif(!estActif);
            authService.mettreAJourProfil(selected);
            refreshUtilisateurs();
            showSuccess("Succès", "Utilisateur " + (selected.getActif() ? "réactivé" : "désactivé") + ".");
        }
    }
    
    // ===== ACTIONS RAPPORTS =====
    
    @FXML
    private void handleGenererRapport() {
        String type = cmbTypeRapport.getValue();
        LocalDate debut = dpDateDebut.getValue();
        LocalDate fin = dpDateFin.getValue();
        
        if (type == null) {
            showWarning("Type requis", "Veuillez sélectionner un type de rapport.");
            return;
        }
        
        Rapport rapport = null;
        Administrateur admin = (Administrateur) currentUser;
        
        switch (type) {
            case "Rapport de ventes":
                rapport = rapportService.genererRapportVentes(
                        admin, debut.atStartOfDay(), fin.atTime(23, 59, 59));
                break;
            case "Rapport par match":
                Match selectedMatch = tableMatchs.getSelectionModel().getSelectedItem();
                if (selectedMatch != null) {
                    rapport = rapportService.genererRapportMatch(admin, selectedMatch);
                } else {
                    showWarning("Match requis", "Veuillez sélectionner un match pour ce type de rapport.");
                    return;
                }
                break;
            case "Rapport d'alertes":
                rapport = rapportService.genererRapportAlertes(admin);
                break;
        }
        
        if (rapport != null) {
            refreshRapports();
            showSuccess("Rapport généré", "Le rapport a été généré avec succès.");
        }
    }
    
    @FXML
    private void handleExporterRapport() {
        Rapport selected = tableRapports.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Sélection requise", "Veuillez sélectionner un rapport à exporter.");
            return;
        }
        
        String path = rapportService.exporterPDF(selected);
        if (path != null) {
            showSuccess("Export réussi", "Rapport exporté vers: " + path);
        } else {
            showError("Erreur", "Impossible d'exporter le rapport.");
        }
    }
    
    // ===== DÉCONNEXION =====
    
    @FXML
    private void handleDeconnexionAdmin(ActionEvent event) {
        handleDeconnexion(event);
    }
}
