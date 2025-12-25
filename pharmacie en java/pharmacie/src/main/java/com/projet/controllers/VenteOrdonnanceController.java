package com.projet.controllers;

import java.sql.SQLException;

import com.projet.models.Client;
import com.projet.models.LigneVente;
import com.projet.models.Ordonnance;
import com.projet.models.Produit;
import com.projet.models.Vente;
import com.projet.services.ClientService;
import com.projet.services.DBConnection;
import com.projet.services.FactureService;
import com.projet.services.OrdonnanceService;
import com.projet.services.ProduitService;
import com.projet.services.VenteService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;

public class VenteOrdonnanceController {

    @FXML
    private AnchorPane anchorVenteLibre;
    @FXML
    private Label labelMontantTotal;
    @FXML
    private TableView<Produit> tableauListeProduit;
    @FXML
    private TableColumn<Produit, Double> tableauListeProduitColPrix;
    @FXML
    private TableColumn<Produit, String> tableauListeProduitColProduit;
    @FXML
    private TableColumn<Produit, Integer> tableauListeProduitColQuantite;
    @FXML
    private TableColumn<Produit, String> tableauListeProduitColReference;
    @FXML
    private TableColumn<Produit, String> tableauListeProduitColType;
    @FXML
    private TableView<LigneVente> tableauPanier;
    @FXML
    private TableColumn<LigneVente, Double> tableauPanierColPrix;
    @FXML
    private TableColumn<LigneVente, String> tableauPanierColProduit;
    @FXML
    private TableColumn<LigneVente, Integer> tableauPanierColQuantite;
    @FXML
    private TableColumn<LigneVente, Double> tableauPanierColSousTotal;
    @FXML
    private TextField textfieldContact;
    @FXML
    private TextField textfieldNomClient;
    @FXML
    private TextField textfieldNomMedecin;
    @FXML
    private TextField textfieldRechercherProduit;
    @FXML
    private TextField textfieldRefernceOrdonnance;

    private ObservableList<LigneVente> panier = FXCollections.observableArrayList();
    private ObservableList<Produit> listeProduits ;
    @FXML
    private VenteService venteService = new VenteService(DBConnection.getConnection());
    @FXML
    private ClientService clientService = new ClientService(DBConnection.getConnection());
    @FXML
    private OrdonnanceService ordonnanceService = new OrdonnanceService(DBConnection.getConnection());
    @FXML
    private ProduitService produitService = new ProduitService(DBConnection.getConnection());

    @FXML
    private void initialize() {
        chargerTableau();
        chargerPanier();
}

