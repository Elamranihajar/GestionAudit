package gestionAudits.data;

import gestionAudits.models.Organisation;
import gestionAudits.models.Processus;
import gestionAudits.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestionProcessusData {
    public static List<Processus> getProcessus() {
        List<Processus> Processuss = new ArrayList<>();

        String sql = """
                    SELECT 	p.id as processus_id,p.name as nom_processus,p.description as description_processus,
                            o.id as org_id,o.name as org_name,
                            u.id as reposonsable_id,u.name as reposonsable_name
                    FROM processus as p,organization as o, user as u
                    WHERE    p.organization_id=o.id
                    AND		p.responsable_id=u.id
                    ORDER BY processus_id;
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User responsable = new User();
                responsable.setId(rs.getInt("reposonsable_id"));
                responsable.setName(rs.getString("reposonsable_name"));
                Organisation organisation=new Organisation();
                organisation.setId(rs.getInt("org_id"));
                organisation.setNom(rs.getString("org_name"));
                Processus Processus=new Processus();
                Processus.setId(rs.getInt("processus_id"));
                Processus.setDescription(rs.getString("description_processus"));

                Processus.setName(rs.getString("nom_processus"));
                Processus.setOrganization(organisation);
                Processus.setResponsable(responsable);
                Processuss.add(Processus);
            }
            return Processuss;
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static boolean insertProcessus(Processus Processus) {
        String sql = "INSERT INTO `processus`(`description`, `name`,`organization_id`, `responsable_id`) VALUES (?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, Processus.getDescription());
            stmt.setString(2, Processus.getName());
            stmt.setInt(3,Processus.getOrganization().getId());
            stmt.setInt(4,Processus.getResponsable().getId());


            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateProcessus(Processus processus) {
        System.out.println(processus);
        String sql = """
                        UPDATE `processus` 
                        SET `description`=?,`name`=?,`organization_id`=?,`responsable_id`=? 
                        WHERE id=? 
                       """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1,processus.getDescription());
            stmt.setString(2,processus.getName());
            stmt.setInt(3,processus.getOrganization().getId());
            stmt.setInt(4,processus.getResponsable().getId());
            stmt.setInt(5,processus.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating site: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteProcessus(int id) {
        String query = "DELETE FROM `processus` WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1,id);
            int rowsInserted = stmt.executeUpdate();

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
