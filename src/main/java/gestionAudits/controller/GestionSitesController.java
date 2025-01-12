package gestionAudits.controller;

import gestionAudits.data.GestionSiteData;
import gestionAudits.models.Site;
import gestionAudits.vues.espaceAdmin.gestionSites.GestionSite;

import java.util.List;

public class GestionSitesController {

    private static GestionSerialisation<Site> gestionSerialisation=new GestionSerialisation<>(Site.class,"sites.json");

    public static boolean ajouterSite(Site site) {
        boolean result = GestionSiteData.insertSite(site);
        if(result)
            gestionSerialisation.mettreAJourFichierJSON(GestionSiteData.getSites());
        return result;
    }

    public static boolean modiferSite(Site site) {
        boolean result = GestionSiteData.updateSite(site);
        if(result)
            gestionSerialisation.mettreAJourFichierJSON(GestionSiteData.getSites());
        return result;
    }

    public static List<Site> listerSites() {
        List<Site> sites =gestionSerialisation.chargerDepuisFichierJSON();
        if(sites==null){
            sites=GestionSiteData.getSites();
            gestionSerialisation.mettreAJourFichierJSON(sites);
        }
        return sites;
    }

    public static boolean suppprimerSite(int id) {
        boolean result = GestionSiteData.deleteSite(id);
        if(result)
            gestionSerialisation.mettreAJourFichierJSON(GestionSiteData.getSites());
        return true;
    }


}
