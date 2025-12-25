package com.projet.controllers;


import java.io.IOException;
import java.sql.SQLException;

import com.projet.models.Produit;
import com.projet.models.Vente;
import com.projet.services.DBConnection;
import com.projet.services.ProduitService;
import com.projet.services.VenteService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class TableauBordController {
    @FXML
    private Label labelVenteTotal;
    @FXML
    private AnchorPane anchorTableauBord;
    @FXML

    private Label labelChiffreDaffaires;
    @FXML
    private Label labelCommandeEnAttente;
    @FXML
    private Label labelStockProduit;
    @FXML
    private Label labelVenteDuJour;
    @FXML
    private TableView<Produit> tableauAlerteStock;
    @FXML
    private TableColumn<Produit, String> tableauAlertesStockColonneProduit;
    @FXML
    private TableColumn<Produit, Integer> tableauAlertesStockColonneQuantite;
    @FXML
    private TableColumn<Produit, String> tableauAlertesStockColonneReference;
    @FXML
    private TableColumn<Produit, String> tableauAlertesStockColonneType;


    @FXML
    private TableView<Vente> tableauVenteRecente;
    @FXML
    private TableColumn<Vente, Integer> tableauVentesRecentesColonneQuantité;
    @FXML
    private TableColumn<Vente, String> tableauVentesRecentesColonneReference;
    @FXML
    private TableColumn<Vente, String> tableauVentesRecentesColonneType;
    
    private ObservableList<Produit> listeStockFaible ;
    private ProduitService produitService;
    private ObservableList<Vente> listeVentes ;
    private VenteService venteService;


    @FXML
    private void initialize() {
        chargerVente();
        chagerStockFaible();
        chargerLabelVenteJour();
        chargerLabelVenteTotal();
        chargerLabelChiffreAffaireJour();
        chargerStockProduit();
    }
    
    private void chargerStockProduit() {
        int total = produitService.compteurStockProduit();
        labelStockProduit.setText(String.valueOf(total));
    }

    @FXML
    private void chagerStockFaible(){
        try {
            produitService = new ProduitService(DBConnection.getConnection());
            listeStockFaible = FXCollections.observableArrayList(produitService.listesStockFaible());
            tableauAlertesStockColonneProduit.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
            tableauAlertesStockColonneReference.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getReference()));
            tableauAlertesStockColonneQuantite.setCellValueFactory(c-> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getQuantite()).asObject());
            tableauAlertesStockColonneType.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));
            tableauAlerteStock.setItems(listeStockFaible);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void chargerVente() {
        try {
            venteService = new VenteService(DBConnection.getConnection());
            listeVentes = FXCollections.observableArrayList(venteService.listesVentesDuJour());
            tableauVentesRecentesColonneReference.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getcodeVente()));
            tableauVentesRecentesColonneType.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getTypeVente()));
            tableauVentesRecentesColonneQuantité.setCellValueFactory(c-> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getQuantiteTotal()).asObject());
            tableauVenteRecente.setItems(listeVentes);
            System.out.println("Charger");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chargerLabelVenteJour(){
        try {
            int venteDuJour = venteService.compteurVenteJour();
            labelVenteDuJour.setText(String.valueOf(venteDuJour));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void chargerLabelVenteTotal(){
        try {
            int venteTotal = venteService.compteurVenteTotal();
            labelVenteTotal.setText(String.valueOf(venteTotal));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void chargerLabelChiffreAffaireJour(){
        try {
            double sommeTotalJour = venteService.compteurChiffreAffaireTotalVentesDuJour();
            labelChiffreDaffaires.setText(String.valueOf(sommeTotalJour));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }







    @FXML
    private void ouvrirStockProduit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Produit.fxml"));
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


    @FXML
    private void ouvrirVente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/VoirVente.fxml"));
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