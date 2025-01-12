package gestionAudits.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SerializationToJson<T> {
    private final ObjectMapper objectMapper;
    private final String PATH;

    // Constructeur
    public SerializationToJson() {
        this.objectMapper = new ObjectMapper();
        this.PATH = "src/main/resources/objects/";

        // Configuration pour gérer les types de date/heure Java 8
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    //sérialiser un objet en fichier JSON
    public boolean serialize(T object, String filePath) {
        try {
            objectMapper.writeValue(new File(this.PATH + filePath), object);
            return true;
        } catch (IOException e) {
            System.err.println("Erreur de sérialisation : " + e.getMessage());
            return false;
        }
    }

    //désérialiser un objet depuis un fichier JSON
    public T deserialize(String filePath, Class<T> classe) {
        try {
            return objectMapper.readValue(new File(this.PATH + filePath), classe);
        } catch (IOException e) {
            System.err.println("Erreur de désérialisation : " + e.getMessage());
            return null;
        }
    }

    //désérialiser une liste d'objets depuis un fichier JSON
    public List<T> deserializeList(String filePath, Class<T> classe) {
        try {
            return objectMapper.readValue(
                new File(this.PATH + filePath),
                objectMapper.getTypeFactory().constructCollectionType(List.class, classe)
            );
        } catch (IOException e) {
            System.err.println("Erreur de désérialisation de liste : " + e.getMessage());
            return null;
        }
    }
}
