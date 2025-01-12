package gestionAudits.data;

import gestionAudits.models.Audit;
import gestionAudits.models.Organisation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestionAuditsData {
    public static boolean insertAudit(Audit audit) {
        String query = "INSERT INTO `audit`(date_debut,`date_fin`, `status`, `type`, `description`) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1,audit.getDateDebut());
            stmt.setDate(2,audit.getDateFin());
            stmt.setString(3, audit.getStatus());
            stmt.setString(4, audit.getType());
            stmt.setString(5, audit.getDescription());

            int rowsInserted = stmt.executeUpdate();

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateAudit(Audit audit) {
        String query = "UPDATE `audit` " +
                       "SET  `date_debut`=?,`date_fin`=?,`status`=?,`type`=?,`description`=?" +
                       "WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1,audit.getDateDebut());
            stmt.setDate(2,audit.getDateFin());
            stmt.setString(3, audit.getStatus());
            stmt.setString(4, audit.getType());
            stmt.setString(5, audit.getDescription());
            stmt.setInt(6, audit.getId());
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteAudit(int id) {
        String query = "DELETE FROM `audit` WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1,id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Audit> getAudits() {
        String query="SELECT `id`, `date_debut`, `date_fin`, `status`, `type`, `description` FROM `audit` WHERE 1";
        List<Audit> list=new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            try {
                while(rs.next())
                   list.add(new Audit(
                           rs.getInt("id"),
                           rs.getString("description"),
                           rs.getString("type"),
                           rs.getDate("date_debut"),
                           rs.getDate("date_fin"),
                           rs.getString("status")
                   ));
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Audit getAudit(int id) {
        String query="SELECT `id`, `date_debut`, `date_fin`, `status`, `type`, `description` FROM `audit` WHERE id=?";
        Audit audit = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            try {
                while(rs.next())
                   audit=new Audit(
                            rs.getInt("id"),
                            rs.getString("description"),
                            rs.getString("type"),
                            rs.getDate("date_debut"),
                            rs.getDate("date_fin"),
                            rs.getString("status")
                    );
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return audit;
    }
}
