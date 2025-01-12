package gestionAudits.data;

import gestionAudits.models.Organisation;
import gestionAudits.models.Site;
import gestionAudits.models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestionUsersData {

    public static boolean insertUser(User user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        String query = "INSERT INTO user (username, motpass,name,email,role_id) VALUES (?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getEmail());
            stmt.setInt(5, user.getRoleId());
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<User> getUsers() {
        List<User> users = new ArrayList<>();

        String sql = "SELECT id,name,email,role_id FROM `user`";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setRoleId(rs.getInt("role_id"));
                users.add(user);
            }
            return users;
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static User getUser(int id) {
        User user = new User();

        String sql = "SELECT id,name,email,role_id,domain FROM `user` WHERE id=? ";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setRoleId(rs.getInt("role_id"));
                user.setDomain(rs.getString("domain"));
            }
            return user;
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static boolean updateUser(User user) {
        System.out.println("data : "+user);
        String sql ="""  
                UPDATE user 
                SET name=?,email=?,role_id=?,username=?
                WHERE id=?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setInt(3, user.getRoleId());
            stmt.setString(4, user.getUsername());
            stmt.setInt(5, user.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating site: " + e.getMessage());
            return false;
        }
    }
    public static boolean deleteUser(int id) {
        System.out.println("data : "+id);
        String sql ="""  
                UPDATE user 
                SET isArchive=1
                WHERE id=?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating site: " + e.getMessage());
            return false;
        }
    }
}
