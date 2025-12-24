package com.projet.services;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.projet.models.Produit;



public class ProduitService {
    private Connection connexion;

    public ProduitService(Connection connexion){
        this.connexion = connexion ;
    }


//============================= FONCTION POUR AJOUTER UN PRODUIT ====================================================

    public void ajouterProduit(Produit produit) throws SQLException {
        String sql = "INSERT INTO produit (reference, nom, prix, quantite ,type ,date) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connexion.prepareStatement(sql);
            stmt.setString(1, produit.getReference());
            stmt.setString(2, produit.getNom());
            stmt.setDouble(3, produit.getPrix());
            stmt.setInt(4,    produit.getQuantite());
            stmt.setString(5, produit.getType());
            stmt.setDate(6,   produit.getDate());
            stmt.executeUpdate();
            System.out.println("Produit inséré avec succès !");

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

//============================ FONCTION POUR LISTER TOUS LES PRODUITS ===========================================

    public List<Produit> ListeTousProduits() throws SQLException {
        List<Produit> listeProduits = new ArrayList<>();
        String sql = "SELECT * FROM produit";
        try {
            PreparedStatement stmt = connexion.prepareStatement(sql) ;
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                listeProduits.add(ResultSetEnProduit(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listeProduits;
    }


//============================= FONCTION POUR TRANSFORMER UN RESULTATSET EN PRODUIT ==============================================


    private Produit ResultSetEnProduit(ResultSet rs) throws SQLException {
        return new Produit(
            rs.getString("reference"),
                rs.getString("nom"),
                rs.getString("type"),
                rs.getInt("quantite"),
                rs.getDouble("prix"),
                rs.getDate("date")
        );
    }


//============================= FONCTION POUR SUPPRIMER UN PRODUIT ============================================


    public void supprimerProduit(int id) throws SQLException {
        String sql = "DELETE FROM produit WHERE id = ?";
        try{
            PreparedStatement stmt = connexion.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Supprimé");
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }


//============================ FONCTION POUR MODIFIER UN PRODUIT===========================================


    public void ModifierProduit(Produit produit) throws SQLException {
        String sql = "UPDATE produit SET reference = ?, nom = ?, prix = ?, quantite = ?, type=?, date=?  WHERE id = ?";
        try {
            PreparedStatement stmt = connexion.prepareStatement(sql) ;
            stmt.setString(1, produit.getNom());
            stmt.setString(2, produit.getReference());
            stmt.setDouble(3, produit.getPrix());
            stmt.setDate(4,    produit.getDate());
            stmt.setString(5,    produit.getType());
            stmt.setInt(6,    produit.getQuantite());
            stmt.setInt(7,    produit.getId());
            stmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }


//============================= FONCTION POUR RECHERCHER PRODUIT ===========================================


    public List<Produit> rechercherProduits(String texte) {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM Produit WHERE LOWER(nom) LIKE ? OR LOWER(type) LIKE ?";

        try (PreparedStatement ps = connexion.prepareStatement(sql)) {
            ps.setString(1, texte + "%");
            ps.setString(2,  texte + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Produit produit = new Produit();
                produit.setReference(rs.getString("reference"));
                produit.setNom(rs.getString("nom"));
                produit.setPrix(rs.getDouble("prix"));
                produit.setQuantite(rs.getInt("quantite"));
                produits.add(produit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }

//============================================================================================================================================================

    public String genererReference() {
        String sql = "SELECT COUNT(*) FROM produit";
        try {
            Statement st = connexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                int count = rs.getInt(1) + 1;
                return String.format("PROD%04d", count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "PROD0001";
    }

//============================================================================================================================================================


    /** Mettre à jour le stock après une vente **/
    public void mettreAJourStock(String nomProduit, int quantiteVendue) throws SQLException {
        String sql = "UPDATE produit SET quantite = quantite - ? WHERE nom = ?";
        PreparedStatement ps = connexion.prepareStatement(sql);
        ps.setInt(1, quantiteVendue);
        ps.setString(2, nomProduit);
        ps.executeUpdate();
    }


//============================================================================================================================================================


    public int compteurStockProduit(){
        
        String req = "SELECT * FROM produit ";
        int compteur = 0;
        try {
            Statement stmt = connexion.createStatement();
            ResultSet rs =stmt.executeQuery(req);
            while(rs.next()){
                compteur +=1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compteur;
    }


//============================================================================================================================================================

    public List<Produit> listesStockFaible(){
        List<Produit> listeProduits = new ArrayList<>();
        String requete = "SELECT * FROM produit WHERE quantite <= 30 ORDER BY quantite ASC" ;
        try {
            PreparedStatement statement = connexion.prepareStatement(requete);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                listeProduits.add(ResultSetEnProduit(rs));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listeProduits;
    }

    
}
