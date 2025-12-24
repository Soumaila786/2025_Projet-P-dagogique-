package com.projet.models;

public class Fournisseur {
    private int id;
    private String reference;
    private String nom;
    private String adresse;
    private String contact;

    public Fournisseur(String reference,String nom, String adresse, String contact) {
        this.reference = reference;
        this.nom = nom;
        this.adresse = adresse;
        this.contact = contact;
    }
    public Fournisseur(){}
    
    // Getters / Setters
    public int    getId(){return id;}
    public String getReference() {return reference;}
    public String getNom(){return nom;}
    public String getAdresse(){return adresse;}
    public String getContact(){ return contact;}

    public void setReference(String reference) { this.reference = reference;}
    public void setNom( String nom){this.nom = nom;}
    public void setAdresse( String adresse){this.adresse = adresse;}
    public void setContact(String contact){ this.contact = contact;}

    @Override
    public String toString() {return this.nom;}

    
}
