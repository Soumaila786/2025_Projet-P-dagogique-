package com.projet.models;

import java.util.Date;

public class Facture {
    private int id;
    private int reference;
    private Date dateFacture;
    private double remise; // en pourcentage (ex: 10%)
    private  String modePaiement;
    
    
    public Facture(int id , int reference ,double remise, String modePaiement)
    {
        this.id = id;
        this.reference = reference;
        this.dateFacture = new Date();
        this.remise = remise;
        this.modePaiement = modePaiement;
    }
    

    //getters et setters
    public int getId() {return id;}
    public int getReference() {return reference;}
    public String getModePaiement() {return modePaiement;}
    public double getRemise() { return remise;}
    public Date getDateFacture() {return dateFacture;}
    
    public void setReference(int reference) {this.reference = reference;}
    public void setRemise(double remise) {this.remise = remise;}
    public void setModePaiement(String modePaiement) {this.modePaiement = modePaiement;}
    public void setDateFacture(Date dateFacture) {this.dateFacture = dateFacture;}
}
