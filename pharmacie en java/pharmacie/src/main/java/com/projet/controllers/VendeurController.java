package com.projet.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class VendeurController {
    @FXML
    private HBox menuVentes;

    @FXML
    private StackPane vendeurPrincipal;
    @FXML
    private HBox menuClient;
    @FXML
    private HBox menuDeconnexion;
    @FXML
    private HBox menuParametre;
    @FXML
    private HBox menuStatistique;
    @FXML
    private HBox menuTableauBord;
    @FXML
    private HBox menuVoirVentes;
    @FXML
    private StackPane vendeurCenter;

//===============================================================================================================

    @FXML
    private void messageAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

//===============================================================================================================

    @FXML
    public void btnClose() {
        System.exit(0);
    }
//===============================================================================================================
    @FXML
    public void initialize() {

        loadPage("TableauBord.fxml");
        menuClient.setOnMouseClicked(event -> loadPage("Client.fxml"));
        menuVoirVentes.setOnMouseClicked(event -> loadPage("VoirVente.fxml"));
        menuStatistique.setOnMouseClicked(event -> loadPage("Statistique.fxml"));
        menuParametre.setOnMouseClicked(event -> loadPage("Parametre.fxml"));
        menuTableauBord.setOnMouseClicked(event -> loadPage("TableauBord.fxml"));
        menuDeconnexion.setOnMouseClicked(event -> deconnexion(event));
    }

//===============================================================================================================

    private void loadPage(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + fxml));
            Parent root = loader.load();
            vendeurCenter.getChildren().clear();
            vendeurCenter.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//===============================================================================================================

private void deconnexion(MouseEvent event) {
    try {
        // Récupérer la stage à partir de l'événement
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        
    } catch (IOException e) {
        e.printStackTrace();
        messageAlert("Erreur","Erreur de chargement de la page de login");
    }
}


}
