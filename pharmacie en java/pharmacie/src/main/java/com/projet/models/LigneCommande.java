package com.projet.models;

public class LigneCommande {
    private int id;
    private String nomProduit;
    private int commande_id;
    private int quantite;
    private double prixAchat;
    private String type;
    private double sousTotal;
    
    public LigneCommande(){}
    public LigneCommande( int commande_id,String nomProduit, int quantite, double prixAchat,String type) {
        this.commande_id = commande_id;
        this.nomProduit = nomProduit;
        this.quantite = quantite;
        this.prixAchat = prixAchat;
        this.type = type;
    }
    
    // Getters / Setters
    public int    getId() { return id;}
    public int    getCommande_id() {  return commande_id;}
    public String getNomProduit() {return nomProduit;}
    public int    getQuantite() { return quantite; }
    public double getPrixAchat() { return prixAchat; }
    public double getSousTotal() { return prixAchat*quantite;}
    
    public String getType() {return type;}
    public void setCommande_id(int commande_id) {this.commande_id = commande_id;}
    public void setNomProduit(String nomProduit) {this.nomProduit = nomProduit; }
    public void setQuantite(int quantite) {this.quantite = quantite;}
    public void setPrixAchat(double prixAchat) {this.prixAchat = prixAchat;}
    public void setType(String type) {this.type = type;}
    public void setSousTotal(double sousTotal) {this.sousTotal = sousTotal;}
    
}
