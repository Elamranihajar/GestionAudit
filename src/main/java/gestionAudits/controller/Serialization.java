package gestionAudits.controller;

import java.io.*;

public class Serialization<T> {

    private static final String BASE_PATH = "src/main/resources/objects/";

    public void serializeObject(String filename, T object) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BASE_PATH + filename))) {
            oos.writeObject(object);
            System.out.println("Objet sérialisé avec succès dans le fichier : " + BASE_PATH + filename);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sérialisation : " + e.getMessage());
        }
    }

    public T deserializeObject(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BASE_PATH + filename))) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors de la désérialisation : " + e.getMessage());
            return null;
        }
    }
}
