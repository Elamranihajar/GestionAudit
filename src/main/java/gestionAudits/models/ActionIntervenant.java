package gestionAudits.models;

public class ActionIntervenant {
    private int id;
    private Action action;
    private User intervenant;
    private String role;

    public int getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public User getIntervenant() {
        return intervenant;
    }

    public void setIntervenant(User intervenant) {
        this.intervenant = intervenant;
    }

    @Override
    public String toString() {
        return "ActionIntervenant{" +
                "id=" + id +
                ", action=" + action +
                ", intervenant=" + intervenant +
                ", role='" + role + '\'' +
                '}';
    }

    // Getters et Setters
}