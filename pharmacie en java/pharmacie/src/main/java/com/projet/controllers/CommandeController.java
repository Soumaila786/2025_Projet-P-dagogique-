package com.projet.controllers;


import java.io.IOException;
import java.security.Timestamp;

import com.projet.models.Commande;
import com.projet.services.CommandeService;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CommandeController {
    @FXML
    private TableColumn tableauCommandesColStatus;

    @FXML
    private TableView<Commande> tableauCommandes;
    @FXML
    private AnchorPane anchorCommandes;
    @FXML
    private TableColumn<Commande, String> tableauCommandesColFournisseur;
    @FXML
    private TableColumn<Commande, Timestamp> tableauCommandesColDate;
    @FXML
    private TableColumn<Commande, Integer> tableauCommandesColQuantite;
    @FXML
    private TableColumn<Commande, String> tableauCommandesColReference;
    @FXML
    private TextField textfieldCommandesRechercher;

    private ObservableList<Commande> listeCommandes;
    private CommandeService commandeService;

    @FXML
    private void initialize() {
        
    }

    @FXML
    private void ouvrirPasserCommande() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PasserCommande.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(null);
            stage.setResizable(false);
            stage.getIcons().clear();
            root.getStylesheets().add(getClass().getResource("/css/home.css").toExternalForm());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fichier FXML introuvable !");
        }
    }
}

