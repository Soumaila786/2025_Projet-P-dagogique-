package com.projet.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.projet.models.Fournisseur;

public class FournisseurService {
    private Connection connexion;

    public FournisseurService(Connection connexion){
        this.connexion = connexion;
    }

    // Récupérer tous les fournisseurs
    public List<Fournisseur> getTousFournisseurs() {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM fournisseur";
        try (PreparedStatement ps = connexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Fournisseur four = new Fournisseur(
                    rs.getString("reference"),
                    rs.getString("nom"),
                    rs.getString("adresse"),
                    rs.getString("contact")
                );
                fournisseurs.add(four);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fournisseurs;
    }

    
    public List<String> listeNomFournisseur(){
        List<String> listes = new ArrayList<>();
        String requete = "SELECT nom FROM fournisseur";
        try{
            PreparedStatement stmt =connexion. prepareStatement(requete);
            ResultSet resultat =stmt.executeQuery();
            while (resultat.next()) {
                listes.add(resultat.getString("nom"));
            }
        }catch(Exception e){
            e.getStackTrace();
        }
        return listes;
    }

    public void ajouterFournisseur(Fournisseur fournisseur) throws SQLException{
        String requete = "INSERT INTO fournisseur (reference, nom, adresse, contact)  VALUES (?,  ?,  ?,  ?)";
        try{
            PreparedStatement stmt = connexion.prepareStatement(requete);
            stmt.setString(1, fournisseur.getReference());
            stmt.setString(2,fournisseur.getNom());
            stmt.setString(3,fournisseur.getAdresse());
            stmt.setString(4,fournisseur.getContact());
            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

     // 4. Mettre à jour un fournisseur
    public void ModifierFournisseur(Fournisseur fournisseur) throws SQLException {
        String sql = "UPDATE fournisseur SET reference = ?, nom = ?, adresse = ?, contact = ? WHERE id = ?";
        try {
            PreparedStatement stmt = connexion.prepareStatement(sql) ;
            stmt.setString(1, fournisseur.getReference());
            stmt.setString(2, fournisseur.getNom());
            stmt.setString(3, fournisseur.getAdresse());
            stmt.setString(4, fournisseur.getContact());
            stmt.setInt(6, fournisseur.getId());
            stmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimerFournisseur(int id) throws SQLException{
        String requete = "DELETE FROM fournisseur WHERE id = ?";
        try{
            PreparedStatement stmt = connexion.prepareStatement(requete);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public String genererReference() {
        String sql = "SELECT COUNT(*) FROM fournisseur";
        try {
            Statement st = connexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                int count = rs.getInt(1) + 1;
                return String.format("FOUR%04d", count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "FOUR0001";
    }

    public Fournisseur getFournisseurParNom(String nom) throws SQLException {
        String sql = "SELECT * FROM fournisseur WHERE nom = ?";
        PreparedStatement ps = connexion.prepareStatement(sql);
        ps.setString(1, nom);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            //return new Fournisseur(rs.getInt("id"), rs.getString("nom"), rs.getString("adresse"),rs.getString("contact"));
        }
        return null;
    }

}
