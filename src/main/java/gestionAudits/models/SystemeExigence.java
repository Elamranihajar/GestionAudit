package gestionAudits.models;

public class SystemeExigence {
    private int id;
    private Audit audit;
    private User auditeur;
    private AutreExigence autreExigence;
    private ClauseStandard clauseStandard;
    private boolean exclu;
    private String motifExclusion;
    private String status;
    private SystemeManagement systemeManagement;
    private String constats;
    private String ecarts;

    public SystemeExigence(int id,String status,boolean exclu,String motifExclusion) {
        this.id = id;
        this.status = status;
        this.exclu = exclu;
        this.motifExclusion = motifExclusion;
    }

    public SystemeExigence() {

    }

    public SystemeExigence(int id) {
        this.id = id;
    }

    public String getConstats() {
        return constats;
    }

    public void setConstats(String constats) {
        this.constats = constats;
    }

    public String getEcarts() {
        return ecarts;
    }

    public void setEcarts(String ecarts) {
        this.ecarts = ecarts;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "SystemeExigence "+this.id ;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public User getAuditeur() {
        return auditeur;
    }

    public void setAuditeur(User auditeur) {
        this.auditeur = auditeur;
    }

    public AutreExigence getAutreExigence() {
        return autreExigence;
    }

    public void setAutreExigence(AutreExigence autreExigence) {
        this.autreExigence = autreExigence;
    }

    public ClauseStandard getClauseStandard() {
        return clauseStandard;
    }

    public void setClauseStandard(ClauseStandard clauseStandard) {
        this.clauseStandard = clauseStandard;
    }

    public boolean isExclu() {
        return exclu;
    }

    public void setExclu(boolean exclu) {
        this.exclu = exclu;
    }

    public String getMotifExclusion() {
        return motifExclusion;
    }

    public void setMotifExclusion(String motifExclusion) {
        this.motifExclusion = motifExclusion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SystemeManagement getSystemeManagement() {
        return systemeManagement;
    }

    public void setSystemeManagement(SystemeManagement systemeManagement) {
        this.systemeManagement = systemeManagement;
    }
}