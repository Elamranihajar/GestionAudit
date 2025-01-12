package gestionAudits.models;

public class ActionStatistique {
    private int idAudit;
    private int nbrActions;
    private float prcEnCours;
    private float prcTerminer;

    public ActionStatistique(int idAudit, int nbrActions, float prcEnCours, float prcTerminer) {
        this.idAudit = idAudit;
        this.nbrActions = nbrActions;
        this.prcEnCours = prcEnCours;
        this.prcTerminer = prcTerminer;
    }
    public ActionStatistique() {}

    public int getIdAudit() {
        return idAudit;
    }

    public void setIdAudit(int idAudit) {
        this.idAudit = idAudit;
    }

    public int getNbrActions() {
        return nbrActions;
    }

    public void setNbrActions(int nbrActions) {
        this.nbrActions = nbrActions;
    }

    public float getPrcEnCours() {
        return prcEnCours;
    }

    public void setPrcEnCours(float prcEnCours) {
        this.prcEnCours = prcEnCours;
    }

    public float getPrcTerminer() {
        return prcTerminer;
    }

    public void setPrcTerminer(float prcTerminer) {
        this.prcTerminer = prcTerminer;
    }
}
