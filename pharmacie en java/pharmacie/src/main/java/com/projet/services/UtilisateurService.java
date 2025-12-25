package com.projet.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.projet.models.Utilisateur;

public class UtilisateurService {
    private Connection connexion;

    public UtilisateurService(Connection connexion) {
        this.connexion = connexion;
    }


//====================================================================================================================

    // 1. Ajouter un utilisateur
    public void ajouterUtilisateur(Utilisateur utilisateur) throws SQLException {
        String sql = "INSERT INTO utilisateur (nom, email, role, salaire, login, mdp, diplome) VALUES (?, ?, ?, ?, ?, ? ,?)";
        try {
            PreparedStatement stmt = connexion.prepareStatement(sql);
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getEmail());
            stmt.setString(3, utilisateur.getRole());
            stmt.setInt(4, utilisateur.getSalaire());
            stmt.setString(5, utilisateur.getLogin());
            stmt.setString(6, utilisateur.getMdp());
            stmt.setString(7, utilisateur.getDiplome());
            stmt.executeUpdate();
            
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }


//====================================================================================================================


    public Utilisateur getUtilisateurById(int id) throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE id = ?";
        try {
            PreparedStatement stmt = connexion.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return transformerResultSetEnUtilisateur(rs);
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//====================================================================================================================

    
    public List<Utilisateur> ListeTousUtilisateurs() throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";
        try {
            PreparedStatement stmt = connexion.prepareStatement(sql) ;
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                utilisateurs.add(transformerResultSetEnUtilisateur(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(utilisateurs);
        return utilisateurs;
    }

//====================================================================================================================


    // 4. Mettre à jour un utilisateur
    public void ModifierUtilisateur(Utilisateur utilisateur) throws SQLException {
        String sql = "UPDATE utilisateur SET nom = ?, login = ?, email = ?, mdp = ?, role = ?, salaire =? WHERE id = ?";
        try {
            PreparedStatement stmt = connexion.prepareStatement(sql) ;
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getLogin());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getMdp());
            stmt.setString(5, utilisateur.getRole());
            stmt.setDouble(5, utilisateur.getSalaire());
            stmt.setInt(6, utilisateur.getId());
            stmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

//====================================================================================================================


    public void supprimerUtilisateur(int id) throws SQLException {
        String sql = "DELETE FROM utilisateur WHERE id = ?";
        try{
            PreparedStatement stmt = connexion.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

//====================================================================================================================

    // Méthode utilitaire pour transformer ResultSet → Utilisateur
    private Utilisateur transformerResultSetEnUtilisateur(ResultSet rs) throws SQLException {
        return new Utilisateur(
                rs.getString("nom"),
                rs.getString("email"),
                rs.getString("role"),
                rs.getInt("salaire"),
                rs.getString("login"),
                rs.getString("mdp"),
                rs.getString("diplome")
        );
    }

//====================================================================================================================

    // Méthode pour vérifier les informations de connexion
    public Utilisateur verifierLogin(String login, String mdp) {
        String sql = "SELECT * FROM utilisateur WHERE login = ? AND mdp = ?";
        try {
            PreparedStatement stmt = connexion.prepareStatement(sql);
            stmt.setString(1, login);
            stmt.setString(2, mdp);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utilisateur user = transformerResultSetEnUtilisateur(rs);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
//====================================================================================================================

    // Méthode pour supprimer un utilisateur
    public void SupprimerUtilisateur(Utilisateur utilisateurSelectionne) {
        String sql = "DELETE FROM utilisateur WHERE id = ?";
        try{
            PreparedStatement stmt = connexion.prepareStatement(sql);
            stmt.setInt(1, utilisateurSelectionne.getId());
            stmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> listesUtilisateurs() throws SQLException{
        List<String> listes = new ArrayList<>();
        String requete = "SELECT nom, email, role, salaire, diplome FROM utilisateur";
        try {
            PreparedStatement stmt = connexion.prepareStatement(requete);
            ResultSet resultats = stmt.executeQuery();
            while(resultats.next()){
                resultats.getString("nom");
                resultats.getString("email");
                resultats.getString("role");
                resultats.getString("salaire");
                resultats.getString("diplome");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listes;

    }

    
}


