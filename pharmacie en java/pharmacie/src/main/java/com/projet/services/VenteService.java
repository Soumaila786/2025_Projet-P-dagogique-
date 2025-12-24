package com.projet.services;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.projet.models.Client;
import com.projet.models.LigneVente;
import com.projet.models.Ordonnance;
import com.projet.models.Produit;
import com.projet.models.Vente;


public class VenteService {
    private Connection connexion;

    public VenteService(Connection connexion) {
        this.connexion = connexion;
    }

    public List<Vente> listesVentesDuJour() throws SQLException{
        List<Vente> listes = new ArrayList<>();
        String requete = "SELECT * From vente WHERE DATE(date) = CURDATE() ";
        try {
            PreparedStatement statement = connexion.prepareStatement(requete);
            ResultSet rSet = statement.executeQuery();
            while(rSet.next()){
                listes.add(ResultSetEnVente(rSet));
            }
        } catch (Exception e) {
        }
        return listes;
    }
    
    // cette fonction permet de retourner une listes de tous les ventes enregeistres dans la base de données
    public List<Vente> listesTousVentes()throws  SQLException{
        List<Vente> listes = new ArrayList<>();
        String requete = "SELECT * From vente";
        try {
            PreparedStatement statement = connexion.prepareStatement(requete);
            ResultSet rSet = statement.executeQuery();
            while(rSet.next()){
                listes.add(ResultSetEnVente(rSet));
            }
        } catch (Exception e) {
        }
        return listes;
    }


    private Vente ResultSetEnVente(ResultSet rs) throws SQLException {
        return new Vente(
            rs.getString("codeVente"),
                rs.getTimestamp("date"),
                rs.getDouble("montant"),
                rs.getString("typeVente"),
                rs.getInt("quantiteTotal")
        );
    }


