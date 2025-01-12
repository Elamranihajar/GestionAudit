package gestionAudits.models;

import java.util.List;

public class Statistiques {
    private int nbrAudits;
    private int nbrActions;
    private int nbrAuditsEnCours;
    private int nbrAuditsPlanifer;
    private int nbrAuditsTerminer;
    private List<SytemExigenceStatistique> nbrConformNonConformParAudit;
    private List<ActionStatistique> nbrActionParAudit;

    public int getNbrAudits() {
        return nbrAudits;
    }

    public void setNbrAudits(int nbrAudits) {
        this.nbrAudits = nbrAudits;
    }

    public int getNbrActions() {
        return nbrActions;
    }

    public void setNbrActions(int nbrActions) {
        this.nbrActions = nbrActions;
    }

    public int getNbrAuditsEnCours() {
        return nbrAuditsEnCours;
    }

    public void setNbrAuditsEnCours(int nbrAuditsEnCours) {
        this.nbrAuditsEnCours = nbrAuditsEnCours;
    }

    public int getNbrAuditsPlanifer() {
        return nbrAuditsPlanifer;
    }

    public void setNbrAuditsPlanifer(int nbrAuditsPlanifer) {
        this.nbrAuditsPlanifer = nbrAuditsPlanifer;
    }

    public int getNbrAuditsTerminer() {
        return nbrAuditsTerminer;
    }

    public void setNbrAuditsTerminer(int nbrAuditsTerminer) {
        this.nbrAuditsTerminer = nbrAuditsTerminer;
    }

    public List<SytemExigenceStatistique> getNbrConformNonConformParAudit() {
        return nbrConformNonConformParAudit;
    }

    public void setNbrConformNonConformParAudit(List<SytemExigenceStatistique> nbrConformNonConformParAudit) {
        this.nbrConformNonConformParAudit = nbrConformNonConformParAudit;
    }

    public List<ActionStatistique> getNbrActionParAudit() {
        return nbrActionParAudit;
    }

    public void setNbrActionParAudit(List<ActionStatistique> nbrActionParAudit) {
        this.nbrActionParAudit = nbrActionParAudit;
    }

    @Override
    public String toString() {
        return "Statistiques{" +
                "nbrAudits=" + nbrAudits +
                ", nbrActions=" + nbrActions +
                ", nbrAuditsEnCours=" + nbrAuditsEnCours +
                ", nbrAuditsPlanifer=" + nbrAuditsPlanifer +
                ", nbrAuditsTerminer=" + nbrAuditsTerminer +
                '}';
    }
}
