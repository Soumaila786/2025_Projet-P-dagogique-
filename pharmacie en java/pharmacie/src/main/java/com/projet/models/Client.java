package com.projet.models;

public class Client {
    private int id;
    private String reference;
    private String nom;
    private String telephone;

    public Client(){}
    public Client(int id,String reference, String nom, String telephone) {
        this.id = id;
        this.reference = reference;
        this.nom = nom;
        this.telephone = telephone;
    }

    // Getters / Setters
    public int    getId()  {return id;}
    public String getReference() {return reference;}
    public String getNom() {return nom;}
    public String getTel() {return telephone;}
    
    public void setNom(String nom){this.nom = nom;}
    public void setTel(String telephone){this.telephone =  telephone;}
    public void setReference(String reference) {this.reference = reference;}
}
