package gestionAudits.data;

import gestionAudits.models.Preuve;
import gestionAudits.models.SystemeExigence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GestionPreuvesData {

    public static boolean insert(List<Preuve> preuveList) {
        if (preuveList == null || preuveList.isEmpty()) {
            return false; // Rien à insérer
        }

        String query = "INSERT INTO `preuve` (`name`, `url`, `systeme_exigence_id`) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Ajout des données en batch pour améliorer la performance
            for (Preuve preuve : preuveList) {
                stmt.setString(1, preuve.getName());
                stmt.setString(2, preuve.getUrl());
                stmt.setInt(3, preuve.getSystemeExigence().getId());
                stmt.addBatch();
            }

            // Exécute le batch
            int[] rowsInserted = stmt.executeBatch();
            return Arrays.stream(rowsInserted).sum() == preuveList.size();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static List<Preuve> getPreuveList(int idSystem){
        List<Preuve> list = new ArrayList<>();
        String query="";
        if(idSystem>0)
            query="SELECT `id`, `name`, `url`,systeme_exigence_id FROM `preuve` WHERE systeme_exigence_id=?";
        else
            query="SELECT id,`name`, `url`,systeme_exigence_id FROM `preuve`";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if(idSystem>0)
                stmt.setInt(1, idSystem);
            ResultSet rs = stmt.executeQuery();
            try {
                while(rs.next()){
                    Preuve preuve = new Preuve();
                    preuve.setId(rs.getInt("id"));
                    preuve.setName(rs.getString("name"));
                    preuve.setUrl(rs.getString("url"));
                    preuve.setSystemeExigence(new SystemeExigence(rs.getInt("systeme_exigence_id")));
                    list.add(preuve);
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

    public static boolean deletePreuve(int id) {
        String query = "DELETE FROM `preuve` WHERE id=?";
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
