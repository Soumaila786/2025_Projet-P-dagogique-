package com.projet.models;

public class Utilisateur {
    private int id;
    
    private String nom;
    private String email;
    private String login;
    private String mdp;
    private String role;
    private String diplome;
    private int salaire;
    
    
    // Constructeurs
    public Utilisateur(String nom,String email,String role, int salaire , String login,String mdp ,String diplome) {
        this.nom = nom;
        this.login =login ;
        this.email = email;
        this.mdp = mdp;
        this.role = role;
        this.salaire = salaire;
        this.diplome = diplome;
    }
    

    // Getters & Setters
    public int    getId() {return id;}
    public String getNom() { return nom; }
    public String getLogin() { return login; }
    public String getEmail() { return email; }
    public String getMdp() { return mdp; }
    public String getRole() { return role; }
    public int    getSalaire() {return salaire;}
    public String getDiplome() {return diplome;}
    
    public void setNom(String nom) { this.nom = nom; }
    public void setEmail(String email) { this.email = email; }
    public void setLogin(String login) { this.login = login; }
    public void setMdp(String mdp) { this.mdp = mdp; }
    public void setRole(String role) { this.role = role; }
    public void setSalaire(int salaire) {this.salaire = salaire;}
    public void setDiplome(String diplome) {this.diplome = diplome;}
    
}
