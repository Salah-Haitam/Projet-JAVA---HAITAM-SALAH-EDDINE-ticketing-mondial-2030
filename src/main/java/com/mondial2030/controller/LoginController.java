package com.mondial2030.controller;

import com.mondial2030.entity.Utilisateur;
import com.mondial2030.entity.Administrateur;
import com.mondial2030.entity.Spectateur;
import com.mondial2030.service.AuthenticationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Contrôleur pour la vue de connexion et d'inscription.
 * Gère l'authentification des utilisateurs (Admin et Spectateur).
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class LoginController implements Initializable {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final AuthenticationService authService = AuthenticationService.getInstance();
    
    // Composants de connexion
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnConnexion;
    @FXML private Label lblError;
    @FXML private Hyperlink linkInscription;
    
    // Composants d'inscription
    @FXML private VBox inscriptionPane;
    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtEmailInscription;
    @FXML private PasswordField txtPasswordInscription;
    @FXML private PasswordField txtPasswordConfirm;
    @FXML private TextField txtTelephone;
    @FXML private Button btnInscrire;
    @FXML private Hyperlink linkConnexion;
    @FXML private Label lblErrorInscription;
    
    // Panneaux
    @FXML private VBox connexionPane;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Initialisation du contrôleur de connexion");
        
        // Cacher le message d'erreur au démarrage
        if (lblError != null) {
            lblError.setVisible(false);
        }
        if (lblErrorInscription != null) {
            lblErrorInscription.setVisible(false);
        }
        
        // Par défaut, afficher le formulaire de connexion
        if (inscriptionPane != null) {
            inscriptionPane.setVisible(false);
            inscriptionPane.setManaged(false);
        }
    }
    
    /**
     * Gère la connexion d'un utilisateur
     */
    @FXML
    private void handleConnexion(ActionEvent event) {
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
        
        // Validation des champs
        if (email.isEmpty() || password.isEmpty()) {
            showError(lblError, "Veuillez remplir tous les champs.");
            return;
        }
        
        if (!isValidEmail(email)) {
            showError(lblError, "Format d'email invalide.");
            return;
        }
        
        // Tentative de connexion
        Optional<Utilisateur> userOpt = authService.authentifier(email, password);
        
        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            logger.info("Connexion réussie pour: {}", user.getEmail());
            
            try {
                // Redirection selon le type d'utilisateur
                if (user instanceof Administrateur) {
                    navigateTo("/fxml/AdminDashboard.fxml", "Dashboard Administrateur", user);
                } else if (user instanceof Spectateur) {
                    navigateTo("/fxml/SpectateurDashboard.fxml", "Espace Spectateur", user);
                }
            } catch (IOException e) {
                logger.error("Erreur lors de la navigation", e);
                showError(lblError, "Erreur lors du chargement de l'interface.");
            }
        } else {
            showError(lblError, "Email ou mot de passe incorrect.");
        }
    }
    
    /**
     * Gère l'inscription d'un nouveau spectateur
     */
    @FXML
    private void handleInscription(ActionEvent event) {
        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String email = txtEmailInscription.getText().trim();
        String password = txtPasswordInscription.getText();
        String passwordConfirm = txtPasswordConfirm.getText();
        String telephone = txtTelephone.getText().trim();
        
        // Validation des champs
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError(lblErrorInscription, "Veuillez remplir tous les champs obligatoires.");
            return;
        }
        
        if (!isValidEmail(email)) {
            showError(lblErrorInscription, "Format d'email invalide.");
            return;
        }
        
        if (password.length() < 8) {
            showError(lblErrorInscription, "Le mot de passe doit contenir au moins 8 caractères.");
            return;
        }
        
        if (!password.equals(passwordConfirm)) {
            showError(lblErrorInscription, "Les mots de passe ne correspondent pas.");
            return;
        }
        
        // Tentative d'inscription
        System.out.println("=== DEBUG INSCRIPTION ===");
        System.out.println("Nom: " + nom + ", Prenom: " + prenom);
        System.out.println("Email: " + email);
        System.out.println("Password length: " + password.length());
        
        Spectateur spectateur = new Spectateur();
        spectateur.setNom(nom);
        spectateur.setPrenom(prenom);
        spectateur.setEmail(email);
        spectateur.setMotDePasse(password); // Le DAO s'occupe du hashage
        spectateur.setTelephone(telephone);
        spectateur.setActif(true);
        
        boolean inscriptionReussie = authService.inscrireSpectateur(spectateur);
        System.out.println("Inscription réussie: " + inscriptionReussie);
        System.out.println("=========================");
        
        if (inscriptionReussie) {
            logger.info("Inscription réussie pour: {}", email);
            
            // Afficher message de succès et revenir à la connexion
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inscription réussie");
            alert.setHeaderText(null);
            alert.setContentText("Votre compte a été créé avec succès. Vous pouvez maintenant vous connecter.");
            alert.showAndWait();
            
            showConnexionPane();
            
            // Pré-remplir l'email
            txtEmail.setText(email);
        } else {
            showError(lblErrorInscription, "Cet email est déjà utilisé.");
        }
    }
    
    /**
     * Affiche le formulaire d'inscription
     */
    @FXML
    private void showInscriptionPane() {
        connexionPane.setVisible(false);
        connexionPane.setManaged(false);
        inscriptionPane.setVisible(true);
        inscriptionPane.setManaged(true);
        clearFields();
    }
    
    /**
     * Affiche le formulaire de connexion
     */
    @FXML
    private void showConnexionPane() {
        inscriptionPane.setVisible(false);
        inscriptionPane.setManaged(false);
        connexionPane.setVisible(true);
        connexionPane.setManaged(true);
        clearFields();
    }
    
    /**
     * Navigue vers une nouvelle vue
     */
    private void navigateTo(String fxmlPath, String title, Utilisateur user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        
        // Passer l'utilisateur au contrôleur
        Object controller = loader.getController();
        if (controller instanceof BaseController) {
            ((BaseController) controller).setCurrentUser(user);
        }
        
        Stage stage = (Stage) btnConnexion.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setTitle(title + " - Mondial 2030");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
    
    /**
     * Affiche un message d'erreur
     */
    private void showError(Label label, String message) {
        if (label != null) {
            label.setText(message);
            label.setVisible(true);
            label.setManaged(true);
        }
    }
    
    /**
     * Valide le format d'un email
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
    
    /**
     * Efface tous les champs
     */
    private void clearFields() {
        if (txtEmail != null) txtEmail.clear();
        if (txtPassword != null) txtPassword.clear();
        if (txtNom != null) txtNom.clear();
        if (txtPrenom != null) txtPrenom.clear();
        if (txtEmailInscription != null) txtEmailInscription.clear();
        if (txtPasswordInscription != null) txtPasswordInscription.clear();
        if (txtPasswordConfirm != null) txtPasswordConfirm.clear();
        if (txtTelephone != null) txtTelephone.clear();
        if (lblError != null) lblError.setVisible(false);
        if (lblErrorInscription != null) lblErrorInscription.setVisible(false);
    }
}
