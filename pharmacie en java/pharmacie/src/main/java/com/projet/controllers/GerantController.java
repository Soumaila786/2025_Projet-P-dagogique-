package com.projet.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GerantController {
    @FXML
    private StackPane gerantPrincipal;
    @FXML
    private StackPane gerantCenter;
    @FXML
    private HBox menuClient;
    @FXML
    private HBox menuCommande;
    @FXML
    private HBox menuDeconnexion;
    @FXML
    private HBox menuFournisseur;
    @FXML
    private HBox menuParametre;
    @FXML
    private HBox menuProduit;
    @FXML
    private HBox menuUtilisateur;
    @FXML
    private HBox menuStatistique;
    @FXML
    private HBox menuTableauBord;
    @FXML
    private HBox menuVoirVente;

    public void btnClose() {
        System.exit(0);
    }

    @FXML
    private void reduire(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }


    @FXML
    public void initialize() {
        loadPage("TableauBord.fxml");
        menuClient.setOnMouseClicked(event -> loadPage("Client.fxml"));
        menuUtilisateur.setOnMouseClicked(event -> loadPage("Utilisateur.fxml"));
        menuProduit.setOnMouseClicked(event -> loadPage("Produit.fxml"));
        menuFournisseur.setOnMouseClicked(event -> loadPage("Fournisseur.fxml"));
        menuCommande.setOnMouseClicked(event -> loadPage("Commande.fxml"));
        menuVoirVente.setOnMouseClicked(event -> loadPage("VoirVente.fxml"));
        menuStatistique.setOnMouseClicked(event -> loadPage("Statistique.fxml"));
        menuParametre.setOnMouseClicked(event -> loadPage("Parametre.fxml"));
        menuTableauBord.setOnMouseClicked(event -> loadPage("TableauBord.fxml"));
        menuDeconnexion.setOnMouseClicked(event -> deconnexion());
        // Petite astuce : passer ce controller en "UserData" pour que les enfants puissent y accéder
        gerantPrincipal.setUserData(this);
    }

    @FXML
    public void loadPage(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + fxml));
            Parent root = loader.load();
            gerantCenter.getChildren().clear();
            gerantCenter.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deconnexion() {
        try {
            Stage stage = (Stage) gerantPrincipal.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.initStyle(javafx.stage.StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public static void messageAlert(String titre, String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(titre);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

