package gestionAudits.controller;

import gestionAudits.data.GestionAuditsData;
import gestionAudits.models.Audit;

import java.util.ArrayList;
import java.util.List;

public class GestionAuditsController {

    private static GestionSerialisation<Audit> gestionSerialisation=new GestionSerialisation<>(Audit.class,"audits.json");

    public static List<Audit> listAudits() {
        List<Audit> audits=gestionSerialisation.chargerDepuisFichierJSON();
        if(audits==null){
            audits=GestionAuditsData.getAudits();
            gestionSerialisation.mettreAJourFichierJSON(audits);
        }
        return audits;
    }

    public static boolean ajouterAudit(Audit audit){
        boolean resultat=GestionAuditsData.insertAudit(audit);
        if(resultat)
            gestionSerialisation.mettreAJourFichierJSON(GestionAuditsData.getAudits());
        return resultat;
    }
    public static boolean modifierAudit(Audit audit){
        if(GestionAuditsData.updateAudit(audit)) {
            gestionSerialisation.mettreAJourFichierJSON(GestionAuditsData.getAudits());
            return true;
        }
        return false;
    }
    public static boolean supprimerAudit(int id){
        if(GestionAuditsData.deleteAudit(id)) {
            gestionSerialisation.mettreAJourFichierJSON(GestionAuditsData.getAudits());
            return true;
        }
        return false;
    }

}
