package com.projet.models;


public class Ordonnance {
    private int id;
    private String reference;
    private String numOrdonnance;
    private String nomMedecin;
    private java.sql.Date date;

    private int client_id;

    public Ordonnance(){    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getNumOrdonnance() { return numOrdonnance; }
    public void setNumOrdonnance(String numOrdonnance) { this.numOrdonnance = numOrdonnance; }

    public String getNomMedecin() { return nomMedecin; }
    public void setNomMedecin(String nomMedecin) { this.nomMedecin = nomMedecin; }

    public java.sql.Date getDate() { return date; }
    public void setDate(java.sql.Date date) { this.date = date; }

    public int getIdClient() { return client_id; }
    public void setIdClient( int client_id) { this.client_id = client_id; }
}