    /** Ajouter une vente dans la table vente et retourner l'ID généré **/
    public int ajouterVente(Vente vente) throws SQLException {
        String sql = "INSERT INTO vente(codeVente, date, montant, typeVente, quantiteTotal) VALUES( ?, ?, ?, ?, ? )";
        PreparedStatement ps = connexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, vente.getcodeVente());
        ps.setTimestamp(2, new java.sql.Timestamp(vente.getDate().getTime()));
        ps.setDouble(3, vente.getMontant());
        ps.setString(4, vente.getTypeVente());
        ps.setInt(5, vente.getQuantiteTotal());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1); // retourne l'ID de la vente
        }
        return -1;
    }

    /** Ajouter une vente dans la table vente et retourner l'ID généré **/
    public int ajouterVenteOrdonnace(Vente vente) throws SQLException {
        String sql = "INSERT INTO vente(codeVente, date, montant, typeVente, quantiteTotal,client_id,ordonnance_id) VALUES( ?, ?, ?, ?, ?, ?, ? )";
        PreparedStatement ps = connexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, vente.getcodeVente());
        ps.setTimestamp(2, new java.sql.Timestamp(vente.getDate().getTime()));
        ps.setDouble(3, vente.getMontant());
        ps.setString(4, vente.getTypeVente());
        ps.setInt(5, vente.getQuantiteTotal());
        ps.setInt(6, vente.getIdClient());
        ps.setInt(7, vente.getIdOrdonnance());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1); // retourne l'ID de la vente
        }
        return -1;
    }

    /** Ajouter une ligne de vente dans la table ligne_vente **/
    public void ajouterLigneVente(LigneVente ligne) throws SQLException {
        String sql = "INSERT INTO ligne_vente(vente_id, produit_id, quantite, prix, total) VALUES(?,?,?,?,?)";
        PreparedStatement ps = connexion.prepareStatement(sql);
        ps.setInt(1, ligne.getId());
        ps.setInt(2, ligne.getId()); // Assure-toi que LigneVente a le produitId
        ps.setInt(3, ligne.getQuantite());
        ps.setDouble(4, ligne.getPrix());
        ps.setDouble(5, ligne.getTotal());
        ps.executeUpdate();
    }

    public String genererReference() {
        String sql = "SELECT COUNT(*) FROM vente";
        try {
            Statement st = connexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                int count = rs.getInt(1) + 1;
                return String.format("VENTE%04d", count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "VENTE0001";
    }



    public int compteurVenteJour() throws SQLException{
        String req = "SELECT * FROM vente WHERE DATE(date) = CURDATE()";
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


    public int compteurVenteTotal() throws SQLException{
        String req = "SELECT * FROM vente ";
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

    public double compteurChiffreAffaireTotalVentesDuJour() throws SQLException {
        String sql = "SELECT SUM(montant) AS total_jour FROM vente WHERE DATE(date) = CURDATE()";
        PreparedStatement ps = connexion.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getDouble("total_jour");
        }
        System.out.println("Succès");

        return 0.0;
    }


    public Map<String, Double> getVentesParHeureAujourdhui() throws SQLException {
        Map<String, Double> data = new LinkedHashMap<>();
        String sql = "SELECT HOUR(date) AS heure, SUM(montant) AS total " +
                    "FROM vente WHERE DATE(date) = CURDATE() GROUP BY HOUR(date)";
        PreparedStatement ps = connexion.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            data.put(rs.getString("heure") + "h", rs.getDouble("total"));
        }
        return data;
    }

    public Map<String, Double> getVentesParJourSemaine() throws SQLException {
        Map<String, Double> data = new LinkedHashMap<>();
        String sql = "SELECT DATE(date) AS jour, SUM(montant) AS total " +
                    "FROM vente WHERE YEARWEEK(date, 1) = YEARWEEK(CURDATE(), 1) " +
                    "GROUP BY DATE(date)";
        PreparedStatement ps = connexion.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            data.put(rs.getString("jour"), rs.getDouble("total"));
        }
        return data;
    }

    public Map<String, Double> getVentesParMois() throws SQLException {
        Map<String, Double> data = new LinkedHashMap<>();
        String sql = "SELECT MONTH(date) AS mois, SUM(montant) AS total " +
                    "FROM vente WHERE YEAR(date) = YEAR(CURDATE()) GROUP BY MONTH(date)";
        PreparedStatement ps = connexion.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            data.put(String.valueOf(rs.getInt("mois")), rs.getDouble("total"));
        }
        return data;
    }

    public Map<String, Double> getVentesParAnnee() throws SQLException {
        Map<String, Double> data = new LinkedHashMap<>();
        String sql = "SELECT YEAR(date) AS annee, SUM(montant) AS total " +
                    "FROM vente GROUP BY YEAR(date)";
        PreparedStatement ps = connexion.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            data.put(String.valueOf(rs.getInt("annee")), rs.getDouble("total"));
        }
        return data;
    }


    public List<LigneVente> obtenirLignesVente(int venteId) throws SQLException {
        List<LigneVente> lignes = new ArrayList<>();
        String sql = "SELECT lv.id, lv.quantite, lv.prix, lv.total, " +
                    "p.id as produit_id, p.reference as reference ,p.nom as produit_nom " +
                    "FROM ligne_vente lv " +
                    "JOIN produit p ON lv.produit_id = p.id " +
                    "WHERE lv.vente_id = ?";
        PreparedStatement ps = connexion.prepareStatement(sql);
        ps.setInt(1, venteId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            LigneVente ligne = new LigneVente();
            ligne.setQuantite(rs.getInt("quantite"));
            ligne.setPrix(rs.getDouble("prix"));
            ligne.setTotal(rs.getDouble("total"));

            Produit produit = new Produit();
            produit.setReference(rs.getString("reference"));
            produit.setNom(rs.getString("produit_nom"));
            ligne.setProduit(produit);

            lignes.add(ligne);
        }
        return lignes;
    }


    public Vente obtenirVenteAvecDetails(int venteId) {
        String sql = "SELECT v.id, v.codeVente, v.date, v.montant, v.typeVente, v.quantiteTotal, " +
                "c.id as client_id, c.nom as client_nom, c.telephone, " +
                "o.id as ordonnance_id, o.reference as ordonnance_ref, o.numOrdonnance, o.nomMedecin " +
                "FROM vente v " +
                "LEFT JOIN client c ON v.client_id = c.id " +
                "LEFT JOIN ordonnance o ON v.ordonnance_id = o.id " +
                "WHERE v.id = ?";

        Vente vente = null;

        try (PreparedStatement ps = connexion.prepareStatement(sql)) {
            ps.setInt(1, venteId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vente = new Vente();
                    vente.setCodeVente(rs.getString("codeVente"));
                    vente.setDate(rs.getTimestamp("date"));
                    vente.setMontant(rs.getDouble("montant"));
                    vente.setTypeVente(rs.getString("typeVente"));
                    vente.setQuantiteTotal(rs.getInt("quantiteTotal"));

                    // Client
                    Client client = new Client();
                    client.setNom(rs.getString("client_nom"));
                    client.setTel(rs.getString("telephone"));
                    vente.setClient(client);

                    // Ordonnance (si elle existe)
                    if (rs.getInt("ordonnance_id") != 0) {
                        Ordonnance ordonnance = new Ordonnance();
                        ordonnance.setId(rs.getInt("ordonnance_id"));
                        ordonnance.setReference(rs.getString("ordonnance_ref"));
                        // ⚠ Correction du nom de colonne pour éviter l'erreur SQL
                        ordonnance.setNumOrdonnance(rs.getString("numOrdonnance")); 
                        ordonnance.setNomMedecin(rs.getString("nomMedecin"));
                        vente.setOrdonnance(ordonnance);
                    }
                }
            }

            // Charger les lignes de vente
            if (vente != null) {
                vente.setLigneVente(obtenirLignesVente(venteId));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la vente avec détails : " + e.getMessage());
            e.printStackTrace();
        }

        return vente;
    }



    
}
