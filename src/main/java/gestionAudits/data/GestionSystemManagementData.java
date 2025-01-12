package gestionAudits.data;

import gestionAudits.models.Organisation;
import gestionAudits.models.Site;
import gestionAudits.models.SystemeManagement;
import gestionAudits.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestionSystemManagementData {

    public static List<SystemeManagement> getSystemManagement() {
        List<SystemeManagement> systemeManagements = new ArrayList<>();

        String sql = """
                SELECT s.id as system_id,s.nom as nom_system,s.nb_personnes as nbr_personne,s.description as description_system,
                        o.id as org_id,o.name as org_name,
                        u.id as reposonsable_id,u.name as reposonsable_name
                FROM systememanagement as s,organization as o, user as u
                WHERE    s.organization_id=o.id
                AND		s.responsable_id=u.id;
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
                SystemeManagement systemeManagement=new SystemeManagement();
                systemeManagement.setId(rs.getInt("system_id"));
                systemeManagement.setDescription(rs.getString("description_system"));
                systemeManagement.setNbPersonnes(rs.getInt("nbr_personne"));
                systemeManagement.setNom(rs.getString("nom_system"));
                systemeManagement.setOrganisation(organisation);
                systemeManagement.setResponsable(responsable);
                systemeManagements.add(systemeManagement);
            }
            return systemeManagements;
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static SystemeManagement getSystemManagement(int id) {
        List<SystemeManagement> systemeManagements = new ArrayList<>();

        String sql = """
               SELECT s.id as system_id,s.nom as nom_system,s.nb_personnes as nbr_personne,s.description as description_system,
                      o.id as org_id,o.name as org_name,
                      u.id as reposonsable_id,u.name as reposonsable_name,u.email as reposonsable_email,u.domain as reposonsable_domain
               FROM systememanagement as s,organization as o, user as u
               WHERE   s.id=?
               AND     s.organization_id=o.id
               AND		s.responsable_id=u.id;
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            SystemeManagement systemeManagement=new SystemeManagement();
            while (rs.next()) {
                User responsable = new User();
                responsable.setId(rs.getInt("reposonsable_id"));
                responsable.setName(rs.getString("reposonsable_name"));
                responsable.setEmail(rs.getString("reposonsable_email"));
                responsable.setDomain(rs.getString("reposonsable_domain"));
                Organisation organisation=new Organisation();
                organisation.setId(rs.getInt("org_id"));
                organisation.setNom(rs.getString("org_name"));
                systemeManagement.setId(rs.getInt("system_id"));
                systemeManagement.setDescription(rs.getString("description_system"));
                systemeManagement.setNbPersonnes(rs.getInt("nbr_personne"));
                systemeManagement.setNom(rs.getString("nom_system"));
                systemeManagement.setOrganisation(organisation);
                systemeManagement.setResponsable(responsable);
            }
            return systemeManagement;
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }


    public static boolean insertSystem(SystemeManagement systemeManagement) {
        String sql = "INSERT INTO `systememanagement`(`description`, `nom`, `nb_personnes`, `organization_id`, `responsable_id`) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, systemeManagement.getDescription());
            stmt.setString(2, systemeManagement.getNom());
            stmt.setInt(3, systemeManagement.getNbPersonnes());
            stmt.setInt(4,systemeManagement.getOrganisation().getId());
            stmt.setInt(5,systemeManagement.getResponsable().getId());


            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateSytemMangement(SystemeManagement systemeManagement) {
        String sql = """
                        UPDATE `systememanagement` 
                        SET `description`=?,`nom`=?,`nb_personnes`=?,`organization_id`=?,`responsable_id`=? 
                        WHERE id=? 
                       """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1,systemeManagement.getDescription());
            stmt.setString(2,systemeManagement.getNom());
            stmt.setInt(3,systemeManagement.getNbPersonnes());
            stmt.setInt(4,systemeManagement.getOrganisation().getId());
            stmt.setInt(5,systemeManagement.getResponsable().getId());
            stmt.setInt(6,systemeManagement.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating site: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteSystem(int id) {
        String query = "DELETE FROM `systememanagement` WHERE id=?";
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
