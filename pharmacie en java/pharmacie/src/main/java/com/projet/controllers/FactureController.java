package com.projet.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class FactureController {

    @FXML
    private Label labelClientTicket;
    @FXML
    private Label labelDateTicket;
    @FXML
    private Label labelMonnaie;
    @FXML
    private Label labelMontantFacture;
    @FXML
    private Label labelMontantPaye;
    @FXML
    private Label labelNumeroTicket;
    @FXML
    private Label labelRemise;
    @FXML
    private Label labelSommeVersee;
    @FXML
    private Label labelVendeurTicket;
    @FXML
    private TableColumn<?, ?> tableauColProduit;
    @FXML
    private TableColumn<?, ?> tableauColQte;
    @FXML
    private TableColumn<?, ?> tableauMontant;
    @FXML
    private TableColumn<?, ?> tableauPU;
    @FXML
    private TableView<?> tableauTicket;

}

