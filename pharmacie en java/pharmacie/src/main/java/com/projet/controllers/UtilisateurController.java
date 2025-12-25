package com.projet.controllers;

import java.sql.SQLException;

import com.projet.models.Utilisateur;
import com.projet.services.DBConnection;
import com.projet.services.UtilisateurService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class UtilisateurController {
    @FXML
    private AnchorPane anchorUtilisateur;
    @FXML
    private ComboBox<String> comboDiplome;
    @FXML
    private ComboBox<String> comboRole;
    @FXML
    private TableView<Utilisateur> tableauUtilisateur;
    @FXML
    private TableColumn<Utilisateur, String> tableauUtilisateurColDiplome;
    @FXML
    private TableColumn<Utilisateur, String> tableauUtilisateurColEmail;
    @FXML
    private TableColumn<Utilisateur, String> tableauUtilisateurColNomPrenom;
    @FXML
    private TableColumn<Utilisateur, String> tableauUtilisateurColRole;
    @FXML
    private TableColumn<Utilisateur, Integer> tableauUtilisateurColSalaire;
    @FXML
    private TextField textfieldSalaire;
    @FXML
    private TextField textfieldEmail;
    @FXML
    private TextField textfieldNomPrenom;
    @FXML
    private TextField textfieldUtilisateurRechercher;

    private ObservableList<Utilisateur> utilisateurs;
    private UtilisateurService utilisateurService;

//========================================================================================================================================

    @FXML
    public void initialize() {
    // 1. Définir les valeurs possibles
    utilisateurService = new UtilisateurService(DBConnection.getConnection());
    comboRole.setItems(FXCollections.observableArrayList("Gerant", "Vendeur", "Caissier"));
    comboRole.setValue("Vendeur");
    comboDiplome.setItems(FXCollections.observableArrayList("NULL","BTS gestion", "Commerce ou Comptabilité","BEPC", "BAC", "BAC+2", "BAC+3", "BAC+5", "Doctorat en Pharmacie" ));

    try {
        utilisateurs = FXCollections.observableArrayList(utilisateurService.ListeTousUtilisateurs());
        tableauUtilisateurColNomPrenom.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
        tableauUtilisateurColEmail.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));
        tableauUtilisateurColRole.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getRole()));
        tableauUtilisateurColDiplome.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getDiplome()));
        tableauUtilisateurColSalaire.setCellValueFactory(c-> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getSalaire()).asObject());
        tableauUtilisateur.setItems(utilisateurs);
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    

    }



//========================================================================================================================================

    // Méthode pour ajouter un utilisateur
    @FXML
    public void ajouter() {
        String nomPrenom = textfieldNomPrenom.getText();
        String email     = textfieldEmail.getText();
        String username  = "Username";
        String password = "1234";
        String salaire1 = textfieldSalaire.getText();
        String role     = comboRole.getSelectionModel().getSelectedItem();
        String diplome  = comboDiplome.getSelectionModel().getSelectedItem();

        if (nomPrenom ==null||nomPrenom.trim().isEmpty() ||
            email ==null ||email.trim().isEmpty() ||
            salaire1==null|| salaire1.trim().isEmpty()||
            role ==null || diplome ==null) {
            messageAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }
        try {

            //conversion en entier
            Integer salaire = Integer.parseInt(salaire1);
            Utilisateur nouvelUtilisateur = new Utilisateur( nomPrenom, email, role, salaire, username, password, diplome);
            utilisateurService.ajouterUtilisateur(nouvelUtilisateur);
            utilisateurs.setAll(utilisateurService.ListeTousUtilisateurs());
            messageAlert("Succès", "Utilisateur ajouté avec succès.");
        } catch (NumberFormatException e) {
            messageAlert("Erreur", "Salaire doit être entier !");
        } catch (Exception e) {
            e.printStackTrace();
            messageAlert("Erreur", "Une erreur est survenue lors de l'ajout !");
        }
        viderChamps();
    }


//========================================================================================================================================


    // Méthode pour modifier un utilisateur
    @FXML
    public void modifier() {
        Utilisateur utilisateurSelectionne = tableauUtilisateur.getSelectionModel().getSelectedItem();
        if (utilisateurSelectionne != null) {
            String nomPrenom = textfieldNomPrenom.getText();
            String email = textfieldEmail.getText();
            String role = comboRole.getValue();
            String salaire1 = textfieldSalaire.getText();
            String diplome = comboDiplome.getValue();

            // Validation des champs
            if (nomPrenom.isEmpty() || email.isEmpty() || role.isEmpty() || salaire1 == null || diplome.isEmpty()) {
                messageAlert("Information", "Veuillez selectionnez une ligne dans le tableau .");
                return;
            }
            Integer salaire = Integer.parseInt(salaire1);
            // Mise à jour de l'utilisateur sélectionné dans le tableau et la base de données
            utilisateurSelectionne.setNom(nomPrenom);
            utilisateurSelectionne.setEmail(email);
            utilisateurSelectionne.setRole(role);
            utilisateurSelectionne.setSalaire(salaire);
            utilisateurSelectionne.setDiplome(diplome);
            //refresh le tableau pour afficher les modifications
            tableauUtilisateur.refresh();
            try {
                utilisateurService.ModifierUtilisateur(utilisateurSelectionne);
                messageAlert("Succès", "Utilisateur modifié avec succès.");
            } catch (SQLException e) {
                e.printStackTrace();
                messageAlert("Erreur", "Une erreur est survenue lors de la modification de l'utilisateur.");
                return;
            }

            viderChamps();
        } else {
            messageAlert("Erreur", "Veuillez sélectionner un utilisateur à modifier.");
        }
    }


//========================================================================================================================================

    // Méthode pour vider les champs
    @FXML
    public void viderChamps() {
        textfieldNomPrenom.clear();
        textfieldEmail.clear();
        textfieldSalaire.clear();
        comboRole.setValue("Vendeur");
        comboDiplome.setValue("NULL");
    }

//========================================================================================================================================



    // Méthode pour supprimer un utilisateur
    @FXML
    public void supprimer() {
        Utilisateur utilisateurSelectionne = tableauUtilisateur.getSelectionModel().getSelectedItem();
        if (utilisateurSelectionne != null) {
            tableauUtilisateur.getItems().remove(utilisateurSelectionne);
            utilisateurService.SupprimerUtilisateur(utilisateurSelectionne);
            messageAlert("Succès", "    Utilisateur supprimé avec succès.");
                
        }else {
            messageAlert("Erreur", "Veuillez sélectionner un utilisateur à supprimer.");
        }
    }


//========================================================================================================================================


    // Méthode pour rechercher un utilisateur
    @FXML
    public void rechercher() {
        String critereRecherche = textfieldUtilisateurRechercher.getText().toLowerCase();
        ObservableList<Utilisateur> utilisateursFiltres = FXCollections.observableArrayList();
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getNom().toLowerCase().contains(critereRecherche) ||
                utilisateur.getEmail().toLowerCase().contains(critereRecherche) ||
                utilisateur.getRole().toLowerCase().contains(critereRecherche)) {
                utilisateursFiltres.add(utilisateur);
            }
        }
        tableauUtilisateur.setItems(utilisateursFiltres);
    }

//========================================================================================================================================

    // Méthode pour afficher un message d'alerte
    @FXML
    public void messageAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

