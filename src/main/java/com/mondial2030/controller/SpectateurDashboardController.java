package com.mondial2030.controller;

import com.mondial2030.entity.*;
import com.mondial2030.service.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Contrôleur du tableau de bord spectateur.
 * Gère la consultation des matchs, l'achat et le transfert de tickets.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class SpectateurDashboardController extends BaseController implements Initializable {
    
    // Services
    private final MatchService matchService = MatchService.getInstance();
    private final TicketService ticketService = TicketService.getInstance();
    private final AlerteService alerteService = AlerteService.getInstance();
    
    // ===== COMPOSANTS HEADER =====
    @FXML private Label lblUserName;
    @FXML private Label lblWelcome;
    
    // ===== COMPOSANTS NAVIGATION =====
    @FXML private Button btnAccueil;
    @FXML private Button btnMatchs;
    @FXML private Button btnMesTickets;
    @FXML private Button btnHistorique;
    @FXML private Button btnMesAlertes;
    @FXML private Button btnProfil;
    
    // ===== PANNEAUX DE CONTENU =====
    @FXML private StackPane contentPane;
    @FXML private VBox accueilPane;
    @FXML private VBox matchsPane;
    @FXML private VBox mesTicketsPane;
    @FXML private VBox historiquePane;
    @FXML private VBox mesAlertesPane;
    @FXML private VBox profilPane;
    @FXML private VBox detailMatchPane;
    
    // ===== ACCUEIL =====
    @FXML private Label lblProchainMatch;
    @FXML private Label lblNbTickets;
    @FXML private VBox prochainMatchCard;
    @FXML private ListView<Match> listMatchsAVenir;
    
    // ===== LISTE DES MATCHS =====
    @FXML private TableView<Match> tableMatchs;
    @FXML private TableColumn<Match, String> colMatchEquipes;
    @FXML private TableColumn<Match, String> colMatchDate;
    @FXML private TableColumn<Match, String> colMatchStade;
    @FXML private TableColumn<Match, String> colMatchPhase;
    @FXML private TableColumn<Match, String> colMatchDisponibilite;
    @FXML private TextField txtSearchMatch;
    @FXML private ComboBox<PhaseMatch> cmbFilterPhase;
    @FXML private DatePicker dpFilterDate;
    
    // ===== DÉTAIL MATCH =====
    @FXML private Label lblDetailEquipes;
    @FXML private Label lblDetailDate;
    @FXML private Label lblDetailStade;
    @FXML private Label lblDetailPhase;
    @FXML private Label lblDetailGroupe;
    @FXML private ImageView imgEquipeDomicile;
    @FXML private ImageView imgEquipeExterieur;
    @FXML private TableView<Zone> tableZones;
    @FXML private TableColumn<Zone, String> colZoneNom;
    @FXML private TableColumn<Zone, String> colZoneType;
    @FXML private TableColumn<Zone, Integer> colZoneCapacite;
    @FXML private TableColumn<Zone, Integer> colZoneDisponible;
    @FXML private TableColumn<Zone, String> colZonePrix;
    @FXML private ComboBox<CategorieTicket> cmbCategorie;
    @FXML private Spinner<Integer> spinnerQuantite;
    @FXML private Label lblPrixTotal;
    private Match selectedMatch;
    
    // ===== MES TICKETS =====
    @FXML private TableView<Ticket> tableMesTickets;
    @FXML private TableColumn<Ticket, String> colTicketCode;
    @FXML private TableColumn<Ticket, String> colTicketMatch;
    @FXML private TableColumn<Ticket, String> colTicketDate;
    @FXML private TableColumn<Ticket, String> colTicketZone;
    @FXML private TableColumn<Ticket, String> colTicketSiege;
    @FXML private TableColumn<Ticket, String> colTicketStatut;
    @FXML private ImageView imgQRCode;
    @FXML private Label lblTicketDetails;
    @FXML private Label lblTicketCode;
    @FXML private Label lblTicketEquipes;
    @FXML private Label lblTicketDate;
    @FXML private Label lblTicketStade;
    @FXML private Label lblTicketZone;
    @FXML private Label lblTicketCategorie;
    @FXML private Label lblTicketPrix;
    @FXML private Label lblTicketStatut;
    
    // ===== HISTORIQUE =====
    @FXML private TableView<Transaction> tableHistorique;
    @FXML private TableColumn<Transaction, String> colHistDate;
    @FXML private TableColumn<Transaction, String> colHistType;
    @FXML private TableColumn<Transaction, String> colHistMontant;
    @FXML private TableColumn<Transaction, String> colHistStatut;
    @FXML private TableColumn<Transaction, String> colHistReference;
    
    // ===== MES ALERTES =====
    @FXML private TableView<Alerte> tableMesAlertes;
    @FXML private TableColumn<Alerte, String> colMesAlertesDate;
    @FXML private TableColumn<Alerte, String> colMesAlertesTitre;
    @FXML private TableColumn<Alerte, String> colMesAlertesType;
    @FXML private TableColumn<Alerte, String> colMesAlertesStatut;
    @FXML private TableColumn<Alerte, String> colMesAlertesReponse;
    @FXML private VBox detailAlertePane;
    @FXML private Label lblDetailMessage;
    @FXML private HBox reponseAdminBox;
    @FXML private Label lblReponseAdmin;
    @FXML private Label lblDateReponse;
    
    // ===== PROFIL =====
    @FXML private TextField txtProfilNom;
    @FXML private TextField txtProfilPrenom;
    @FXML private TextField txtProfilEmail;
    @FXML private TextField txtProfilTelephone;
    @FXML private PasswordField txtOldPassword;
    @FXML private PasswordField txtNewPassword;
    @FXML private PasswordField txtConfirmPassword;
    
    // Listes observables
    private ObservableList<Match> matchsList = FXCollections.observableArrayList();
    private ObservableList<Ticket> ticketsList = FXCollections.observableArrayList();
    private ObservableList<Transaction> transactionsList = FXCollections.observableArrayList();
    private ObservableList<Zone> zonesList = FXCollections.observableArrayList();
    private ObservableList<Alerte> mesAlertesList = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Initialisation du dashboard spectateur");
        
        // Initialiser les tableaux
        initializeMatchsTable();
        initializeTicketsTable();
        initializeHistoriqueTable();
        initializeZonesTable();
        initializeMesAlertesTable();
        
        // Initialiser les filtres
        initializeFilters();
        
        // Initialiser le spinner de quantité
        if (spinnerQuantite != null) {
            spinnerQuantite.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
            spinnerQuantite.valueProperty().addListener((obs, oldVal, newVal) -> updatePrixTotal());
        }
        
        // Afficher l'accueil par défaut
        showPane(accueilPane);
    }
    
    @Override
    protected void onUserSet() {
        if (currentUser != null) {
            if (lblUserName != null) {
                lblUserName.setText(currentUser.getNom() + " " + currentUser.getPrenom());
            }
            if (lblWelcome != null) {
                lblWelcome.setText("Bienvenue, " + currentUser.getPrenom() + " !");
            }
            
            // Charger les données
            refreshAllData();
            loadProfilData();
        }
    }
    
    // ===== INITIALISATION DES TABLEAUX =====
    
    private void initializeMatchsTable() {
        if (tableMatchs == null) return;
        
        colMatchEquipes.setCellValueFactory(cellData -> {
            Match m = cellData.getValue();
            String equipes = m.getEquipeDomicile().getNom() + " vs " + m.getEquipeExterieur().getNom();
            return new SimpleStringProperty(equipes);
        });
        colMatchDate.setCellValueFactory(cellData -> 
            new SimpleStringProperty(formatDate(cellData.getValue().getDateHeure())));
        colMatchStade.setCellValueFactory(new PropertyValueFactory<>("stade"));
        colMatchPhase.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getPhase().name().replace("_", " ")));
        colMatchDisponibilite.setCellValueFactory(cellData -> {
            Match m = cellData.getValue();
            int dispo = m.getPlacesDisponibles();
            return new SimpleStringProperty(dispo > 0 ? dispo + " places" : "Complet");
        });
        
        // Double-clic pour voir les détails
        tableMatchs.setRowFactory(tv -> {
            TableRow<Match> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showDetailMatch(row.getItem());
                }
            });
            return row;
        });
        
        tableMatchs.setItems(matchsList);
    }
    
    private void initializeTicketsTable() {
        if (tableMesTickets == null) return;
        
        colTicketCode.setCellValueFactory(new PropertyValueFactory<>("codeQR"));
        colTicketMatch.setCellValueFactory(cellData -> {
            Ticket t = cellData.getValue();
            if (t.getMatch() != null) {
                return new SimpleStringProperty(t.getMatch().getEquipeDomicile().getNom() + " vs " + 
                    t.getMatch().getEquipeExterieur().getNom());
            }
            return new SimpleStringProperty("");
        });
        colTicketDate.setCellValueFactory(cellData -> {
            Ticket t = cellData.getValue();
            if (t.getMatch() != null) {
                return new SimpleStringProperty(formatDate(t.getMatch().getDateHeure()));
            }
            return new SimpleStringProperty("");
        });
        colTicketZone.setCellValueFactory(cellData -> {
            Ticket t = cellData.getValue();
            if (t.getZone() != null) {
                return new SimpleStringProperty(t.getZone().getNom());
            }
            return new SimpleStringProperty("");
        });
        colTicketSiege.setCellValueFactory(cellData -> {
            Ticket t = cellData.getValue();
            if (t.getSiege() != null) {
                return new SimpleStringProperty(t.getSiege().getNumero());
            }
            return new SimpleStringProperty("Non assigné");
        });
        colTicketStatut.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getStatut().name()));
        
        // Sélection pour afficher QR code
        tableMesTickets.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSel, newSel) -> {
                if (newSel != null) {
                    showTicketDetails(newSel);
                }
            }
        );
        
        tableMesTickets.setItems(ticketsList);
    }
    
    private void initializeHistoriqueTable() {
        if (tableHistorique == null) return;
        
        colHistDate.setCellValueFactory(cellData -> 
            new SimpleStringProperty(formatDate(cellData.getValue().getDateTransaction())));
        colHistType.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getType().name()));
        colHistMontant.setCellValueFactory(cellData -> 
            new SimpleStringProperty(formatMontant(cellData.getValue().getMontant())));
        colHistStatut.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getStatut().name()));
        colHistReference.setCellValueFactory(new PropertyValueFactory<>("referenceExterne"));
        
        tableHistorique.setItems(transactionsList);
    }
    
    private void initializeMesAlertesTable() {
        if (tableMesAlertes == null) return;
        
        colMesAlertesDate.setCellValueFactory(cellData -> 
            new SimpleStringProperty(formatDate(cellData.getValue().getDateCreation())));
        colMesAlertesTitre.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getTitre()));
        colMesAlertesType.setCellValueFactory(cellData -> {
            TypeAlerte type = cellData.getValue().getTypeAlerte();
            return new SimpleStringProperty(type != null ? type.getLibelle() : "");
        });
        colMesAlertesStatut.setCellValueFactory(cellData -> {
            Alerte a = cellData.getValue();
            String statut;
            if (a.aReponse()) {
                statut = "✅ Répondu";
            } else if (a.estLue()) {
                statut = "👁️ Lu";
            } else {
                statut = "⏳ En attente";
            }
            return new SimpleStringProperty(statut);
        });
        colMesAlertesReponse.setCellValueFactory(cellData -> {
            String reponse = cellData.getValue().getReponseAdmin();
            return new SimpleStringProperty(reponse != null ? reponse : "—");
        });
        
        tableMesAlertes.setItems(mesAlertesList);
        
        // Sélection pour afficher le détail
        tableMesAlertes.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSel, newSel) -> {
                if (newSel != null) {
                    showDetailAlerte(newSel);
                } else {
                    hideDetailAlerte();
                }
            }
        );
    }
    
    private void showDetailAlerte(Alerte alerte) {
        if (detailAlertePane != null) {
            detailAlertePane.setVisible(true);
            detailAlertePane.setManaged(true);
        }
        if (lblDetailMessage != null) {
            lblDetailMessage.setText(alerte.getMessage());
        }
        
        // Afficher la réponse si elle existe
        if (reponseAdminBox != null) {
            if (alerte.aReponse()) {
                reponseAdminBox.setVisible(true);
                reponseAdminBox.setManaged(true);
                if (lblReponseAdmin != null) {
                    lblReponseAdmin.setText(alerte.getReponseAdmin());
                }
                if (lblDateReponse != null && alerte.getDateReponse() != null) {
                    lblDateReponse.setText("Répondu le " + formatDate(alerte.getDateReponse()));
                }
            } else {
                reponseAdminBox.setVisible(false);
                reponseAdminBox.setManaged(false);
            }
        }
    }
    
    private void hideDetailAlerte() {
        if (detailAlertePane != null) {
            detailAlertePane.setVisible(false);
            detailAlertePane.setManaged(false);
        }
    }
    
    private void initializeZonesTable() {
        if (tableZones == null) return;
        
        colZoneNom.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getNom()));
        colZoneType.setCellValueFactory(cellData -> {
            TypeZone type = cellData.getValue().getTypeZone();
            return new SimpleStringProperty(type != null ? type.name() : "");
        });
        colZoneCapacite.setCellValueFactory(cellData -> {
            Integer cap = cellData.getValue().getCapacite();
            return new javafx.beans.property.SimpleIntegerProperty(cap != null ? cap : 0).asObject();
        });
        colZoneDisponible.setCellValueFactory(cellData -> {
            Integer dispo = cellData.getValue().getPlacesDisponibles();
            return new javafx.beans.property.SimpleIntegerProperty(dispo != null ? dispo : 0).asObject();
        });
        colZonePrix.setCellValueFactory(cellData -> {
            // Calculer le prix directement sans passer par zone.getMatch() (évite LazyInitializationException)
            Zone zone = cellData.getValue();
            double prixBase = selectedMatch != null && selectedMatch.getPrixBase() != null 
                ? selectedMatch.getPrixBase() : 100.0;
            Double coefficient = zone.getCoefficientPrix();
            double prix = prixBase * (coefficient != null ? coefficient : 1.0);
            return new SimpleStringProperty(formatMontant(prix));
        });
        
        tableZones.setItems(zonesList);
        
        // Sélection pour mettre à jour le prix
        tableZones.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSel, newSel) -> updatePrixTotal()
        );
    }
    
    private void initializeFilters() {
        if (cmbFilterPhase != null) {
            cmbFilterPhase.setItems(FXCollections.observableArrayList(PhaseMatch.values()));
            cmbFilterPhase.getItems().add(0, null);
            cmbFilterPhase.setPromptText("Toutes les phases");
        }
        
        if (cmbCategorie != null) {
            cmbCategorie.setItems(FXCollections.observableArrayList(CategorieTicket.values()));
            cmbCategorie.setValue(CategorieTicket.STANDARD);
            cmbCategorie.valueProperty().addListener((obs, oldVal, newVal) -> updatePrixTotal());
        }
    }
    
    // ===== NAVIGATION =====
    
    @FXML
    private void showAccueil() {
        showPane(accueilPane);
        refreshAccueil();
    }
    
    @FXML
    private void showMatchs() {
        showPane(matchsPane);
        refreshMatchs();
    }
    
    @FXML
    private void showMesTickets() {
        showPane(mesTicketsPane);
        refreshMesTickets();
    }
    
    @FXML
    private void showHistorique() {
        showPane(historiquePane);
        refreshHistorique();
    }
    
    @FXML
    private void showProfil() {
        showPane(profilPane);
        loadProfilData();
    }
    
    @FXML
    private void showMesAlertes() {
        showPane(mesAlertesPane);
        refreshMesAlertes();
    }
    
    private void showPane(VBox pane) {
        // Cacher tous les panneaux
        hideAllPanes();
        
        // Afficher le panneau sélectionné
        if (pane != null) {
            pane.setVisible(true);
            pane.setManaged(true);
        }
    }
    
    private void hideAllPanes() {
        VBox[] panes = {accueilPane, matchsPane, mesTicketsPane, historiquePane, mesAlertesPane, profilPane, detailMatchPane};
        for (VBox pane : panes) {
            if (pane != null) {
                pane.setVisible(false);
                pane.setManaged(false);
            }
        }
    }
    
    // ===== RAFRAÎCHISSEMENT DES DONNÉES =====
    
    private void refreshAllData() {
        refreshAccueil();
        refreshMatchs();
        refreshMesTickets();
        refreshHistorique();
        refreshMesAlertes();
    }
    
    private void refreshAccueil() {
        Spectateur spectateur = (Spectateur) currentUser;
        
        // Nombre de tickets - utiliser le service au lieu de la collection lazy
        if (lblNbTickets != null) {
            List<Ticket> tickets = ticketService.getTicketsSpectateur(spectateur.getId());
            int nbTickets = tickets != null ? tickets.size() : 0;
            lblNbTickets.setText(nbTickets + " ticket(s)");
        }
        
        // Prochain match
        List<Match> matchsAVenir = matchService.getMatchsAVenir();
        if (lblProchainMatch != null && !matchsAVenir.isEmpty()) {
            Match prochain = matchsAVenir.get(0);
            lblProchainMatch.setText(prochain.getEquipeDomicile().getNom() + " vs " + 
                prochain.getEquipeExterieur().getNom() + "\n" +
                formatDate(prochain.getDateHeure()));
        }
        
        // Liste des matchs à venir
        if (listMatchsAVenir != null) {
            listMatchsAVenir.setItems(FXCollections.observableArrayList(
                matchsAVenir.stream().limit(5).collect(Collectors.toList())
            ));
        }
    }
    
    private void refreshMatchs() {
        matchsList.clear();
        matchsList.addAll(matchService.getMatchsAVenir());
    }
    
    private void refreshMesTickets() {
        Spectateur spectateur = (Spectateur) currentUser;
        ticketsList.clear();
        List<Ticket> tickets = ticketService.getTicketsSpectateur(spectateur.getId());
        if (tickets != null) {
            ticketsList.addAll(tickets);
        }
    }
    
    private void refreshHistorique() {
        Spectateur spectateur = (Spectateur) currentUser;
        transactionsList.clear();
        // Utiliser le DAO pour éviter LazyInitializationException
        com.mondial2030.dao.impl.TransactionDAOImpl transactionDAO = new com.mondial2030.dao.impl.TransactionDAOImpl();
        List<Transaction> transactions = transactionDAO.findBySpectateurId(spectateur.getId());
        if (transactions != null) {
            transactionsList.addAll(transactions);
        }
    }
    
    private void refreshMesAlertes() {
        if (currentUser == null) {
            logger.warn("refreshMesAlertes: currentUser est null");
            return;
        }
        Spectateur spectateur = (Spectateur) currentUser;
        logger.info("refreshMesAlertes: Chargement des alertes pour spectateur ID=" + spectateur.getId());
        mesAlertesList.clear();
        List<Alerte> alertes = alerteService.getAlertesSpectateur(spectateur.getId());
        logger.info("refreshMesAlertes: " + (alertes != null ? alertes.size() : 0) + " alertes trouvées");
        if (alertes != null) {
            mesAlertesList.addAll(alertes);
        }
        hideDetailAlerte();
    }
    
    // ===== GESTION DES ALERTES UTILISATEUR =====
    
    @FXML
    private void handleNouvelleAlerte() {
        // Créer le dialogue de création d'alerte
        Dialog<Alerte> dialog = new Dialog<>();
        dialog.setTitle("Nouvelle alerte");
        dialog.setHeaderText("Soumettre une alerte, question ou signalement");
        
        // Boutons
        ButtonType btnEnvoyer = new ButtonType("Envoyer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnEnvoyer, ButtonType.CANCEL);
        
        // Formulaire
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        // Type d'alerte (filtrer les types utilisateur seulement)
        ComboBox<TypeAlerte> cmbType = new ComboBox<>();
        cmbType.setItems(FXCollections.observableArrayList(
            TypeAlerte.SIGNALEMENT_UTILISATEUR,
            TypeAlerte.QUESTION_UTILISATEUR,
            TypeAlerte.RECLAMATION,
            TypeAlerte.ASSISTANCE
        ));
        cmbType.setPromptText("Type de demande");
        cmbType.setPrefWidth(250);
        cmbType.setConverter(new javafx.util.StringConverter<TypeAlerte>() {
            @Override
            public String toString(TypeAlerte t) { return t != null ? t.getLibelle() : ""; }
            @Override
            public TypeAlerte fromString(String s) { return null; }
        });
        
        // Niveau de priorité
        ComboBox<NiveauAlerte> cmbNiveau = new ComboBox<>();
        cmbNiveau.setItems(FXCollections.observableArrayList(
            NiveauAlerte.FAIBLE,
            NiveauAlerte.MOYEN,
            NiveauAlerte.ELEVE
        ));
        cmbNiveau.setValue(NiveauAlerte.MOYEN);
        cmbNiveau.setPrefWidth(250);
        cmbNiveau.setConverter(new javafx.util.StringConverter<NiveauAlerte>() {
            @Override
            public String toString(NiveauAlerte n) { return n != null ? n.getLibelle() : ""; }
            @Override
            public NiveauAlerte fromString(String s) { return null; }
        });
        
        TextField txtTitre = new TextField();
        txtTitre.setPromptText("Titre de votre demande");
        txtTitre.setPrefWidth(250);
        
        TextArea txtMessage = new TextArea();
        txtMessage.setPromptText("Décrivez votre demande en détail...");
        txtMessage.setPrefRowCount(5);
        txtMessage.setWrapText(true);
        txtMessage.setPrefWidth(250);
        
        grid.add(new Label("Type:"), 0, 0);
        grid.add(cmbType, 1, 0);
        grid.add(new Label("Priorité:"), 0, 1);
        grid.add(cmbNiveau, 1, 1);
        grid.add(new Label("Titre:"), 0, 2);
        grid.add(txtTitre, 1, 2);
        grid.add(new Label("Message:"), 0, 3);
        grid.add(txtMessage, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        
        // Désactiver le bouton Envoyer par défaut
        javafx.scene.Node btnEnvoyerNode = dialog.getDialogPane().lookupButton(btnEnvoyer);
        btnEnvoyerNode.setDisable(true);
        
        // Activer/désactiver le bouton selon la validité du formulaire
        Runnable validateForm = () -> {
            boolean isValid = cmbType.getValue() != null && 
                             !txtTitre.getText().trim().isEmpty() && 
                             !txtMessage.getText().trim().isEmpty();
            btnEnvoyerNode.setDisable(!isValid);
        };
        
        cmbType.valueProperty().addListener((obs, o, n) -> validateForm.run());
        txtTitre.textProperty().addListener((obs, o, n) -> validateForm.run());
        txtMessage.textProperty().addListener((obs, o, n) -> validateForm.run());
        
        // Focus sur le type par défaut
        cmbType.requestFocus();
        
        // Création de l'alerte
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnEnvoyer) {
                TypeAlerte type = cmbType.getValue();
                NiveauAlerte niveau = cmbNiveau.getValue();
                String titre = txtTitre.getText().trim();
                String message = txtMessage.getText().trim();
                
                try {
                    Spectateur spectateur = (Spectateur) currentUser;
                    logger.info("Création alerte: type=" + type + ", titre=" + titre + ", spectateur=" + spectateur.getId());
                    Alerte alerte = alerteService.creerAlerteUtilisateur(spectateur, titre, message, type, niveau);
                    logger.info("Alerte créée avec succès, ID=" + (alerte != null ? alerte.getId() : "null"));
                    return alerte;
                } catch (Exception e) {
                    logger.error("Erreur lors de la création de l'alerte: " + e.getMessage(), e);
                    showError("Erreur", "Impossible de créer l'alerte: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });
        
        Optional<Alerte> result = dialog.showAndWait();
        result.ifPresent(alerte -> {
            showSuccess("Alerte envoyée", "Votre demande a été envoyée. " +
                "L'équipe administrative vous répondra dans les plus brefs délais.");
            refreshMesAlertes();
        });
    }
    
    // ===== DÉTAIL MATCH =====
    
    private void showDetailMatch(Match match) {
        this.selectedMatch = match;
        hideAllPanes();
        
        if (detailMatchPane != null) {
            detailMatchPane.setVisible(true);
            detailMatchPane.setManaged(true);
        }
        
        // Remplir les informations
        if (lblDetailEquipes != null) {
            lblDetailEquipes.setText(match.getEquipeDomicile().getNom() + " vs " + 
                match.getEquipeExterieur().getNom());
        }
        if (lblDetailDate != null) {
            lblDetailDate.setText(formatDate(match.getDateHeure()));
        }
        if (lblDetailStade != null) {
            lblDetailStade.setText(match.getStade() + ", " + match.getVille());
        }
        if (lblDetailPhase != null) {
            lblDetailPhase.setText(match.getPhase().name().replace("_", " "));
        }
        if (lblDetailGroupe != null && match.getGroupe() != null) {
            lblDetailGroupe.setText("Groupe " + match.getGroupe());
        }
        
        // Charger les drapeaux des équipes
        loadTeamFlag(imgEquipeDomicile, match.getEquipeDomicile());
        loadTeamFlag(imgEquipeExterieur, match.getEquipeExterieur());
        
        // Charger les zones via le DAO pour éviter LazyInitializationException
        zonesList.clear();
        com.mondial2030.dao.impl.ZoneDAOImpl zoneDAO = new com.mondial2030.dao.impl.ZoneDAOImpl();
        List<Zone> zones = zoneDAO.findByMatchId(match.getId());
        if (zones != null && !zones.isEmpty()) {
            zonesList.addAll(zones);
            System.out.println("DEBUG - Zones chargées: " + zones.size());
            for (Zone z : zones) {
                System.out.println("DEBUG - Zone: " + z.getNom() + ", Capacité: " + z.getCapacite() + ", Dispo: " + z.getPlacesDisponibles());
            }
        } else {
            System.out.println("DEBUG - Aucune zone trouvée pour le match " + match.getId());
        }
        
        // Réinitialiser le prix
        updatePrixTotal();
    }
    
    /**
     * Charge le drapeau d'une équipe depuis flagcdn.com
     */
    private void loadTeamFlag(ImageView imageView, Equipe equipe) {
        if (imageView == null || equipe == null) return;
        
        String codePays = equipe.getCodePays();
        if (codePays != null && !codePays.isEmpty()) {
            try {
                // Mapper les codes FIFA (3 lettres) vers les codes ISO (2 lettres)
                String isoCode = mapFifaToIso(codePays);
                // Utiliser l'API flagcdn.com pour obtenir les drapeaux
                String flagUrl = "https://flagcdn.com/w160/" + isoCode.toLowerCase() + ".png";
                Image flagImage = new Image(flagUrl, 80, 80, true, true, true);
                imageView.setImage(flagImage);
            } catch (Exception e) {
                System.out.println("Impossible de charger le drapeau pour " + codePays);
            }
        }
    }
    
    /**
     * Mappe les codes FIFA (3 lettres) vers les codes ISO 3166-1 alpha-2
     */
    private String mapFifaToIso(String fifaCode) {
        Map<String, String> fifaToIso = new HashMap<>();
        fifaToIso.put("MAR", "ma");  // Maroc
        fifaToIso.put("ESP", "es");  // Espagne
        fifaToIso.put("POR", "pt");  // Portugal
        fifaToIso.put("ARG", "ar");  // Argentine
        fifaToIso.put("FRA", "fr");  // France
        fifaToIso.put("BRA", "br");  // Brésil
        fifaToIso.put("GER", "de");  // Allemagne
        fifaToIso.put("JPN", "jp");  // Japon
        fifaToIso.put("ENG", "gb");  // Angleterre (UK)
        fifaToIso.put("NED", "nl");  // Pays-Bas
        fifaToIso.put("BEL", "be");  // Belgique
        fifaToIso.put("USA", "us");  // États-Unis
        fifaToIso.put("ITA", "it");  // Italie
        fifaToIso.put("CRO", "hr");  // Croatie
        fifaToIso.put("SEN", "sn");  // Sénégal
        fifaToIso.put("GHA", "gh");  // Ghana
        fifaToIso.put("KOR", "kr");  // Corée du Sud
        fifaToIso.put("AUS", "au");  // Australie
        fifaToIso.put("MEX", "mx");  // Mexique
        fifaToIso.put("URU", "uy");  // Uruguay
        fifaToIso.put("SUI", "ch");  // Suisse
        fifaToIso.put("DEN", "dk");  // Danemark
        fifaToIso.put("TUN", "tn");  // Tunisie
        fifaToIso.put("POL", "pl");  // Pologne
        fifaToIso.put("ECU", "ec");  // Équateur
        fifaToIso.put("WAL", "gb-wls");  // Pays de Galles
        fifaToIso.put("IRN", "ir");  // Iran
        fifaToIso.put("QAT", "qa");  // Qatar
        fifaToIso.put("CAN", "ca");  // Canada
        fifaToIso.put("CMR", "cm");  // Cameroun
        fifaToIso.put("CRC", "cr");  // Costa Rica
        fifaToIso.put("SRB", "rs");  // Serbie
        
        return fifaToIso.getOrDefault(fifaCode.toUpperCase(), fifaCode.toLowerCase().substring(0, 2));
    }
    
    @FXML
    private void handleRetourMatchs() {
        showMatchs();
    }
    
    private void updatePrixTotal() {
        if (lblPrixTotal == null || selectedMatch == null) return;
        
        Zone zone = tableZones.getSelectionModel().getSelectedItem();
        CategorieTicket categorie = cmbCategorie.getValue();
        int quantite = spinnerQuantite.getValue();
        
        if (zone != null && categorie != null) {
            // Calculer le prix directement sans passer par zone.getMatch() (évite LazyInitializationException)
            double prixBase = selectedMatch.getPrixBase() != null ? selectedMatch.getPrixBase() : 100.0;
            Double coefficient = zone.getCoefficientPrix();
            double prixZone = prixBase * (coefficient != null ? coefficient : 1.0);
            double prixUnitaire = ticketService.calculerPrix(prixZone, categorie, selectedMatch.getPhase());
            double prixTotal = prixUnitaire * quantite;
            lblPrixTotal.setText(formatMontant(prixTotal));
        } else {
            lblPrixTotal.setText("--");
        }
    }
    
    // ===== ACHAT DE TICKETS =====
    
    @FXML
    private void handleAcheterTicket() {
        Zone zone = tableZones.getSelectionModel().getSelectedItem();
        CategorieTicket categorie = cmbCategorie.getValue();
        int quantite = spinnerQuantite.getValue();
        
        if (zone == null) {
            showWarning("Zone requise", "Veuillez sélectionner une zone.");
            return;
        }
        
        if (zone.getPlacesDisponibles() < quantite) {
            showWarning("Places insuffisantes", 
                "Il n'y a que " + zone.getPlacesDisponibles() + " place(s) disponible(s) dans cette zone.");
            return;
        }
        
        // Confirmation d'achat - calculer le prix directement sans passer par zone.getMatch()
        double prixBase = selectedMatch.getPrixBase() != null ? selectedMatch.getPrixBase() : 100.0;
        Double coefficient = zone.getCoefficientPrix();
        double prixZone = prixBase * (coefficient != null ? coefficient : 1.0);
        double prixUnitaire = ticketService.calculerPrix(prixZone, categorie, selectedMatch.getPhase());
        double prixTotal = prixUnitaire * quantite;
        
        Optional<ButtonType> result = showConfirmation("Confirmer l'achat",
            String.format("Voulez-vous acheter %d ticket(s) pour un total de %s ?", 
                quantite, formatMontant(prixTotal)));
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Spectateur spectateur = (Spectateur) currentUser;
            
            List<Ticket> tickets = ticketService.acheterTickets(
                spectateur, selectedMatch, zone, categorie, quantite);
            
            if (!tickets.isEmpty()) {
                showSuccess("Achat réussi", 
                    String.format("%d ticket(s) acheté(s) avec succès !", tickets.size()));
                refreshMesTickets();
                showDetailMatch(selectedMatch); // Rafraîchir les disponibilités
            } else {
                showError("Erreur", "L'achat a échoué. Veuillez réessayer.");
            }
        }
    }
    
    // ===== GESTION DES TICKETS =====
    
    private void showTicketDetails(Ticket ticket) {
        // Générer et afficher le QR code
        if (imgQRCode != null) {
            String qrContent = "MONDIAL2030|" + ticket.getCodeQR() + "|" + 
                ticket.getMatch().getId() + "|" + ticket.getCategorie();
            javafx.scene.image.Image qrImage = com.mondial2030.util.QRCodeGenerator.generateQRCodeImage(qrContent, 180, 180);
            if (qrImage != null) {
                imgQRCode.setImage(qrImage);
            }
        }
        
        // Afficher le code du ticket
        if (lblTicketCode != null) {
            String code = ticket.getCodeQR();
            // Tronquer si trop long
            if (code != null && code.length() > 20) {
                code = code.substring(0, 8) + "..." + code.substring(code.length() - 8);
            }
            lblTicketCode.setText(code);
        }
        
        // Afficher les équipes
        if (lblTicketEquipes != null) {
            lblTicketEquipes.setText(ticket.getMatch().getEquipeDomicile().getNom() + 
                " vs " + ticket.getMatch().getEquipeExterieur().getNom());
        }
        
        // Afficher la date
        if (lblTicketDate != null) {
            lblTicketDate.setText(formatDate(ticket.getMatch().getDateHeure()));
        }
        
        // Afficher le stade
        if (lblTicketStade != null) {
            lblTicketStade.setText(ticket.getMatch().getStade());
        }
        
        // Afficher la zone
        if (lblTicketZone != null) {
            lblTicketZone.setText(ticket.getZone() != null ? ticket.getZone().getNom() : "N/A");
        }
        
        // Afficher la catégorie
        if (lblTicketCategorie != null) {
            lblTicketCategorie.setText(ticket.getCategorie().name());
        }
        
        // Afficher le prix
        if (lblTicketPrix != null) {
            lblTicketPrix.setText(formatMontant(ticket.getPrix()));
        }
        
        // Afficher le statut avec couleur
        if (lblTicketStatut != null) {
            lblTicketStatut.setText(ticket.getStatut().name());
            lblTicketStatut.getStyleClass().removeAll("status-valide", "status-reserve", "status-annule");
            switch (ticket.getStatut()) {
                case VALIDE -> lblTicketStatut.getStyleClass().add("status-valide");
                case RESERVE -> lblTicketStatut.getStyleClass().add("status-reserve");
                case ANNULE, EXPIRE -> lblTicketStatut.getStyleClass().add("status-annule");
                default -> {}
            }
        }
        
        // Ancien label (pour compatibilité)
        if (lblTicketDetails != null) {
            lblTicketDetails.setText(ticket.getCodeQR());
        }
    }
    
    @FXML
    private void handleTransfererTicket() {
        Ticket ticket = tableMesTickets.getSelectionModel().getSelectedItem();
        if (ticket == null) {
            showWarning("Sélection requise", "Veuillez sélectionner un ticket à transférer.");
            return;
        }
        
        if (!ticket.isTransferable()) {
            showWarning("Transfert impossible", 
                "Ce ticket ne peut pas être transféré (statut: " + ticket.getStatut() + ").");
            return;
        }
        
        // Demander l'email du destinataire
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Transférer le ticket");
        dialog.setHeaderText("Ticket: " + ticket.getCodeQR());
        dialog.setContentText("Email du destinataire:");
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(email -> {
            boolean success = ticketService.transfererTicket(ticket.getId(), email);
            if (success) {
                showSuccess("Transfert réussi", "Le ticket a été transféré à " + email);
                refreshMesTickets();
            } else {
                showError("Erreur", "Le transfert a échoué. Vérifiez l'email du destinataire.");
            }
        });
    }
    
    @FXML
    private void handleAnnulerTicket() {
        Ticket ticket = tableMesTickets.getSelectionModel().getSelectedItem();
        if (ticket == null) {
            showWarning("Sélection requise", "Veuillez sélectionner un ticket à annuler.");
            return;
        }
        
        if (ticket.getStatut() != StatutTicket.VALIDE) {
            showWarning("Annulation impossible", 
                "Seuls les tickets valides peuvent être annulés.");
            return;
        }
        
        Optional<ButtonType> result = showConfirmation("Confirmer l'annulation",
            "Êtes-vous sûr de vouloir annuler ce ticket ?\nVous serez remboursé du montant: " + 
            formatMontant(ticket.getPrix()));
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = ticketService.annulerTicket(ticket.getId());
            if (success) {
                showSuccess("Annulation réussie", "Le ticket a été annulé et remboursé.");
                refreshMesTickets();
                refreshHistorique();
            } else {
                showError("Erreur", "L'annulation a échoué.");
            }
        }
    }
    
    @FXML
    private void handleDownloadTicketPdf() {
        Ticket ticket = tableMesTickets.getSelectionModel().getSelectedItem();
        if (ticket == null) {
            showWarning("Sélection requise", "Veuillez sélectionner un ticket à télécharger.");
            return;
        }
        
        // Ouvrir un dialogue pour choisir l'emplacement
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Enregistrer le ticket PDF");
        fileChooser.setInitialFileName("ticket_" + ticket.getCodeQR().substring(0, 8) + ".pdf");
        fileChooser.getExtensionFilters().add(
            new javafx.stage.FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf")
        );
        
        java.io.File file = fileChooser.showSaveDialog(tableMesTickets.getScene().getWindow());
        if (file != null) {
            try {
                generateTicketPdf(ticket, file.getAbsolutePath());
                showSuccess("Téléchargement réussi", 
                    "Le ticket a été enregistré dans:\n" + file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                showError("Erreur", "Impossible de générer le PDF: " + e.getMessage());
            }
        }
    }
    
    /**
     * Génère un PDF pour le ticket
     */
    private void generateTicketPdf(Ticket ticket, String filePath) throws Exception {
        com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(
            new com.itextpdf.kernel.pdf.PdfWriter(filePath)
        );
        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDoc, 
            com.itextpdf.kernel.geom.PageSize.A5);
        
        // Couleurs
        com.itextpdf.kernel.colors.Color headerColor = new com.itextpdf.kernel.colors.DeviceRgb(30, 58, 95);
        com.itextpdf.kernel.colors.Color accentColor = new com.itextpdf.kernel.colors.DeviceRgb(237, 137, 54);
        
        // En-tête avec fond coloré
        com.itextpdf.layout.element.Table headerTable = new com.itextpdf.layout.element.Table(1);
        headerTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));
        com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell()
            .setBackgroundColor(headerColor)
            .setPadding(15)
            .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER);
        
        headerCell.add(new com.itextpdf.layout.element.Paragraph("🏆 MONDIAL 2030")
            .setFontSize(24)
            .setFontColor(com.itextpdf.kernel.colors.ColorConstants.WHITE)
            .setBold()
            .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
        headerCell.add(new com.itextpdf.layout.element.Paragraph("E-TICKET OFFICIEL")
            .setFontSize(12)
            .setFontColor(new com.itextpdf.kernel.colors.DeviceRgb(184, 201, 217))
            .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
        headerTable.addCell(headerCell);
        document.add(headerTable);
        
        document.add(new com.itextpdf.layout.element.Paragraph("\n"));
        
        // QR Code
        String qrContent = "MONDIAL2030|" + ticket.getCodeQR() + "|" + 
            ticket.getMatch().getId() + "|" + ticket.getCategorie();
        java.awt.image.BufferedImage qrBuffered = generateQRCodeBufferedImage(qrContent, 150, 150);
        if (qrBuffered != null) {
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            javax.imageio.ImageIO.write(qrBuffered, "PNG", baos);
            com.itextpdf.io.image.ImageData imageData = com.itextpdf.io.image.ImageDataFactory.create(baos.toByteArray());
            com.itextpdf.layout.element.Image qrImage = new com.itextpdf.layout.element.Image(imageData);
            qrImage.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);
            document.add(qrImage);
        }
        
        // Code du ticket
        document.add(new com.itextpdf.layout.element.Paragraph(ticket.getCodeQR())
            .setFontSize(8)
            .setFontColor(com.itextpdf.kernel.colors.ColorConstants.GRAY)
            .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
        
        document.add(new com.itextpdf.layout.element.Paragraph("\n"));
        
        // Ligne de séparation
        document.add(new com.itextpdf.layout.element.Paragraph("✂ - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ✂")
            .setFontSize(10)
            .setFontColor(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY)
            .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
        
        document.add(new com.itextpdf.layout.element.Paragraph("\n"));
        
        // Match title
        String matchTitle = ticket.getMatch().getEquipeDomicile().getNom() + 
            " vs " + ticket.getMatch().getEquipeExterieur().getNom();
        document.add(new com.itextpdf.layout.element.Paragraph(matchTitle)
            .setFontSize(18)
            .setBold()
            .setFontColor(headerColor)
            .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
        
        // Phase et groupe
        String phaseGroupe = ticket.getMatch().getPhase().name().replace("_", " ");
        if (ticket.getMatch().getGroupe() != null) {
            phaseGroupe += " - Groupe " + ticket.getMatch().getGroupe();
        }
        document.add(new com.itextpdf.layout.element.Paragraph(phaseGroupe)
            .setFontSize(11)
            .setFontColor(com.itextpdf.kernel.colors.ColorConstants.GRAY)
            .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
        
        document.add(new com.itextpdf.layout.element.Paragraph("\n"));
        
        // Détails dans un tableau
        com.itextpdf.layout.element.Table detailsTable = new com.itextpdf.layout.element.Table(2);
        detailsTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));
        
        addDetailRow(detailsTable, "📅 Date", formatDate(ticket.getMatch().getDateHeure()));
        addDetailRow(detailsTable, "🏟️ Stade", ticket.getMatch().getStade());
        addDetailRow(detailsTable, "📍 Ville", ticket.getMatch().getVille() + ", " + ticket.getMatch().getPays());
        addDetailRow(detailsTable, "📌 Zone", ticket.getZone() != null ? ticket.getZone().getNom() : "N/A");
        addDetailRow(detailsTable, "🎫 Catégorie", ticket.getCategorie().name());
        
        document.add(detailsTable);
        
        document.add(new com.itextpdf.layout.element.Paragraph("\n"));
        
        // Prix
        document.add(new com.itextpdf.layout.element.Paragraph("Prix: " + formatMontant(ticket.getPrix()))
            .setFontSize(16)
            .setBold()
            .setFontColor(accentColor)
            .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
        
        // Statut
        String statutText = "Statut: " + ticket.getStatut().name();
        com.itextpdf.kernel.colors.Color statutColor = ticket.getStatut() == StatutTicket.VALIDE ? 
            new com.itextpdf.kernel.colors.DeviceRgb(39, 103, 73) : 
            new com.itextpdf.kernel.colors.DeviceRgb(192, 86, 33);
        document.add(new com.itextpdf.layout.element.Paragraph(statutText)
            .setFontSize(12)
            .setBold()
            .setFontColor(statutColor)
            .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
        
        document.add(new com.itextpdf.layout.element.Paragraph("\n\n"));
        
        // Footer
        com.itextpdf.layout.element.Table footerTable = new com.itextpdf.layout.element.Table(1);
        footerTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));
        com.itextpdf.layout.element.Cell footerCell = new com.itextpdf.layout.element.Cell()
            .setBackgroundColor(headerColor)
            .setPadding(10)
            .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER);
        footerCell.add(new com.itextpdf.layout.element.Paragraph("Présentez ce QR code à l'entrée du stade")
            .setFontSize(10)
            .setFontColor(new com.itextpdf.kernel.colors.DeviceRgb(184, 201, 217))
            .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
        footerTable.addCell(footerCell);
        document.add(footerTable);
        
        document.close();
    }
    
    /**
     * Ajoute une ligne de détail au tableau
     */
    private void addDetailRow(com.itextpdf.layout.element.Table table, String label, String value) {
        com.itextpdf.layout.element.Cell labelCell = new com.itextpdf.layout.element.Cell()
            .add(new com.itextpdf.layout.element.Paragraph(label).setFontSize(11).setBold())
            .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
            .setPadding(5);
        com.itextpdf.layout.element.Cell valueCell = new com.itextpdf.layout.element.Cell()
            .add(new com.itextpdf.layout.element.Paragraph(value).setFontSize(11))
            .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER)
            .setPadding(5);
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
    
    /**
     * Génère un QR code en BufferedImage pour le PDF
     */
    private java.awt.image.BufferedImage generateQRCodeBufferedImage(String text, int width, int height) {
        try {
            com.google.zxing.qrcode.QRCodeWriter qrCodeWriter = new com.google.zxing.qrcode.QRCodeWriter();
            com.google.zxing.common.BitMatrix bitMatrix = qrCodeWriter.encode(text, 
                com.google.zxing.BarcodeFormat.QR_CODE, width, height);
            return com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // ===== FILTRES =====
    
    @FXML
    private void handleFilterMatchs() {
        String search = txtSearchMatch != null ? txtSearchMatch.getText().trim().toLowerCase() : "";
        PhaseMatch phase = cmbFilterPhase != null ? cmbFilterPhase.getValue() : null;
        LocalDate date = dpFilterDate != null ? dpFilterDate.getValue() : null;
        
        List<Match> filtered = matchService.getMatchsAVenir().stream()
            .filter(m -> {
                boolean matchSearch = search.isEmpty() ||
                    m.getEquipeDomicile().getNom().toLowerCase().contains(search) ||
                    m.getEquipeExterieur().getNom().toLowerCase().contains(search) ||
                    m.getStade().toLowerCase().contains(search) ||
                    m.getVille().toLowerCase().contains(search);
                    
                boolean matchPhase = phase == null || m.getPhase() == phase;
                
                boolean matchDate = date == null || 
                    m.getDateHeure().toLocalDate().equals(date);
                
                return matchSearch && matchPhase && matchDate;
            })
            .collect(Collectors.toList());
        
        matchsList.clear();
        matchsList.addAll(filtered);
    }
    
    @FXML
    private void handleResetFilters() {
        if (txtSearchMatch != null) txtSearchMatch.clear();
        if (cmbFilterPhase != null) cmbFilterPhase.setValue(null);
        if (dpFilterDate != null) dpFilterDate.setValue(null);
        refreshMatchs();
    }
    
    // ===== PROFIL =====
    
    private void loadProfilData() {
        if (currentUser == null) return;
        
        if (txtProfilNom != null) txtProfilNom.setText(currentUser.getNom());
        if (txtProfilPrenom != null) txtProfilPrenom.setText(currentUser.getPrenom());
        if (txtProfilEmail != null) txtProfilEmail.setText(currentUser.getEmail());
        if (txtProfilTelephone != null) txtProfilTelephone.setText(currentUser.getTelephone());
    }
    
    @FXML
    private void handleSauvegarderProfil() {
        String nom = txtProfilNom.getText().trim();
        String prenom = txtProfilPrenom.getText().trim();
        String telephone = txtProfilTelephone.getText().trim();
        
        if (nom.isEmpty() || prenom.isEmpty()) {
            showWarning("Champs requis", "Le nom et le prénom sont obligatoires.");
            return;
        }
        
        currentUser.setNom(nom);
        currentUser.setPrenom(prenom);
        currentUser.setTelephone(telephone);
        
        // Sauvegarder via le service
        AuthenticationService.getInstance().mettreAJourProfil(currentUser);
        
        showSuccess("Profil mis à jour", "Vos informations ont été sauvegardées.");
        
        // Mettre à jour l'affichage
        if (lblUserName != null) {
            lblUserName.setText(nom + " " + prenom);
        }
    }
    
    @FXML
    private void handleChangerMotDePasse() {
        String oldPwd = txtOldPassword.getText();
        String newPwd = txtNewPassword.getText();
        String confirmPwd = txtConfirmPassword.getText();
        
        if (oldPwd.isEmpty() || newPwd.isEmpty() || confirmPwd.isEmpty()) {
            showWarning("Champs requis", "Tous les champs de mot de passe sont obligatoires.");
            return;
        }
        
        if (newPwd.length() < 8) {
            showWarning("Mot de passe faible", "Le nouveau mot de passe doit contenir au moins 8 caractères.");
            return;
        }
        
        if (!newPwd.equals(confirmPwd)) {
            showWarning("Confirmation incorrecte", "Les mots de passe ne correspondent pas.");
            return;
        }
        
        boolean success = AuthenticationService.getInstance()
            .changerMotDePasse(currentUser, oldPwd, newPwd);
        
        if (success) {
            showSuccess("Mot de passe changé", "Votre mot de passe a été modifié avec succès.");
            txtOldPassword.clear();
            txtNewPassword.clear();
            txtConfirmPassword.clear();
        } else {
            showError("Erreur", "L'ancien mot de passe est incorrect.");
        }
    }
    
    // ===== DÉCONNEXION =====
    
    @FXML
    private void handleDeconnexionSpectateur(ActionEvent event) {
        handleDeconnexion(event);
    }
}
