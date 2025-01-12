package gestionAudits.controller;

import gestionAudits.data.*;
import gestionAudits.models.Audit;
import gestionAudits.models.ClauseStandard;
import gestionAudits.models.SystemeExigence;

import java.util.List;

public class GestionSystemExigenceController {
    public static boolean ajouterSystemExigence(SystemeExigence systemeExigence) {
        int id = GestionAutreExigenceData.insert(systemeExigence.getAutreExigence());
        systemeExigence.getAutreExigence().setId(id);
        if (id!=-1)
            return GestionSystemExigenceData.insert(systemeExigence);
        return false;
    }
    public static List<SystemeExigence> getSystemExigence(Audit audit) {
        List<SystemeExigence> list=GestionSystemExigenceData.selectAll(audit.getId());
        for (SystemeExigence systemeExigence : list) {
            int id=systemeExigence.getAuditeur().getId(); //idAuditeur
            systemeExigence.setAuditeur(GestionUsersData.getUser(id));
            id=systemeExigence.getAutreExigence().getId(); //idAutreExigence
            if (id!=0)
                systemeExigence.setAutreExigence(GestionAutreExigenceData.get(id));
            id=systemeExigence.getClauseStandard().getId();//idClauseStandard
            ClauseStandard cs=GestionClauseStandartData.getClauseStandard(id);
            if (cs==null)
                cs=new ClauseStandard();
            systemeExigence.setClauseStandard(cs);
            id=systemeExigence.getSystemeManagement().getId();
            systemeExigence.setSystemeManagement(GestionSystemManagementData.getSystemManagement(id));
        }
        return list;
    }

    public static boolean modiferSystemExigence(SystemeExigence systemeExigence) {
        System.out.println(systemeExigence.getAutreExigence().getId());
        int resultat=GestionAutreExigenceData.insert(systemeExigence.getAutreExigence());
        if(resultat > 0) // le cas ou syteme exigence ajouter autre exigence (resultat est l'id d'exgence ajouter)
            systemeExigence.getAutreExigence().setId(resultat);
        return GestionSystemExigenceData.update(systemeExigence);
    }

    public static boolean supprimer(SystemeExigence systemeExigences) {
        return GestionSystemExigenceData.delete(systemeExigences.getId());
    }

    public static List<SystemeExigence> getSystemExigence(int idAuditeur) {
        List<SystemeExigence> list=GestionSystemExigenceData.get(idAuditeur);
        for (SystemeExigence systemeExigence : list) {
            int id=systemeExigence.getAudit().getId(); //idAudit
            systemeExigence.setAudit(GestionAuditsData.getAudit(id));
            id=systemeExigence.getAutreExigence().getId(); //idAutreExigence
            if (id!=0)
                systemeExigence.setAutreExigence(GestionAutreExigenceData.get(id));
            id=systemeExigence.getClauseStandard().getId();//idClauseStandard
            systemeExigence.setClauseStandard(GestionClauseStandartData.getClauseStandard(id));
            id=systemeExigence.getSystemeManagement().getId();
            systemeExigence.setSystemeManagement(GestionSystemManagementData.getSystemManagement(id));
        }
        return list;
    }
}
