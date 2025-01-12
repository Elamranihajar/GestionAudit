package gestionAudits.models;

public class SystemeManagement {
    private int id;
    private String description;
    private String nom;
    private int nbPersonnes;
    private Organisation organisation;
    private User responsable;

    public SystemeManagement(int id) {
        this.id = id;
    }

    public SystemeManagement() {

    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNbPersonnes() {
        return nbPersonnes;
    }

    public void setNbPersonnes(int nbPersonnes) {
        this.nbPersonnes = nbPersonnes;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public User getResponsable() {
        return responsable;
    }

    public void setResponsable(User responsable) {
        this.responsable = responsable;
    }

}