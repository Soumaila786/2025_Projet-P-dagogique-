package com.projet.controllers;


import com.projet.models.Fournisseur;
import com.projet.services.DBConnection;
import com.projet.services.FournisseurService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class FournisseurController {

    @FXML
    private AnchorPane anchorFournisseurs;
    @FXML
    private TableView<Fournisseur> tableauFournisseur;
    @FXML
    private TableColumn<Fournisseur, String> tableauFournisseurColAdresse;
    @FXML
    private TableColumn<Fournisseur, String> tableauFournisseurColContact;
    @FXML
    private TableColumn<Fournisseur, String> tableauFournisseurColNomFour;
    @FXML
    private TableColumn<Fournisseur, String> tableauFournisseurColReference;
    @FXML
    private TextField textfieldAdresseFour;
    @FXML
    private TextField textfieldContactFour;
    @FXML
    private TextField textfieldNomFour;
    @FXML
    private TextField textfieldRechercher;

    private ObservableList<Fournisseur> listesFournisseurs;
    private FournisseurService fournisseurService;
    



    @FXML
    private void initialize() {
        try {
            fournisseurService = new FournisseurService(DBConnection.getConnection());
            listesFournisseurs = FXCollections.observableArrayList(fournisseurService.getTousFournisseurs());
    
            tableauFournisseurColReference.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getReference()));
            tableauFournisseurColNomFour.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
            tableauFournisseurColAdresse.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getAdresse()));
            tableauFournisseurColContact.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getContact()));
            tableauFournisseur.setItems(listesFournisseurs);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    

    public void enregistrer(){
        String reference = fournisseurService.genererReference();
        String nom = textfieldNomFour.getText();
        String adresse = textfieldAdresseFour.getText();
        String contact = textfieldContactFour.getText();

        if (nom.isEmpty() || adresse.isEmpty() || contact.isEmpty()){
            GerantController.messageAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        try {
            Fournisseur fournisseur = new Fournisseur(reference, nom, adresse, contact);
            fournisseurService.ajouterFournisseur(fournisseur);
            listesFournisseurs.setAll(fournisseurService.getTousFournisseurs());
            GerantController.messageAlert("Succès","Fournisseur ajouté avec succès.");
            viderchamps();
        } catch (Exception e) {
            GerantController.messageAlert("Erreur", "Une erreur est survenue lors de l'ajout !");
        }
    }

    public void viderchamps(){
        textfieldNomFour.clear();
        textfieldAdresseFour.clear();
        textfieldContactFour.clear();
    }
}