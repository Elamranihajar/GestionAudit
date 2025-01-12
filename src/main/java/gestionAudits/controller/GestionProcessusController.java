package gestionAudits.controller;

import gestionAudits.data.GestionProcessusData;
import gestionAudits.models.Processus;

import java.util.List;

public class GestionProcessusController {

    public static boolean ajouterProcessus(Processus p) {
        return GestionProcessusData.insertProcessus(p);
    }
    public static List<Processus> listerProcessus() {
        return GestionProcessusData.getProcessus();
    }
    public static boolean modifierProcessus(Processus p) {
        return GestionProcessusData.updateProcessus(p);
    }
    public static boolean supprimerProcessus(int id) {
        return GestionProcessusData.deleteProcessus(id);
    }

}
