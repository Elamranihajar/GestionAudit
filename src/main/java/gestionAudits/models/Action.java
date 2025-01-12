package gestionAudits.models;

import java.sql.Date;

public class Action {
    private int id;
    private String nom;
    private Date dateDebut;
    private Date dateFin;
    private int dependance;
    private String status;
    private String description;
    private Date dateDebutPrevue;
    private Date dateFinPrevue;
    private SystemeExigence systemeExigence;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public int getDependance() {
        return dependance;
    }

    public void setDependance(int dependance) {
        this.dependance = dependance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateDebutPrevue() {
        return dateDebutPrevue;
    }

    public void setDateDebutPrevue(Date dateDebutPrevue) {
        this.dateDebutPrevue = dateDebutPrevue;
    }

    public Date getDateFinPrevue() {
        return dateFinPrevue;
    }

    public void setDateFinPrevue(Date dateFinPrevue) {
        this.dateFinPrevue = dateFinPrevue;
    }

    public SystemeExigence getSystemeExigence() {
        return systemeExigence;
    }

    public void setSystemeExigence(SystemeExigence systemeExigence) {
        this.systemeExigence = systemeExigence;
    }

    // Getters et Setters
}
