package gestionAudits.controller;


import gestionAudits.data.GestionOrganisationData;
import gestionAudits.models.Organisation;

import java.io.File;
import java.util.List;

public class GestionOrganisationController {
    Organisation organisation;
    private static GestionSerialisation<Organisation> gestionSerialisation=new GestionSerialisation<>(Organisation.class,"organisations.json");
    public GestionOrganisationController(Organisation organisation) {
        this.organisation = organisation;
    }
    public static boolean ajouterOrganisation(Organisation organisation){
        boolean result = GestionOrganisationData.inserterOrganisation(organisation);
        if(result){
           gestionSerialisation.mettreAJourFichierJSON(GestionOrganisationData.listerOrganisation());
        }
        return result;
    }
    public static boolean supprimerOrganisation(int id){
        boolean resultat=GestionOrganisationData.deleteOrganisation(id);
        if(resultat){
            gestionSerialisation.mettreAJourFichierJSON(GestionOrganisationData.listerOrganisation());
        }
       return resultat;
    }
    public static boolean modifierOrganisation(Organisation organisation){
        boolean resultat=GestionOrganisationData.updateOrganisation(organisation);
        if(resultat){
           gestionSerialisation.mettreAJourFichierJSON(GestionOrganisationData.listerOrganisation());
        }
        return resultat;
    }
    public static List<Organisation> listerOrganisation(){
        List<Organisation> resultat=gestionSerialisation.chargerDepuisFichierJSON();
        if(resultat==null){
            System.out.println("Erreur : listerOrganisation null");
            List<Organisation> list=GestionOrganisationData.listerOrganisation();
            gestionSerialisation.mettreAJourFichierJSON(list);
            return  list;
        }
        return resultat;
    }


}
