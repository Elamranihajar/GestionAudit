package gestionAudits.data;

import gestionAudits.models.Organisation;
import gestionAudits.models.Site;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestionSiteData {

    public static List<Site> getSites() {
        List<Site> sites = new ArrayList<>();

        String sql = """
            SELECT s.id AS site_id, s.name AS site_name, s.address AS site_address, 
                   s.description AS site_description, 
                   o.id AS org_id, o.name AS org_name, o.address AS org_address
            FROM Site s
            JOIN Organization o ON s.organization_id = o.id
            ORDER BY site_id
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                sites.add(new Site(
                        rs.getInt("site_id"),
                        rs.getString("site_name"),
                        rs.getString("site_address"),
                        rs.getString("site_description"),
                        new Organisation(
                                rs.getInt("org_id"),
                                rs.getString("org_name"),
                                rs.getString("org_address")
                        )
                ));
            }
            return sites;
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static boolean insertSite(Site site) {
        String sql = "INSERT INTO Site (name, address, description, organization_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, site.getName());
            stmt.setString(2, site.getAddress());
            stmt.setString(3, site.getDescription());
            stmt.setInt(4, site.getOrganization().getId());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateSite(Site site) {
        String sql = "UPDATE Site SET name = ?, address = ?, description = ?, organization_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, site.getName());
            stmt.setString(2, site.getAddress());
            stmt.setString(3, site.getDescription());
            stmt.setInt(4, site.getOrganization().getId());
            stmt.setInt(5, site.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating site: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteSite(int id) {
        String query = "DELETE FROM site WHERE id = ?";
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
