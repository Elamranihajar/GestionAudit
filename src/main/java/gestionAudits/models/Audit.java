package gestionAudits.models;

import java.text.SimpleDateFormat;
import java.sql.Date;

public class Audit {
    private int id;
    private Date dateDebut;
    private Date dateFin;
    private String status;
    private String type;
    private String description;

    public Audit(int id, String description, String type, Date dateDebut, Date dateFin, String status) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.dateDebut=dateDebut;
        this.dateFin=dateFin;
        this.status=status;
    }

    public Audit() {

    }

    public Audit(int auditId) {
        this.id = auditId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
    // Getters et Setters
}