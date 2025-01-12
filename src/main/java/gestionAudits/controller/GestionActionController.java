package gestionAudits.controller;

import gestionAudits.data.GestionActionData;
import gestionAudits.models.Action;
import gestionAudits.models.ActionIntervenant;
import gestionAudits.models.SystemeExigence;

import java.util.ArrayList;
import java.util.List;

public class GestionActionController {

    public static boolean ajouterAction(Action action,List<ActionIntervenant> actionIntervenant) {

        int resultat=GestionActionData.insertAction(action);
        action.setId(resultat);
        actionIntervenant.forEach(a->a.setAction(action));
        if(resultat!=-1){
            return GestionActionData.insertActionIntervenant(actionIntervenant);
        }
        return false;
    }
    public static List<Action> listerActions(int idSystemExigence) {
        return GestionActionData.get(idSystemExigence);
    }
    public static List<ActionIntervenant> listerActionIntervenants(int idAction) {
        return GestionActionData.getActionIntervenants(idAction);
    }
    public static boolean modifierAction(Action action) {
        return GestionActionData.update(action);
    }
    public static boolean supprimerAction(int id) {
        return GestionActionData.deleteActionIntervenantByAction(id) && GestionActionData.delete(id);
    }

    public static boolean supprimerActionIntervenant(int id) {
        return GestionActionData.deleteActionIntervenant(id);
    }

    public static boolean ajouterActionIntervenant(ActionIntervenant actionIntervenant) {
        List<ActionIntervenant>  list=new ArrayList<>();
        list.add(actionIntervenant);
        return GestionActionData.insertActionIntervenant(list);
    }
}
