package gestionAudits;

import gestionAudits.controller.GestionSerialisation;
import gestionAudits.controller.RensgnierStatusController;
import gestionAudits.controller.SerializationToJson;
import gestionAudits.data.StatistiqueData;
import gestionAudits.models.*;
import gestionAudits.vues.MainPage;
import gestionAudits.vues.authentification.Auth;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        SerializationToJson<Session> serialization=new SerializationToJson<>();
        Session session = serialization.deserialize("session.json", Session.class);

        System.out.println(session);
        if(session!=null){
            if(session.isActive())
            {
                Session.userConnected=session.getUser();
                System.out.println("session active");
                System.out.println(session);
                SwingUtilities.invokeLater(() -> {
                    new MainPage().setVisible(true);
                });
            }
            else
                SwingUtilities.invokeLater(Auth::new);
        }else {
           SwingUtilities.invokeLater(Auth::new);
        }
    }
}