package com.projet.models;

public class LigneVente {
    private int id;
    private String nomProduit;
    private int quantite;
    private double prix;
    private double total;

    private Produit produit;

    public void setProduit(Produit produit) {this.produit = produit;}
    public Produit getProduit() {return produit;}
    public LigneVente(){}
    public LigneVente( String nomProduit, double prix, int quantite,Double total) {
        this.nomProduit = nomProduit;
        this.quantite = quantite;
        this.prix = prix;
        this.total = prix * quantite;
    }

    // Getters et setters
    public int getId() { return id; }
    public String getnomProduit() { return nomProduit; }
    public int getQuantite() { return quantite; }
    public double getPrix() { return prix; }
    public double getTotal() { return total; }

    public void setId(int id) { this.id = id; }
    public void setProduitId(String nomProduit) { this.nomProduit = nomProduit; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public void setPrix(double prix) { this.prix = prix; }
    public void setTotal(double total) { this.total = total; }
}



