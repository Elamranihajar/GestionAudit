package gestionAudits.data;

import gestionAudits.models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class AuthentificationWithLoginData {
    public User authenticate(String username, String password) {
        String query = "SELECT * FROM user WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("motpass");
                if (BCrypt.checkpw(password, storedPassword)) {
                    User user = new User();
                    return new User(rs.getInt("id"),
                                    rs.getString("username"),
                                    rs.getString("email"),
                                    rs.getString("name"),
                                    rs.getInt("role_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Utilisateur non trouvé ou mot de passe incorrect
    }

    // Méthode pour enregistrer un utilisateur
    public boolean register(String username, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String query = "INSERT INTO user (username, password) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            int rowsInserted = stmt.executeUpdate();

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getRole(String email) {
        String query = "SELECT role_id FROM user WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("role_id"); // Récupérer le rôle de l'utilisateur
            } else {
                return 0; // Aucun résultat trouvé
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0; // En cas d'erreur, retourner 0
        }
    }
    public static User get(String email) {
        String query = "SELECT `id`, `username`,`name`, `email`, `domain`, `role_id`" +
                        " FROM user WHERE email = ?";

        User user = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setDomain(rs.getString("domain"));
                user.setRoleId(rs.getInt("role_id"));
                user.setName(rs.getString("name"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }
}
