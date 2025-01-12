package gestionAudits.data;

import gestionAudits.models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestionSystemExigenceData {
    public static boolean insert(SystemeExigence systemeExigence) {
        String sql = "INSERT INTO `systemeexigence`" +
                     "(`audit_id`, `auditeur_id`, `autre_exigence_id`, `classe_standard_id`, `exclus`, `motif_exclusion`, `status`, `systeme_management_id`)" +
                     " VALUES (?,?,?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1,systemeExigence.getAudit().getId());
            stmt.setInt(2,systemeExigence.getAuditeur().getId());
            if (systemeExigence.getAutreExigence() != null) {
                stmt.setInt(3, systemeExigence.getAutreExigence().getId());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            stmt.setInt(4,systemeExigence.getClauseStandard().getId());
            stmt.setInt(5,systemeExigence.isExclu()?1:0);
            stmt.setString(6,systemeExigence.getMotifExclusion());
            stmt.setString(7,systemeExigence.getStatus());
            stmt.setInt(8,systemeExigence.getSystemeManagement().getId());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static List<SystemeExigence> selectAll(int auditId) {
        List<SystemeExigence> systemeExigenceList = new ArrayList<>();

        String sql = """
                SELECT `id`, `audit_id`, `auditeur_id`,
                       `autre_exigence_id`, `classe_standard_id`, `exclus`, 
                       `motif_exclusion`, `status`, `systeme_management_id` 
                FROM `systemeexigence`
                WHERE audit_id=?
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1,auditId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                SystemeExigence systemeExigence=new SystemeExigence(
                        rs.getInt("id"),
                        rs.getString("status"),
                        rs.getInt("exclus") == 1,
                        rs.getString("motif_exclusion")
                );

                systemeExigence.setAutreExigence(new AutreExigence(rs.getInt("autre_exigence_id")));
                systemeExigence.setAuditeur(new User(rs.getInt("auditeur_id")));
                systemeExigence.setSystemeManagement(new SystemeManagement(rs.getInt("systeme_management_id")));
                systemeExigence.setClauseStandard(new ClauseStandard(rs.getInt("classe_standard_id"),null,null));
                systemeExigenceList.add(systemeExigence);
            }
            return systemeExigenceList;
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static List<SystemeExigence> get(int idAuditeur) {
        List<SystemeExigence> systemeExigenceList = new ArrayList<>();

        String sql = """
                SELECT `id`, `audit_id`,
                       `autre_exigence_id`, `classe_standard_id`, `exclus`, 
                       `motif_exclusion`, `status`, `systeme_management_id`,constat,ecart
                FROM `systemeexigence`
                WHERE auditeur_id=?
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1,idAuditeur);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                SystemeExigence systemeExigence=new SystemeExigence(
                        rs.getInt("id"),
                        rs.getString("status"),
                        rs.getInt("exclus") == 1,
                        rs.getString("motif_exclusion")
                );
                systemeExigence.setConstats(rs.getString("constat"));
                systemeExigence.setEcarts(rs.getString("ecart"));

                systemeExigence.setAudit(new Audit(rs.getInt("audit_id")));
                systemeExigence.setAutreExigence(new AutreExigence(rs.getInt("autre_exigence_id")));
                systemeExigence.setSystemeManagement(new SystemeManagement(rs.getInt("systeme_management_id")));
                systemeExigence.setClauseStandard(new ClauseStandard(rs.getInt("classe_standard_id"),null,null));
                systemeExigenceList.add(systemeExigence);
            }
            return systemeExigenceList;
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static boolean update(SystemeExigence systemeExigence) {
        String sql = "UPDATE `systemeexigence`" +
                    "SET  auditeur_id=?,autre_exigence_id=?,classe_standard_id=?,exclus=?,motif_exclusion=?,status=?,systeme_management_id=?,`constat`=?, `ecart`=?" +
                    "WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

          //  stmt.setInt(1,systemeExigence.getAudit().getId());
            stmt.setInt(1,systemeExigence.getAuditeur().getId());

            if (systemeExigence.getAutreExigence() != null) {
                stmt.setInt(2, systemeExigence.getAutreExigence().getId());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            stmt.setInt(3,systemeExigence.getClauseStandard().getId());
            stmt.setInt(4,systemeExigence.isExclu()?1:0);
            stmt.setString(5,systemeExigence.getMotifExclusion());
            stmt.setString(6,systemeExigence.getStatus());
            stmt.setInt(7,systemeExigence.getSystemeManagement().getId());
            stmt.setString(8,systemeExigence.getConstats());
            stmt.setString(9,systemeExigence.getEcarts());
            stmt.setInt(10,systemeExigence.getId());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(int id) {
        String query = "DELETE FROM `systemeexigence` WHERE id=?";
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
}
