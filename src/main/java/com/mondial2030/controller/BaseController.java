package com.mondial2030.controller;

import com.mondial2030.entity.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

/**
 * Contrôleur de base fournissant des fonctionnalités communes.
 * Tous les autres contrôleurs doivent hériter de cette classe.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public abstract class BaseController {
    
    protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);
    protected Utilisateur currentUser;
    
    /**
     * Définit l'utilisateur courant
     */
    public void setCurrentUser(Utilisateur user) {
        this.currentUser = user;
        onUserSet();
    }
    
    /**
     * Méthode appelée après la définition de l'utilisateur.
     * À surcharger dans les sous-classes si nécessaire.
     */
    protected void onUserSet() {
        // Par défaut, ne fait rien
    }
    
    /**
     * Récupère l'utilisateur courant
     */
    public Utilisateur getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Navigue vers une autre vue
     */
    protected void navigateTo(String fxmlPath, String title, javafx.scene.Node sourceNode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            // Passer l'utilisateur au nouveau contrôleur
            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setCurrentUser(currentUser);
            }
            
            Stage stage = (Stage) sourceNode.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setTitle(title + " - Mondial 2030");
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            logger.error("Erreur lors de la navigation vers: {}", fxmlPath, e);
            showError("Erreur de navigation", "Impossible de charger la vue demandée.");
        }
    }
    
    /**
     * Déconnecte l'utilisateur et retourne à l'écran de connexion
     */
    @FXML
    protected void handleDeconnexion(javafx.event.ActionEvent event) {
        Optional<ButtonType> result = showConfirmation(
                "Déconnexion",
                "Êtes-vous sûr de vouloir vous déconnecter ?"
        );
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            logger.info("Déconnexion de l'utilisateur: {}", currentUser.getEmail());
            currentUser = null;
            
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
                Parent root = loader.load();
                
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
                stage.setTitle("Connexion - Mondial 2030");
                stage.setScene(scene);
                stage.setMaximized(false);
                stage.setWidth(800);
                stage.setHeight(600);
                stage.centerOnScreen();
                stage.show();
                
            } catch (IOException e) {
                logger.error("Erreur lors de la déconnexion", e);
            }
        }
    }
    
    /**
     * Affiche une boîte de dialogue d'information
     */
    protected void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Affiche une boîte de dialogue d'erreur
     */
    protected void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Affiche une boîte de dialogue d'avertissement
     */
    protected void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Affiche une boîte de dialogue de confirmation
     */
    protected Optional<ButtonType> showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait();
    }
    
    /**
     * Affiche une boîte de dialogue de succès
     */
    protected void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Succès");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Formate un montant en devise
     */
    protected String formatMontant(Double montant) {
        if (montant == null) return "0,00 €";
        return String.format("%.2f €", montant);
    }
    
    /**
     * Formate une date pour l'affichage
     */
    protected String formatDate(java.time.LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    
    /**
     * Formate une date sans l'heure
     */
    protected String formatDateOnly(java.time.LocalDate date) {
        if (date == null) return "";
        return date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
