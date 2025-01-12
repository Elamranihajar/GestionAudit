package gestionAudits.models;

public class ClauseStandard {
    private int id;
    private Standard standard;
    private Clause clause;

    public ClauseStandard(int id, Standard standard, Clause clause) {
        this.id = id;
        this.standard = standard;
        this.clause = clause;
    }

    public ClauseStandard() {
    }

    public String toString(){
        return clause.getDescription()+" / "+standard.getDescription();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Standard getStandard() {
        return standard;
    }

    public void setStandard(Standard standard) {
        this.standard = standard;
    }

    public Clause getClause() {
        return clause;
    }

    public void setClause(Clause clause) {
        this.clause = clause;
    }
// Getters et Setters
}