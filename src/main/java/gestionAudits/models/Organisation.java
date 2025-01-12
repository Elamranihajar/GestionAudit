package gestionAudits.models;

public class Organisation {
    private String nom;
    private String adresse;
    private int id;

    public Organisation(String nom, String adresse) {
        this.nom = nom;
        this.adresse = adresse;
    }
    public Organisation() {}

    public Organisation(int id, String nom, String adresse) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {this.id=id;}
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public String toString(){
        return "Organisation{" +
                "id : " + getId()+
                "nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                '}';
    }
}
