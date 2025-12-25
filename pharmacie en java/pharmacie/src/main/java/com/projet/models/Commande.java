package com.projet.models;

import java.sql.Timestamp;

public class Commande {
    private int id ;
    private String reference;
    private int four_id;
    private Timestamp date;
    private int montant;
    private String status;
    
    public Commande(){}
    public Commande( String reference,Timestamp date, int montant) {
        this.reference = reference;
        this.montant = montant;
        this.date = date;
    }
    
    public String getStatus() { return status;}
    public void setStatus(String status) {this.status = status;}
    public int getId(){ return id;}
    public int getFour_id() { return four_id;}
    public String getReference(){ return reference;}
    public Timestamp getDate(){ return date;}
    public int getMontant() {return montant;}
    
    
    public void setFour_id(int four_id) {this.four_id = four_id;}
    public void setMontant(int montant) {this.montant = montant;}
    public void setReference(String reference) { this.reference = reference;}
    public void setDate(Timestamp date) {this.date = date;}

    

    
	
}

