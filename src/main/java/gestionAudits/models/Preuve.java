package gestionAudits.models;

public class Preuve {
    private int id;
    private String name;
    private String url;
    private SystemeExigence systemeExigence;

    public Preuve(String name, String url,SystemeExigence systemeExigence) {
        this.name = name;
        this.url = url;
        this.systemeExigence = systemeExigence;
    }
    public Preuve() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SystemeExigence getSystemeExigence() {
        return systemeExigence;
    }

    public void setSystemeExigence(SystemeExigence systemeExigence) {
        this.systemeExigence = systemeExigence;
    }

    @Override
    public String toString() {
        return "Preuve{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", systemeExigence=" + systemeExigence +
                '}';
    }
}