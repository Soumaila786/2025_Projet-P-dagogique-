package com.projet.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/pharmamanager";
    private static final String USER = "root" ;
    private static final String PASSWORD = "" ;
    private static Connection connexion ;

    public static Connection getConnection(){
        if (connexion  == null ){
            try{
                connexion = DriverManager.getConnection(URL, USER, PASSWORD) ; 
                System.out.println("Connexion reussie.");
            }catch(SQLException e ){
                System.out.println("Erreur de connexion " + e.getMessage() );
            }
        }
        return connexion;
    }
}
