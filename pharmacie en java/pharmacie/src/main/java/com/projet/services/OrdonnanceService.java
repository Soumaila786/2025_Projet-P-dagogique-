package com.projet.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.projet.models.Ordonnance;

public class OrdonnanceService {
    private Connection connexion;
    public OrdonnanceService(Connection connexion){
        this.connexion =connexion;
    }
    
    /** Ajouter une vente dans la table ordonnance et retourner l'ID généré **/
    public int ajouterOrdonnance(Ordonnance ordonnance) throws SQLException {
        String sql = "INSERT INTO ordonnance(client_id, reference, numOrdonnance ,nomMedecin) VALUES( ? ,? , ?, ? )";
        PreparedStatement ps = connexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setInt(1, ordonnance.getIdClient());
        ps.setString(2, ordonnance.getReference());
        ps.setString(3, ordonnance.getNumOrdonnance());
        ps.setString(4, ordonnance.getNomMedecin());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1); // retourne l'ID de la vente
        }
        return -1;
    }

    public String genererReference() {
        String sql = "SELECT COUNT(*) FROM ordonnance";
        try {
            Statement st = connexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                int count = rs.getInt(1) + 1;
                return String.format("ORD%04d", count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "ORD0001";
    }
}
