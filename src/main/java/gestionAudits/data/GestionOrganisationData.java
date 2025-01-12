package gestionAudits.data;

import gestionAudits.models.Organisation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestionOrganisationData {

    public static List<Organisation> listerOrganisation(){
        String query="SELECT * FROM organization";
        List<Organisation> list=new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            try {
                while(rs.next())
                    list.add(new Organisation(rs.getInt("id"),rs.getString("name"),rs.getString("address")));
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    public static boolean inserterOrganisation(Organisation organisation){

        String query = "INSERT INTO organization (name,address) VALUES (?, ?)";
    System.out.println(query);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, organisation.getNom());
            stmt.setString(2, organisation.getAdresse());
            int rowsInserted = stmt.executeUpdate();

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateOrganisation(Organisation organisation){
        String query = "UPDATE organization SET name = ?, address = ? WHERE id = ?";
        System.out.println(query);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1,organisation.getNom());
            stmt.setString(2, organisation.getAdresse());
            stmt.setInt(3,organisation.getId());
            int rowsInserted = stmt.executeUpdate();

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteOrganisation(int id){
        String query = "DELETE FROM organization WHERE id = ?";
        System.out.println(id);
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
