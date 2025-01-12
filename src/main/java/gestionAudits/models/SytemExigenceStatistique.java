package gestionAudits.models;

public class SytemExigenceStatistique {
    private int auditId;
    private int pourcentageRespectees;
    private int pourcentageNonRespectees;
    private int nbrSytem;

    public SytemExigenceStatistique(int auditId, int pourcentageRespectees, int pourcentageNonRespectees, int nbrSytem) {
        this.auditId = auditId;
        this.pourcentageRespectees = pourcentageRespectees;
        this.pourcentageNonRespectees = pourcentageNonRespectees;
        this.nbrSytem = nbrSytem;
    }

    public SytemExigenceStatistique(){}

    public int getAuditId() {
        return auditId;
    }

    public int getPourcentageRespectees() {
        return pourcentageRespectees;
    }

    public int getPourcentageNonRespectees() {
        return pourcentageNonRespectees;
    }

    public int getNbrSytem() {return nbrSytem;}
    @Override
    public String toString() {
        return "Audit ID: " + auditId + 
               ", Respectées: " + pourcentageRespectees + "%" + 
               ", Non Respectées: " + pourcentageNonRespectees + "%";
    }
}
