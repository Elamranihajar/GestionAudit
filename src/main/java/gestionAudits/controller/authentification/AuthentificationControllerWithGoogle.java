
package gestionAudits.controller.authentification;

import com.google.api.client.auth.oauth2.*;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;

import java.awt.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;


import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import gestionAudits.data.AuthentificationWithLoginData;
import gestionAudits.models.User;

public class AuthentificationControllerWithGoogle {

    public AuthentificationControllerWithGoogle() throws Exception {
        authenticateWithGoogle();
    }
    private String token;
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private User user;

    public void authenticateWithGoogle() throws Exception {

    }
    public User getInfoUser() {return user;}
    public String getToken() {
        return token;
    }

    private User retrieveUserInfo(Credential credential) throws IOException, GeneralSecurityException {
       return null;
    }

}
