package gestionAudits.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.LocalDateTime;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Session implements Serializable {
    private static final long serialVersionUID = 1L;

    private User user;              // Utilisateur associé à la session
    private LocalDateTime creationTime; // Heure de création de la session
    public static User userConnected;

    public Session(User user) {
        this.user = user;
        this.creationTime = LocalDateTime.now();
    }

    public Session() {}
    //vérifier si la session est toujours active
    public boolean isActive() {
        // Vérifier si la session a expiré (plus de 24 heures après la création)
        return LocalDateTime.now().isBefore(creationTime.plusHours(24));
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return "Session{" +
                "user=" + user.toString() +
                ", creationTime=" + creationTime +
                ", isActive=" + isActive() +
                '}';
    }
}
