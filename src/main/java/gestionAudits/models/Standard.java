package gestionAudits.models;

public class Standard {
    private int id;
    private String description;
    private String reference;

    public Standard(int id, String description, String reference) {
        this.id = id;
        this.description = description;
        this.reference = reference;
    }

    public Standard() {

    }

    public String toString(){
        return "Standard{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", reference='" + reference + '\'' +
                '}';
    }
    public Standard(String description, String reference) {
        this.description = description;
        this.reference = reference;
    }

    public int getId() {
        return id;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}