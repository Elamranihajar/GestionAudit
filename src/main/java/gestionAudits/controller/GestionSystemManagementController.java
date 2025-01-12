package gestionAudits.controller;

import gestionAudits.data.GestionSystemManagementData;
import gestionAudits.models.SystemeManagement;

import java.util.List;

public class GestionSystemManagementController {

    public static List<SystemeManagement> listeSystemeManagements(){
        return GestionSystemManagementData.getSystemManagement();
    }
    public static boolean ajouterSystem(SystemeManagement systemeManagement) {
        return GestionSystemManagementData.insertSystem(systemeManagement);
    }
    public static boolean modifierSystem(SystemeManagement systemeManagement) {
        return GestionSystemManagementData.updateSytemMangement(systemeManagement);
    }
    public static boolean supprimerSysteme(int id) {
        return GestionSystemManagementData.deleteSystem(id);
    }

}
