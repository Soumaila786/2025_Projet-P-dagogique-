package com.projet.controllers;

import java.io.IOException;
import java.sql.Timestamp;

import com.projet.models.Vente;
import com.projet.services.DBConnection;
import com.projet.services.VenteService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class VoirVenteController {

    @FXML
    private TableColumn<Vente ,String> tableauVoirVenteColTypeVente;
    @FXML
    private AnchorPane anchorVoirVentes;
    @FXML
    private Button btnVoirVenteNouveauVente;
    @FXML
    private TableView<Vente> tableauVoirVente;
    @FXML
    private TableColumn<Vente, Timestamp> tableauVoirVenteColDate;
    @FXML
    private TableColumn<Vente, Double> tableauVoirVenteColMontant;
    @FXML
    private TableColumn<Vente, String> tableauVoirVenteColNomClient;
    @FXML
    private TableColumn<Vente, String> tableauVoirVenteColReference;
    @FXML
    private TextField textfieldVoirVentesRechercher;

    private ObservableList<Vente> listeVentes ;
    private VenteService venteService;


    @FXML
    private void initialize() {
        chargerVente();
    }


    private void chargerVente() {
        try {
            venteService = new VenteService(DBConnection.getConnection());
            listeVentes = FXCollections.observableArrayList(venteService.listesTousVentes());
            // Supposons que getDate() retourne un java.sql.Timestamp
            tableauVoirVenteColDate.setCellValueFactory(c ->new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getDate()));
            tableauVoirVenteColMontant.setCellValueFactory( c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getMontant()).asObject());
            tableauVoirVenteColReference.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getcodeVente()));
            tableauVoirVenteColTypeVente.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getTypeVente()));
            tableauVoirVente.setItems(listeVentes);
             // Double-clic sur une ligne
            tableauVoirVente.setRowFactory(tv -> {
                TableRow<Vente> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (!row.isEmpty() && event.getClickCount() == 2 ) {
                        Vente vente = row.getItem();
                        ouvrirDetailsVente(vente.getId());

                    }
                });
                return row;
        });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void ouvrirVenteLibre() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/VenteLibre.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(null);
            stage.setResizable(false);
            // Mettre icône transparente pour “supprimer” le logo
            stage.getIcons().clear();
            root.getStylesheets().add(getClass().getResource("/css/home.css").toExternalForm());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fichier FXML introuvable !");
        }
    }

    @FXML
    private void ouvrirVenteOrdonnance() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/VenteOrdonnance.fxml"));
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

    private void ouvrirDetailsVente(int venteId) {
    try {
        Vente vente = venteService.obtenirVenteAvecDetails(venteId);
        System.out.println("Double clic sur : " + vente.getcodeVente());

        if (vente != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DetailsVenteLibre.fxml"));
            Parent root = loader.load();

            DetailsVenteLibreController controller = loader.getController();
            controller.setVente(vente);

            root.getStylesheets().add(getClass().getResource("/css/home.css").toExternalForm());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails Vente");
            stage.show();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}




