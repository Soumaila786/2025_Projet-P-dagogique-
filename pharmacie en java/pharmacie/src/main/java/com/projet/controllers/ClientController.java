package com.projet.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ClientController {
    @FXML
    private AnchorPane anchorClients;
    @FXML
    private TableView<?> tableauClient;
    @FXML
    private TableColumn<?, ?> tableauClientsColNom;
    @FXML
    private TableColumn<?, ?> tableauClientsColNomMedecin;
    @FXML
    private TableColumn<?, ?> tableauClientsColNumeroTel;
    @FXML
    private TableColumn<?, ?> tableauClientsColReference;
    @FXML
    private TableColumn<?, ?> tableauClientsColReferenceOrdonnance;
    @FXML
    private TextField textfieldClientNom;
    @FXML
    private TextField textfieldClientNomMedecin;
    @FXML
    private TextField textfieldClientNumeroTel;
    @FXML
    private TextField textfieldClientRechercher;
    @FXML
    private TextField textfieldClientReferenceOrdonnance;

    public void initialize() {
        // Initialisation si nécessaire
    }

    public void ajouterClient() {
        // Logique pour ajouter un client
    }
    public void modifierClient() {
        // Logique pour modifier un client
    }
    public void supprimerClient() {
        // Logique pour supprimer un client
    }
    

}
