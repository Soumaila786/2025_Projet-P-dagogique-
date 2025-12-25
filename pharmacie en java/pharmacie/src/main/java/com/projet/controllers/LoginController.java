package com.projet.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.projet.models.Utilisateur;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import com.projet.services.UtilisateurService;

public class LoginController {

    @FXML
    private AnchorPane anchorLogin;
    @FXML
    private TextField textfieldLogin;
    @FXML
    private PasswordField passwordMotDePasse;
    @FXML
    private Button btnSeConnecter;
    @FXML
    private Hyperlink hyperlinkMotDePasse;

    private UtilisateurService utilisateur;

//=================================================================================================================================
    @FXML
    public void initialize() {
        try {
            String url = "jdbc:mysql://localhost:3306/pharmamanager";
            String user = "root";
            String password = "";
            Connection connexion = DriverManager.getConnection(url, user, password);
            utilisateur = new UtilisateurService(connexion); // Initialisation du service
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }
//=================================================================================================================================

    @FXML
    public void btnClose() {
        System.exit(0);
    }
//=================================================================================================================================

    private void messageAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.initStyle(javafx.stage.StageStyle.UNDECORATED);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


//=================================================================================================================================

    @FXML
    public void authentification() throws IOException {
        String login = textfieldLogin.getText();
        String mdp = passwordMotDePasse.getText();
        if (login.isEmpty() || mdp.isEmpty()) {
            messageAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }
        // Appel du service pour vérifier le login
        Utilisateur user = utilisateur.verifierLogin(login, mdp);
        if (user != null) {
            messageAlert("Succès", "Connexion réussie ! Bienvenue "+ user.getNom());
            System.out.println(" Connexion réussie ! Bienvenue " + user.getNom() + " (" + user.getRole() + ")");
            // Fermer la fenêtre de login
            btnSeConnecter.getScene().getWindow().hide();
            
            String vue = "";
            if ("gerant".equalsIgnoreCase(user.getRole())) {
                vue = "/views/Gerant.fxml";
            } else if ("vendeur".equalsIgnoreCase(user.getRole())) {
                vue = "/views/Vendeur.fxml";
            } else {
                messageAlert("Erreur", "Rôle non reconnu !");
                return;
            }

            // Charger la page correspondante
            Parent root = FXMLLoader.load(getClass().getResource(vue));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initStyle(javafx.stage.StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        } else {
            messageAlert("Erreur", "Nom d'utilisateur ou mot de passe incorrects");
        }
    }


//=================================================================================================================================


@FXML
private void ouvrirMotDePasseOublie() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MotDePasseOublie.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();

        stage.initStyle(javafx.stage.StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


//=================================================================================================================================


    public void remplirChamps(String login, String mdp) {
        textfieldLogin.setText(login);
        passwordMotDePasse.setText(mdp);
    }


}