    private void chargerPanier() {
        try {
            tableauPanierColProduit.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getnomProduit()));
            tableauPanierColSousTotal.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getTotal()).asObject());
            tableauPanierColPrix.setCellValueFactory( c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPrix()).asObject());
            tableauPanierColQuantite.setCellValueFactory(c-> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getQuantite()).asObject());
            tableauPanier.setItems(panier);
            miseAJourTotal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    private void miseAJourTotal() {
       // Calcul et affichage du montant total
        double total = panier.stream().mapToDouble(LigneVente::getTotal).sum();
        labelMontantTotal.setText(String.valueOf(total));
    }


    private void chargerTableau() {
        try {
            produitService = new ProduitService(DBConnection.getConnection());
            listeProduits = FXCollections.observableArrayList(produitService.ListeTousProduits());
            tableauListeProduitColReference.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getReference()));
            tableauListeProduitColProduit.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
            tableauListeProduitColType.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));
            tableauListeProduitColPrix.setCellValueFactory( c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPrix()).asObject());
            tableauListeProduitColQuantite.setCellValueFactory(c-> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getQuantite()).asObject());
            tableauListeProduit.setItems(listeProduits);
            tableauListeProduit.setRowFactory(table->{
                TableRow<Produit> ligne = new TableRow<>();
                ligne.setOnMouseClicked(event->{
                    if(event.getClickCount() == 2 && !ligne.isEmpty()){
                        Produit pro = ligne.getItem();
                        demanderQuantite(pro);
                    }
                });
                return ligne;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Demande de la quantité pour un produit **/
    private void demanderQuantite(Produit pro) {
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Quantité à vendre");
        dialog.setHeaderText("Produit : " + pro.getNom());
        dialog.setContentText("Quantité : ");
        dialog.setResizable(false);

        dialog.showAndWait().ifPresent(qte -> {
            try {
                int quantite = Integer.parseInt(qte);
                if (quantite > 0 && pro.getQuantite() >= quantite) {
                    double sousTotal = pro.getPrix() * quantite;
                    LigneVente ligne = new LigneVente(pro.getNom(), pro.getPrix(), quantite, sousTotal);

                    panier.add(ligne);
                    pro.setQuantite(pro.getQuantite() - quantite); 
                    tableauListeProduit.refresh();
                    miseAJourTotal();
                } else {
                    GerantController.messageAlert(null, "Stock insuffisant !");
                }
            } catch (NumberFormatException e) {
                GerantController.messageAlert(null, "Veuillez entrer un nombre valide !");
            }
        });
    }


    //Validation de la vente
    public void validerVente() throws SQLException {

        String referenceClient = clientService.genererReference();
        String nomClient = textfieldNomClient.getText();
        String telephone = textfieldContact.getText();
        String referenceOrdonnance = ordonnanceService.genererReference();
        String numOrdonnance = textfieldRefernceOrdonnance.getText();
        String nomMedecin =textfieldNomMedecin.getText();

        if(panier.isEmpty()){
            GerantController.messageAlert(null, "Votre panier est vide !");
            return;
        }

        if(nomClient.isEmpty()||telephone.isEmpty()||numOrdonnance.isEmpty()||nomMedecin.isEmpty()){
            GerantController.messageAlert(null, "Veuillez remplir tous les champs SVP !");
            return;
        }
        Client client = new Client();
        client.setNom(nomClient);
        client.setReference(referenceClient);
        client.setTel(telephone);
        int id_client= clientService.ajouterClient(client);

        Ordonnance ordonnance =new Ordonnance();
        ordonnance.setIdClient(id_client);
        ordonnance.setReference(referenceOrdonnance);
        ordonnance.setNomMedecin(nomMedecin);
        ordonnance.setNumOrdonnance(numOrdonnance);
        int id_ordonnance = ordonnanceService.ajouterOrdonnance(ordonnance);
    

        String reference = venteService.genererReference();
        String typeVente = "Ordonnance";
        double montant = panier.stream().mapToDouble(LigneVente::getTotal).sum();
        int quantiteTotal = panier.stream().mapToInt(LigneVente::getQuantite).sum();

        Vente vente = new Vente();
        vente.setIdClient(id_client);
        vente.setIdOrdonnance(id_ordonnance);
        vente.setCodeVente(reference);
        vente.setDate(new java.sql.Timestamp(System.currentTimeMillis()));
        vente.setTypeVente(typeVente);
        vente.setMontant(montant);
        vente.setQuantiteTotal(quantiteTotal);
        System.out.println(id_client);

        try {
            int venteId = venteService.ajouterVente(vente);
            for(LigneVente ligne : panier) {
                ligne.setId(venteId);
                venteService.ajouterLigneVente(ligne);
                produitService.mettreAJourStock(ligne.getnomProduit(), ligne.getQuantite());
            }
            GerantController.messageAlert(null, "Vente validée avec succès !");
            
            FactureService.genererFacture(vente,panier);
            panier.clear();
            tableauPanier.setItems(panier);
            miseAJourTotal();
            tableauListeProduit.refresh();

        } catch (SQLException e) {
            e.printStackTrace();
            GerantController.messageAlert(null, "Erreur lors de la validation de la vente !");
        }
    }


    public void annulerVente(){
        panier.clear();
        textfieldContact.clear();
        textfieldNomClient.clear();
        textfieldNomMedecin.clear();
        textfieldRefernceOrdonnance.clear();
        labelMontantTotal.setText("0.0");
        tableauListeProduit.refresh();
    }

}
