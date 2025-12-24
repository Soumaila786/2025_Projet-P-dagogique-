package com.projet.models;

public class MaterielMedical extends Produit {
    private  String typeMateriel;

    public MaterielMedical(int id, String nom, String reference, String typeMateriel , double prix, int quantite) {
        //super(id, nom, reference, prix, quantite);
        this.typeMateriel = typeMateriel ;
    }

    public String getTypeMateriel(){ return typeMateriel; }
    public void setTypeMateriel(String typeMateriel){ this.typeMateriel = typeMateriel; }
    
}
