package gestionAudits.data;

import gestionAudits.models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GestionActionData {

    public static int insertAction(Action action) {
        String query="INSERT INTO `action` (nom, `date_debut`, `date_fin`, `description`, `systemeexigence_id`) VALUES (?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, action.getNom());
            stmt.setDate(2, action.getDateDebut());
            stmt.setDate(3, action.getDateFin());
            stmt.setString(4, action.getDescription());
            stmt.setInt(5,action.getSystemeExigence().getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                    // Si c'était une insertion, retourner l'ID généré
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1);
                        }
                    }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Retourner -1 en cas d'erreur
        return -1;
    }

    public static boolean insertActionIntervenant(List<ActionIntervenant> actionIntervenants) {
        if (actionIntervenants == null || actionIntervenants.isEmpty()) {
            return false; // Rien à insérer
        }

        String query = "INSERT INTO `actionintervenant`(`action_id`, `intervenant_id`,role) VALUES (?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Ajout des données en batch pour améliorer la performance
            for (ActionIntervenant actionIntervenant : actionIntervenants) {
                stmt.setInt(1, actionIntervenant.getAction().getId());
                stmt.setInt(2, actionIntervenant.getIntervenant().getId());
                stmt.setString(3, actionIntervenant.getRole());
                stmt.addBatch();
            }

            // Exécute le batch
            int[] rowsInserted = stmt.executeBatch();
            return Arrays.stream(rowsInserted).sum() == actionIntervenants.size();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean update(Action action) {
        String sql="UPDATE `action` " +
                    "SET `nom`=?,`date_debut`=?,`date_fin`=?," +
                    "`status`=?,`description`=?,`systemeexigence_id`=? " +
                    "WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, action.getNom());
            stmt.setDate(2, action.getDateDebut());
            stmt.setDate(3, action.getDateFin());
            stmt.setString(4, action.getStatus());
            stmt.setString(5, action.getDescription());
            stmt.setInt(6,action.getSystemeExigence().getId());
            stmt.setInt(7, action.getId());
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(int id) {
        String sql="DELETE FROM `action` WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1,id);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Action> get(int idSystemExigence) {
        String query="SELECT `id`, `nom`, `date_debut`, `date_fin`, `dependance`, `status`, `description` FROM `action` WHERE systemeexigence_id=?";
        List<Action> list=new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1,idSystemExigence);
            ResultSet rs = stmt.executeQuery();
            try {
                while(rs.next()){
                    Action a=new Action();
                    a.setId(rs.getInt("id"));
                    a.setNom(rs.getString("nom"));
                    a.setDateDebut(rs.getDate("date_debut"));
                    a.setDateFin(rs.getDate("date_fin"));
                    a.setDescription(rs.getString("description"));
                    a.setStatus(rs.getString("status"));
                    a.setDependance(rs.getInt("dependance"));
                    list.add(a);
                }

                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<ActionIntervenant> getActionIntervenants(int idAction) {
        String query="SELECT a.id,u.id as id_user,u.name as name_user,u.email as email_user,a.role " +
                    "FROM actionintervenant as a,user as u " +
                    "WHERE action_id=? AND a.intervenant_id=u.id;";
        List<ActionIntervenant> list=new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1,idAction);
            ResultSet rs = stmt.executeQuery();
            try {
                while(rs.next()){
                    ActionIntervenant a=new ActionIntervenant();
                    a.setId(rs.getInt("id"));
                    a.setRole(rs.getString("role"));
                    User intervenant=new User();
                    intervenant.setId(rs.getInt("id_user"));
                    intervenant.setName(rs.getString("name_user"));
                    intervenant.setEmail(rs.getString("email_user"));
                    a.setIntervenant(intervenant);
                    list.add(a);
                }

                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    public static boolean deleteActionIntervenant(int id) {
        String sql="DELETE FROM `actionintervenant` WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1,id);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean deleteActionIntervenantByAction(int idAction) {
        String sql="DELETE FROM `actionintervenant` WHERE action_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1,idAction);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
