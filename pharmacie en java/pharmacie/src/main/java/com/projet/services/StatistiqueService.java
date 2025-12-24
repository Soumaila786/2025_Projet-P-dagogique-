package com.projet.services;

import java.sql.Connection;

public class StatistiqueService {
    private Connection connexion;

    public StatistiqueService(Connection connexion){
        this.connexion = connexion;
    }
}
