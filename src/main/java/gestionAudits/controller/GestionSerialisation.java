package gestionAudits.controller;

import java.io.File;
import java.util.List;

public class GestionSerialisation <T> {
    private final Class<T> type; // Type de classe géré
    private final String fichierJson; // Chemin du fichier JSON

    public GestionSerialisation(Class<T> type, String fichierJson) {
        this.type = type;
        this.fichierJson = fichierJson;
    }

    public void mettreAJourFichierJSON(List<T> data) {
        File fichier = new File(fichierJson);
        if (fichier.exists() && !fichier.delete()) {
            System.err.println("Erreur : impossible de supprimer l'ancien fichier " + fichierJson);
            return;
        }

        if (data == null) {
            System.err.println("Erreur : données nulles");
            return;
        }

        SerializationToJson serializationToJson = new SerializationToJson();
        boolean success = serializationToJson.serialize(data, fichierJson);
        if (!success) {
            System.err.println("Erreur : échec de la sérialisation.");
        }
    }

    /**
     * Charge les objets depuis le fichier JSON.
     */
    public List<T> chargerDepuisFichierJSON() {
        File fichier = new File("src/main/resources/objects/"+fichierJson);
        if (!fichier.exists()) {
            return null;
        }

        SerializationToJson serializationToJson = new SerializationToJson();
        return serializationToJson.deserializeList(fichierJson, type);
    }


}
