package gestionAudits.controller;

import gestionAudits.models.Preuve;
import gestionAudits.models.SystemeExigence;

import java.util.List;

public class RensgnierStatusController {
    public static boolean ajouterRenseignerStatus(SystemeExigence systemeExigence, List<Preuve> preuves) {
//        systemeExigence.setAuditeur(Session.userConnected);
//        for(Preuve p:preuves)
//            p.setSystemeExigence(systemeExigence);
//        boolean isInsert=GestionSystemExigenceController.modiferSystemExigence(systemeExigence) && RensgnierStatusData.insert(preuves);
//        SerializationToJson<List<SystemeExigence>> serializationSystem=new SerializationToJson<>();
//        SerializationToJson<List<Preuve>> serializationPreuves=new SerializationToJson<>();
//        if(isInsert){
//          boolean isSerializeSys=serializationSystem.serialize(GestionSystemExigenceController.getSystemExigence(Session.userConnected.getId()),"systemExigence.json");
//          boolean isSerializePre=serializationPreuves.serialize(getPreuves(0),"preuves.json");
//          return isSerializeSys && isSerializePre;
//        }
       // System.out.println(systemeExigence.getStatus());
        return GestionSystemExigenceController.modiferSystemExigence(systemeExigence);
    }




}
