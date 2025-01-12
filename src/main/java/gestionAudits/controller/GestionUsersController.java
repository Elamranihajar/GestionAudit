package gestionAudits.controller;

import gestionAudits.data.GestionUsersData;
import gestionAudits.models.User;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GestionUsersController {
    public static boolean ajouterUser(User user) {
        String username = user.getName().replaceAll("\\s+","_").toLowerCase();
        String motpass=generatePassword(username);
        user.setUsername(username);
        user.setPassword(motpass);
        System.out.println(user);
        if(GestionUsersData.insertUser(user)){
            String subject="Parmaétre de connexion";
            String body="les parmaetres de connexion dans votre compte audit : \n"
                        + "login (username) : "+username+
                          "\nmotpass : "+motpass+
                          "\nlors vous modifer votre motpass";
            EmailSender.sendEmail(user.getEmail(),subject,body);
            return true;
        }
        return false;
    }
    public static List<User> getUsers() {
        return GestionUsersData.getUsers();
    }
    public static List<User> listerAudits(){
        return GestionUsersData.getUsers().
                                        stream().
                                        filter(user -> user.getRoleId()==2).
                                        collect(Collectors.toList());
    }
    public static boolean modifierUser(User user) {

        String username = user.getName().replaceAll("\\s+","_").toLowerCase();
        user.setUsername(username);
        System.out.println(user);
        return GestionUsersData.updateUser(user);
    }
    public static boolean supprimerUser(int id) {
        System.out.println(id);
        return GestionUsersData.deleteUser(id);
    }
    private static String generatePassword(String username) {

        Random random = new Random();
        StringBuilder password = new StringBuilder(STR."#\{username}");

        for (int i = 0; i < 8; i++) {
            int digit = random.nextInt(10); // Génère un chiffre entre 0 et 9
            password.append(digit);
        }
        password.append("@");
        return password.toString();
    }
}
