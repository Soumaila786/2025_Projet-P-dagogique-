package com.projet.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.projet.models.Commande;
import com.projet.models.LigneCommande;


public class CommandeService {
    private Connection connexion;
    public CommandeService(Connection connexion){
        this.connexion = connexion;
    }

    public String genererReference() {
        String sql = "SELECT COUNT(*) FROM commande";
        try {
            Statement st = connexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                int count = rs.getInt(1) + 1;
                return String.format("CMD%04d", count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "CMD0001";
        }

    

    public int ajouterCommande(Commande commande) throws SQLException {
        String sql = "INSERT INTO commande(reference, fournisseur_id, montant, date, statut) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = connexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setString(1, commande.getReference());
        ps.setInt(2, commande.getFour_id());
        ps.setDouble(3, commande.getMontant());
        ps.setTimestamp(4, new java.sql.Timestamp(commande.getDate().getTime()));
        ps.setString(5, commande.getStatus());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1); // retourne l'ID de la commande insérée
        }
        return -1;
    }

    /** Ajouter une ligne de vente dans la table ligne_vente **/
    public void ajouterLigneCommande(LigneCommande ligne) throws SQLException {
        String sql = "INSERT INTO ligne_commande(commande_id, nom_produit, prix, quantite, Sous_total) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = connexion.prepareStatement(sql);
        ps.setInt(1, ligne.getId());
        ps.setString(2, ligne.getNomProduit());
        ps.setInt(3, ligne.getQuantite());
        ps.setDouble(4, ligne.getPrixAchat());
        ps.setDouble(5, ligne.getSousTotal());
        ps.executeUpdate();
    }




    }

