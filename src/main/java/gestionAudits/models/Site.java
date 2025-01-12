package gestionAudits.models;

import com.google.api.services.people.v1.model.Organization;

public class Site {
    private int id;
    private String name;
    private String address;
    private String description;
    private Organisation organisation;

    public Site(int id,String name,String address, String description, Organisation organisation) {
        this.name = name;
        this.id = id;
        this.address = address;
        this.description = description;
        this.organisation = organisation;
    }

    public Site(String name, String address, String description, Organisation organization) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.organisation = organization;
    }

    public Site(){}

    @Override
    public String toString() {
        return "Site{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", organisation=" + organisation +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Organisation getOrganization() {
        return organisation;
    }

    public void setOrganization(Organisation organization) {
        this.organisation = organization;
    }
}