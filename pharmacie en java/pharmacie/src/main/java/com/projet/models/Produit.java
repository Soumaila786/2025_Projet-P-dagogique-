package com.projet.models;

import java.sql.Date;

public class Produit {
    private int id;
    private String reference;
    private String nom;
    private double prix;
    private int quantite;
    private String type;
    private Date date;
    
    
    public Produit( String reference , String nom ,String type ,int quantite ,  double prix,Date date) {
        this.reference = reference;
        this.nom = nom;
        this.prix = prix;
        this.date = date;
        this.type = type;
        this.quantite = quantite;
    }
    
    public Produit(){};


    
    public void vendre(int quantite) {
        if (quantite >= quantite) {
            quantite -= quantite;
        } else {
            System.out.println("quantite insuffisant !");
        }
    }
    
    // Getters / Setters
    public int  getId(){return id;}
    public String getReference(){return reference;}
    public String  getNom(){return nom;}
    public double getPrix() {return prix;}
    public Date getDate() { return date;}
    public int  getQuantite(){return quantite;}
    public String getType() { return type;}
    
    public void setReference(String reference ){this.reference = reference;}
    public void  setNom(String nom){ this.nom = nom; }
    public void setPrix(double prix) {this.prix = prix;}
    public void  setQuantite(int quantite){ this.quantite = quantite; }
    public void setType(String type) { this.type = type; }
    public void setDate(Date date) {this.date = date; }
}