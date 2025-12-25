package com.projet.controllers;

import java.sql.SQLException;
import java.time.LocalDate;

import com.projet.models.Produit;
import com.projet.services.DBConnection;
import com.projet.services.ProduitService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ProduitController {
    @FXML
    private AnchorPane anchorProduit;
    @FXML
    private ComboBox<String> comboProduitChoisirType;
    @FXML
    private TableView<Produit> tableauProduit;
    @FXML
    private TableColumn<Produit, String> tableauProduitColonneDatePeremption;
    @FXML
    private TableColumn<Produit, Double> tableauProduitColonnePrix;
    @FXML
    private TableColumn<Produit, String> tableauProduitColonneProduit;
    @FXML
    private TableColumn<Produit, Integer> tableauProduitColonneQuantite;
    @FXML
    private TableColumn<Produit, String> tableauProduitColonneReference;
    @FXML
    private TableColumn<Produit, String> tableauProduitColonneType;
    @FXML
    private TextField textfielRechercherProduit;
    @FXML
    private DatePicker textfieldProduitDate;
    @FXML
    private TextField textfieldProduitNomProduit;
    @FXML
    private TextField textfieldProduitPrix;
    @FXML
    private TextField textfieldProduitQuantite;

    private ObservableList<Produit> listesProduits;
    private ProduitService produitService;


    @FXML
    private void initialize() {
        try {
            produitService = new ProduitService(DBConnection.getConnection());
            comboProduitChoisirType.setItems(FXCollections.observableArrayList("Medicament", "Materiel", "Cosmetique"));
            comboProduitChoisirType.setValue("Medicament");
            // Initialisation correcte de l'ObservableList
            listesProduits = FXCollections.observableArrayList(produitService.ListeTousProduits());
            tableauProduitColonneReference.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getReference()));
            tableauProduitColonneProduit.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
            tableauProduitColonneType.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));
            tableauProduitColonnePrix.setCellValueFactory( c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPrix()).asObject());
            tableauProduitColonneQuantite.setCellValueFactory(c-> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getQuantite()).asObject());
            tableauProduitColonneDatePeremption.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDate().toString()));
            tableauProduit.setItems(listesProduits);
            tableauProduit.setRowFactory(table->{
                TableRow<Produit> ligne = new TableRow<>();
                ligne.setOnMouseClicked(event->{
                    if(event.getClickCount() == 2 && !ligne.isEmpty()){
                        Produit pro = ligne.getItem();
                        supprimerLigneAvecConfirmation(pro.getId());
                         // Supprimer du tableau
                        tableauProduit.getItems().remove(pro);
                        tableauProduit.refresh();

                    }
                });
                return ligne;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void ajouter() {
        String nom      = textfieldProduitNomProduit.getText();
        String prix     = textfieldProduitPrix.getText();
        String quantite = textfieldProduitQuantite.getText();
        LocalDate date  = textfieldProduitDate.getValue();
        String type     = comboProduitChoisirType.getSelectionModel().getSelectedItem();

        // Vérification des champs vides
        if (nom == null || nom.trim().isEmpty() ||
            prix == null || prix.trim().isEmpty() ||
            quantite == null || quantite.trim().isEmpty() ||
            date == null) {
            messageAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }
        try {
            // Conversion des types
            Double prixU = Double.parseDouble(prix);
            Integer qte = Integer.parseInt(quantite);
            java.sql.Date sqlDate = java.sql.Date.valueOf(date); // conversion LocalDate → java.sql.Date
            // Génération d'une référence automatique
            String reference = produitService.genererReference();
            // Création de l'objet Produit
            Produit pro = new Produit( reference, nom , type,  qte, prixU, sqlDate);
            // Sauvegarde via le service/DAO
            produitService.ajouterProduit(pro);
            listesProduits.setAll(produitService.ListeTousProduits());
            messageAlert("Succès", "Produit ajouté avec succès !");
            viderchamps();
        } catch (NumberFormatException e) {
            messageAlert("Erreur", "Prix et quantité doivent être numériques !");
        } catch (Exception e) {
            e.printStackTrace();
            messageAlert("Erreur", "Une erreur est survenue lors de l'ajout !");
        }
    }

    private void supprimerLigneAvecConfirmation(int id_produit) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setContentText("Voulez-vous vraiment supprimer cette ligne ?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Appel de la méthode du service
                    produitService.supprimerProduit(id_produit);
                } catch (SQLException e) {
                    e.printStackTrace();
                    GerantController.messageAlert(null, "Erreur lors de la suppression dans la base de données !");
                }
            }
        });
    }



    @FXML
    public void viderchamps(){
        // Optionnel : vider les champs après ajout
        textfieldProduitNomProduit.clear();
        textfieldProduitPrix.clear();
        textfieldProduitQuantite.clear();
        textfieldProduitDate.setValue(null);
    }


    @FXML
    private void messageAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
