package gestionAudits.controller;

import gestionAudits.data.GestionPreuvesData;
import gestionAudits.models.Preuve;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestionPreuvsController {

    private static GestionSerialisation<Preuve> gestSerialisation=new GestionSerialisation(Preuve.class,"preuvs.json");

    public static boolean addPreuve(Preuve preuve) {
        List<Preuve> preuves=new ArrayList<>();
        preuves.add(preuve);
        boolean resultat=GestionPreuvesData.insert(preuves);
        if(resultat)
            gestSerialisation.mettreAJourFichierJSON(GestionPreuvesData.getPreuveList(0));
        return resultat;
    }

    public static List<Preuve> getPreuves(int idSysteme) {
        List<Preuve> preuves=gestSerialisation.chargerDepuisFichierJSON();
        if (preuves==null){
            preuves=GestionPreuvesData.getPreuveList(idSysteme);
            gestSerialisation.mettreAJourFichierJSON(GestionPreuvesData.getPreuveList(0));
        }
        if(idSysteme==0)
            return preuves;
        return preuves.stream()
                        .filter(p->p.getSystemeExigence().getId()==idSysteme)
                        .collect(Collectors.toList());
    }
    public static boolean supprimerPreuve(Preuve preuve) {
        boolean resultat=GestionPreuvesData.deletePreuve(preuve.getId());;
        if(resultat)
            gestSerialisation.mettreAJourFichierJSON(GestionPreuvesData.getPreuveList(0));
        return resultat;
    }

}
