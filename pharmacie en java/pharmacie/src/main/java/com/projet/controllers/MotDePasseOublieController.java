package com.projet.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MotDePasseOublieController {

    @FXML
    private AnchorPane anchorLogin;
    @FXML
    private Button btnSeConnecter;
    @FXML
    private PasswordField passwordConfirmerNewMdp;
    @FXML
    private PasswordField passwordNewMdp;
    @FXML
    private TextField textfieldEmail;
    @FXML
    private TextField textfieldUser;
    @FXML
    private Label labelMessage;



//=================================================================================================================================
    

    @FXML
    private void initialize() {
    }

//=================================================================================================================================

    @FXML
    public void btnClose() {
        System.exit(0);
    }

//================================================================================================================================


//================================================================================================================================

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }


//=================================================================================================================================
    @FXML
    private void VerificationChamps() {
        String email = textfieldEmail.getText().trim();
        String user = textfieldUser.getText().trim();
        String newMdp = passwordNewMdp.getText().trim();
        String comfirmeMdp = passwordConfirmerNewMdp.getText().trim();

        if(email.isEmpty()||!isValidEmail(email)) {
            labelMessage.setText("Votre email est invalide!");
            return;
        }
        if(user.isEmpty()) {
            labelMessage.setText("Nom d'utilisateur vide !");
            return;
        }
        if(newMdp.isEmpty()) {
            labelMessage.setText("Nouveau mot de passe  vide !");
            return;
        }
        if(comfirmeMdp.isEmpty()) {
            labelMessage.setText("Confirmer votre mot de passe !");
            return;
        }
        if (!newMdp.equals(comfirmeMdp)) {
            labelMessage.setText("Les mots de passe ne correspondent pas !");
            return;
        }
        
        redirigerVersLogin();
    }
    

//=================================================================================================================================
    @FXML
    private void redirigerVersLogin() {
        try {
            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) textfieldEmail.getScene().getWindow();
            currentStage.close();
            // Charger le fxml du login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
            Scene scene = new Scene(loader.load());
            // Récupérer le contrôleur du login
            LoginController loginController = loader.getController();
            // Pré-remplir les champs avec le nom d'utilisateur et le nouveau mot de passe
            loginController.remplirChamps(textfieldUser.getText(), passwordNewMdp.getText());
            // Afficher la fenêtre login
            Stage stage = new Stage();
            stage.initStyle(javafx.stage.StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
