package gestionAudits.controller;

import gestionAudits.data.GestionAuditsData;
import gestionAudits.data.StatistiqueData;
import gestionAudits.models.Statistiques;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatistiqueController {
    private static GestionSerialisation<Statistiques>  s=new GestionSerialisation<>(Statistiques.class,"statistiques.json");
    public static Statistiques getStatistiques() {


//        List<Statistiques> statistiques=s.chargerDepuisFichierJSON();
//
//
//        if(statistiques!=null) {
//            System.out.println("DD : "+statistiques.size());
//            return statistiques.getFirst();
//        }
        Statistiques statistique=StatistiqueData.getStatistiqueAudits();
        statistique.setNbrActions(StatistiqueData.getStatistiqueAction());
        statistique.setNbrConformNonConformParAudit(StatistiqueData.nbrConformNonConformParAudit());
        statistique.setNbrActionParAudit(StatistiqueData.nbrActionParAudit());

//        statistiques=new ArrayList<>();
//        statistiques.add(statistique);
//
//        s.mettreAJourFichierJSON(statistiques);
        return statistique;
    }
}
