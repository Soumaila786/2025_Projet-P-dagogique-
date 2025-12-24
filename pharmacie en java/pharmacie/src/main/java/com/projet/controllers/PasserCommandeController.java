package com.projet.controllers;


import java.util.List;

import com.projet.models.Commande;
import com.projet.models.Fournisseur;
import com.projet.models.LigneCommande;
import com.projet.models.Produit;
import com.projet.services.CommandeService;
import com.projet.services.DBConnection;
import com.projet.services.FournisseurService;
import com.projet.services.ProduitService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class PasserCommandeController {
    @FXML
    private ComboBox<String> combofournisseurs;

    @FXML
    private TextField textfieldPrix;
    @FXML
    private TextField textfieldQuantite;
    @FXML
    private TableColumn<LigneCommande,Double> colSousTotal;
    @FXML
    private TableColumn<LigneCommande,Double> colPrixAchat;
    @FXML
    private TableColumn<LigneCommande,String> colProduit;
    @FXML
    private TableColumn<LigneCommande, Integer> colQuantite;
    @FXML
    private TableColumn<LigneCommande, String> colType;
    @FXML
    private ComboBox<String> comboTypeProduit;
    @FXML
    private TableView<LigneCommande> tableauPanierCommande;
    @FXML
    private TextField textfieldNom;
    

    private FournisseurService fournisseurService;
    private CommandeService commandeService;
    private ObservableList<Produit> listesProduits;
    private ProduitService produitService;
    private ObservableList<LigneCommande> panier = FXCollections.observableArrayList();



    @FXML
    private void initialize() {
        chargerCombo();
        chargerPanier();
        chargerFournisseurs();
        
    }
    
    public void chargerPanier(){
        try {
            colProduit.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNomProduit()));
            colType.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));
            colPrixAchat.setCellValueFactory( c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPrixAchat()).asObject());
            colQuantite.setCellValueFactory(c-> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getQuantite()).asObject());
            colSousTotal.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getSousTotal()).asObject());
            tableauPanierCommande.setItems(panier);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void chargerCombo(){
        try {
            comboTypeProduit.setItems(FXCollections.observableArrayList("Medicament", "Materiel", "Cosmetique"));
            comboTypeProduit.setValue("Medicament");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void chargerFournisseurs() {
    try {
        FournisseurService fournisseurService = new FournisseurService(DBConnection.getConnection());
        List<Fournisseur> fournisseurs = fournisseurService.getTousFournisseurs();

        // Extraire uniquement les noms
        ObservableList<String> nomsFournisseurs = FXCollections.observableArrayList();
        for (Fournisseur f : fournisseurs) {
            nomsFournisseurs.add(f.getNom());
        }

        combofournisseurs.setItems(nomsFournisseurs);

    } catch (Exception e) {
        e.printStackTrace();
    }
}


    
    

    @FXML
    public void ajouterPanier() {
        String nom      = textfieldNom.getText();
        String prix     = textfieldPrix.getText();
        String quantite = textfieldQuantite.getText();
        String type  = comboTypeProduit.getSelectionModel().getSelectedItem();

        // Vérification des champs vides
        if (nom == null || nom.trim().isEmpty() ||
            prix == null || prix.trim().isEmpty() ||
            quantite == null || quantite.trim().isEmpty() ||
            type == null) {
            GerantController.messageAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }
        try {
            // Conversion des types
            Double prixU = Double.parseDouble(prix);
            Integer qte = Integer.parseInt(quantite);
            Double sousTotal = prixU * qte;
            // Génération d'une référence automatique
            // Création ligne commande
            LigneCommande ligne = new LigneCommande();
            ligne.setNomProduit(nom);
            ligne.setPrixAchat(prixU);
            ligne.setQuantite(qte);
            ligne.setSousTotal(sousTotal);
            panier.add(ligne);
            GerantController.messageAlert("Succès", "Ligne ajouté avec succès !");
            viderchamps();
            
        } catch (NumberFormatException e) {
            GerantController.messageAlert("Erreur", "Prix et quantité doivent être numériques !");
        } catch (Exception e) {
            e.printStackTrace();
            GerantController.messageAlert("Erreur", "Une erreur est survenue lors de l'ajout !");
        }
    }


    @FXML
    public void validerCommande() {
        String fournisseur = combofournisseurs.getSelectionModel().getSelectedItem();
        if (fournisseur == null) {
            GerantController.messageAlert("Erreur", "Veuillez choisir un fournisseur !");
            return;
        }

        if (panier.isEmpty()) {
            GerantController.messageAlert("Erreur", "Votre panier est vide !");
            return;
        }

        try {
            //  Insérer la commande
            Commande commande = new Commande();
            commande.setReference("CMD-" + System.currentTimeMillis()); // référence auto
            //commande.setFour_id(fournisseur.getId());
            commande.setDate(new java.sql.Timestamp(System.currentTimeMillis()));
            commande.setMontant(
                panier.stream().mapToInt(LigneCommande::getQuantite).sum()
            );
            commande.setStatus("EN_COURS");

            //int commandeId = commandeService.ajouterCommande(commande); // Retourne l’ID généré

            // 2️⃣ Insérer les lignes du panier
            for (LigneCommande ligne : panier) {
                //ligne.setCommandeId(commandeId); // Associer l’ID de la commande
                commandeService.ajouterLigneCommande(ligne);
            }

            GerantController.messageAlert("Succès", "Commande enregistrée avec succès !");
            panier.clear(); // vider le panier

        } catch (Exception e) {
            e.printStackTrace();
            GerantController.messageAlert("Erreur", "Impossible d’enregistrer la commande !");
        }
    }


    private void viderchamps() {
        textfieldNom.clear();
        textfieldPrix.clear();
        textfieldQuantite.clear();
        comboTypeProduit.setValue("Medicament");
    }
    }
