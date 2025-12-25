package com.projet.models;
import java.util.Date;

public class Medicament extends Produit {

    private String dose;
    private Date datePeremption;

    public Medicament(int id, String nom, String reference, String dose, double prix, int quantite, Date datePeremption) {
        //super(id, nom, reference, prix, quantite);
        this.dose = dose ;
        this.datePeremption = datePeremption;
    }


    public String getDose(){ return dose; }
    public Date   getDatePeremption(){ return datePeremption; }
    public void   setDose(String dose){ this.dose = dose; }
    public void   setType(Date datePeremption){ this.datePeremption = datePeremption; }
    
}
