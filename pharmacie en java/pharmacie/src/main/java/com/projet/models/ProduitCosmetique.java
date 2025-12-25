package com.projet.models;

public class ProduitCosmetique extends Produit {
    private String type ;

    public ProduitCosmetique(int id, String nom, String reference,String type, double prix, int quantite) {
       // super(id, nom, reference, prix, quantite);
        this.type = type ;
    }

    public String getType(){ return type; }
    public void   setType(String type){ this.type = type; }
    
    
}
