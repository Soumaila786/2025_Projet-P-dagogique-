package com.projet.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.projet.models.Client;


public class ClientService {
    private Connection connexion;

    public ClientService(Connection connexion){
        this.connexion = connexion;

    }

//====================================================================================================================

    // 1. Ajouter un client
    public void ajouterclient(Client client) throws SQLException {
        String sql = "INSERT INTO client (nom, telephone) VALUES (?, ?)";
        try {
            PreparedStatement stmt = connexion.prepareStatement(sql);
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getTel());
            stmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
//====================================================================================================================

    // Mettre a jour un client
    public void miseAjourClient(Client client ) throws SQLException{
        String requete = "UPDATE client SET NOM = ? , telephone = ? WHERE id = ?";
        try{
            PreparedStatement stmt = connexion.prepareStatement(requete);
            stmt.setNString(0,client.getNom());
            stmt.setNString(1,client.getTel());
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

//====================================================================================================================

    //Supprimer un client 
    public void supprimerClient(Client client) throws SQLException{
        String requete = "DELETE FROM client WHERE id = ?";
        try{
            PreparedStatement stmt = connexion.prepareStatement(requete);
            stmt.setInt(1,client.getId());
            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

//====================================================================================================================
    //Afficher la liste des clients
    public List<Client> listeTousClient() throws SQLException{
        List<Client> clients = new ArrayList<>();
        String requete = "SELECT * FROM client";
        try{
            PreparedStatement stmt =connexion.prepareStatement(requete);
            ResultSet resultats = stmt.executeQuery();
            while( resultats.next()){
                clients.add(new Client(
                    resultats.getInt("id"),
                    resultats.getString("reference"),
                    resultats.getString("nom"),
                    resultats.getString("telephone")
                ));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return clients;
    }

//====================================================================================================================

    //Rechercher un client par du texte
    public List<Client> rechercherClient(String texte) throws SQLException{
        List<Client> listesClients = new ArrayList<>();
        String requete = "SELECT * FROM client WHERE nom LIKE ? OR telephone LIKE ?";
        try{
            PreparedStatement stmt = connexion.prepareStatement(requete);
            String contenueTexte ="%"+texte+"%";
            stmt.setString(1, contenueTexte);
            stmt.setString(2, contenueTexte);
            ResultSet resultats = stmt.executeQuery();
            while(resultats.next()){
                listesClients.add( new Client(
                    resultats.getInt( "id"),
                    resultats.getString("reference"),
                    resultats.getString("nom"),
                    resultats.getString("telephone")
                    ));
            }
    }catch(SQLException e){
        e.printStackTrace();
    }
        return listesClients;
    }

    /** Ajouter une vente dans la table client et retourner l'ID généré **/
    public int ajouterClient(Client client) throws SQLException {
        String sql = "INSERT INTO client(reference, nom,telephone) VALUES( ?, ?, ? )";
        PreparedStatement ps = connexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, client.getReference());
        ps.setString(2, client.getNom());
        ps.setString(3, client.getTel());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1); // retourne l'ID de la vente
        }
        return -1;
    }

    public String genererReference() {
        String sql = "SELECT COUNT(*) FROM client";
        try {
            Statement st = connexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                int count = rs.getInt(1) + 1;
                return String.format("CLT%04d", count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "CLT0001";
    }
}
