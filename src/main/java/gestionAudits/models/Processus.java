package gestionAudits.models;

public class Processus {
    private int id;
    private String name;
    private String description;
    private Organisation organization;
    private User responsable;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Organisation getOrganization() {
        return organization;
    }

    public void setOrganization(Organisation organization) {
        this.organization = organization;
    }

    public User getResponsable() {
        return responsable;
    }

    public void setResponsable(User responsable) {
        this.responsable = responsable;
    }

    @Override
    public String toString() {
        return "Processus{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", organization=" + organization +
                ", responsable=" + responsable +
                '}';
    }
}