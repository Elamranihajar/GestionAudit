package gestionAudits.models;

public class Clause {
    private int id;
    private String description;
    private String reference;

    public Clause(int id, String description, String reference) {
        this.id = id;
        this.description = description;
        this.reference = reference;
    }
    public String toString(){
        return "Clause{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", reference='" + reference + '\'' +
                '}';
    }
    public Clause(String description, String reference) {
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
// Getters et Setters
}