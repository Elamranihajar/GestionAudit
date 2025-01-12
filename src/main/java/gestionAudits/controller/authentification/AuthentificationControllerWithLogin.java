package gestionAudits.controller.authentification;

import gestionAudits.data.AuthentificationWithLoginData;
import gestionAudits.models.User;

import java.io.File;

public class AuthentificationControllerWithLogin {
    private String login;
    private String password;
    public static User authUser;
    private AuthentificationWithLoginData authentificationWithLoginData;
    public AuthentificationControllerWithLogin(String login, String password) {
        this.login = login;
        this.password = password;
        authentificationWithLoginData = new AuthentificationWithLoginData();
    }
    public boolean isAuthentificationOk() {
        authUser=authentificationWithLoginData.authenticate(login,password);
        return authUser != null;
    }
    public static boolean deconnexion() {
       // File file = new File("src/main/resources/objects/session.ser");
        File file = new File("src/main/resources/objects/session.json");

        if (file.exists())
            return file.delete();
        return false;
    }

}
