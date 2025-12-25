// package com.projet.controllers;

// import java.util.List;

// import com.projet.models.LigneVente;
// import com.projet.models.Produit;
// import com.projet.models.Vente;

// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// import javafx.fxml.FXML;
// import javafx.scene.control.Label;
// import javafx.scene.control.TableColumn;
// import javafx.scene.control.TableView;
// import javafx.scene.control.cell.PropertyValueFactory;

// public class DetailsVenteLibreController {

//     @FXML private TableView<Produit> tableauDetailsVente;
//     @FXML private TableColumn<Produit, String> colNomProduit;
//     @FXML private TableColumn<Produit, Double> colPrixUnitaire;
//     @FXML private TableColumn<Produit, Integer> colQuantiteProduit;
//     @FXML private TableColumn<Produit, String> colReferenceProduit;
//     @FXML private TableColumn<Produit, Double> colSousTotal;
//     @FXML private TableColumn<Produit, String> colTypeProduit;
//     @FXML private Label labelDateVente;
//     @FXML private Label labelMontantTotalVente;
//     @FXML private Label labelNomClient;
//     @FXML private Label labelReferenceVente;
//     @FXML private Label labelTelephoneClient;
//     @FXML private Label labelTypeVente;


// @FXML
// public void initialize() {
//     // Lier les colonnes aux propriétés de Produit
//     colNomProduit.setCellValueFactory(new PropertyValueFactory<>("nom"));
//     colPrixUnitaire.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
//     colQuantiteProduit.setCellValueFactory(new PropertyValueFactory<>("quantite"));
//     colReferenceProduit.setCellValueFactory(new PropertyValueFactory<>("reference"));
//     colTypeProduit.setCellValueFactory(new PropertyValueFactory<>("type"));
// }

// // Méthode pour afficher les détails d'une vente
// public void afficherDetailsVente(Vente vente) {
//     // Remplir les labels
//     labelDateVente.setText(vente.getDate().toString());
//     labelMontantTotalVente.setText(String.valueOf(vente.getMontant()));
//     labelNomClient.setText(vente.getClient().getNom());
//     labelReferenceVente.setText(vente.getcodeVente());
//     labelTelephoneClient.setText(vente.getClient().getTel());
//     labelTypeVente.setText(vente.getTypeVente());

//     // Charger les produits de la vente (les lignes de vente)
//     List<LigneVente> lignes = vente.getLigne();

//     // Convertir en ObservableList pour le TableView
//     ObservableList<Produit> produits = FXCollections.observableArrayList();
//     for (LigneVente ligne : lignes) {
//         Produit p = ligne.getProduit();
//         p.setQuantite(ligne.getQuantite()); // injecter la quantité vendue
//         produits.add(p);
//     }

//     tableauDetailsVente.setItems(produits);
// }




// }

package com.projet.controllers;

import com.projet.models.LigneVente;
import com.projet.models.Vente;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DetailsVenteLibreController {

        @FXML private TableView<LigneVente> tableauDetailsVente;
        @FXML private TableColumn<LigneVente, String> colNomProduit;
        @FXML private TableColumn<LigneVente, Double> colPrixUnitaire;
        @FXML private TableColumn<LigneVente, Integer> colQuantiteProduit;
        @FXML private TableColumn<LigneVente, String> colReferenceProduit;
        @FXML private TableColumn<LigneVente, Double> colSousTotal;
        @FXML private TableColumn<LigneVente, String> colTypeProduit;
        @FXML private Label labelDateVente;
        @FXML private Label labelMontantTotalVente;
        @FXML private Label labelNomClient;
        @FXML private Label labelReferenceVente;
        @FXML private Label labelTelephoneClient;
        @FXML private Label labelTypeVente;

        private Vente vente;

        public void setVente(Vente vente) {
        this.vente = vente;

        // Labels
        labelReferenceVente.setText("Vente N° : " + vente.getcodeVente());
        labelDateVente.setText("Date : " + vente.getDate());
        labelNomClient.setText("Client : " + vente.getClient().getNom());
        labelTelephoneClient.setText("Téléphone : " + vente.getClient().getTel());
        labelTypeVente.setText("Type de vente : " + vente.getTypeVente());
        labelMontantTotalVente.setText(vente.getMontant() + " F CFA");

        // Colonnes du tableau
        colNomProduit.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProduit().getNom()));
        colReferenceProduit.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProduit().getReference()));
        colTypeProduit.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProduit().getType()));
        colQuantiteProduit.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getQuantite()).asObject());
        colPrixUnitaire.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getPrix()).asObject());
        colSousTotal.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getTotal()).asObject());

        // Remplir le tableau avec les lignes de vente
        tableauDetailsVente.setItems(FXCollections.observableArrayList(vente.getLigne()));
        }
        
}

