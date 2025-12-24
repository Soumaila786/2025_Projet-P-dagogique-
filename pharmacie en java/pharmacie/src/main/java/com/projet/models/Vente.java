package com.projet.models;

import java.sql.Timestamp;
import java.util.List;


public class Vente {
    private int id ;
    private String codeVente;
    private Timestamp date;
    private double montant;
    private String typeVente;
    private int quantiteTotal;

    // Relations
    private int client_id;        // FK -> Client
    private int ordonnance_id; // FK -> Ordonnance (nullable)
    
    private Client client;
    private Ordonnance ordonnance;
    private List<LigneVente> ligne;
    
    public Vente(){}
    public Vente( String codeVente,Timestamp date,double montant,String typeVente,int quantiteTotal) {
        this.codeVente = codeVente;
        this.montant = montant;
        this.quantiteTotal = quantiteTotal;
        this.typeVente = typeVente;
        this.date = date;
    }
    
    public int getId(){ return id;}
    public String getcodeVente(){ return codeVente;}
    public double getMontant() {return montant; }
    public Timestamp getDate(){ return date;}
    public String getTypeVente() {return typeVente;}
    public int getQuantiteTotal() {return quantiteTotal;}

    public int getIdClient() { return client_id;  }
    public int getIdOrdonnance() { return ordonnance_id; }
    
    public Client getClient() {  return client;  }
    public Ordonnance getOrdonnance() { return ordonnance;}
    public List<LigneVente> getLigne() {return ligne;}

    public void setLigneVente(List<LigneVente> ligne) {this.ligne = ligne; }
    public void setClient(Client client) { this.client = client;}
    public void setOrdonnance(Ordonnance ordonnance) {this.ordonnance = ordonnance;}
    
    public void setQuantiteTotal(int quantiteTotal) {this.quantiteTotal = quantiteTotal;}
    public void setTypeVente(String typeVente) { this.typeVente = typeVente; }
    public void setCodeVente(String codeVente) { this.codeVente = codeVente;}
    public void setMontant(double montant) {this.montant = montant;}
    public void setDate(Timestamp date) {this.date = date;}

    public void setIdClient(int client_id) { this.client_id = client_id; }
    public void setIdOrdonnance(int ordonnance_id) { this.ordonnance_id = ordonnance_id;}

    }

    

    
	


