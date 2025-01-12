package gestionAudits.data;

import gestionAudits.models.AutreExigence;
import gestionAudits.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionAutreExigenceData {

    public static int insert(AutreExigence autreExigence) {
        String query;
        boolean isUpdate = false;

        // Vérifier si l'exigence existe déjà
        AutreExigence existingExigence = get(autreExigence.getId());

        if (existingExigence != null && existingExigence.getName() != null) {
            // Si l'exigence existe, préparer une requête de mise à jour
            query = "UPDATE `autreexigence` " +
                    "SET name = ?, `description` = ?, `type` = ? " +
                    "WHERE id = ?";
            isUpdate = true;
        } else {
            // Sinon, préparer une requête d'insertion
            query = "INSERT INTO `autreexigence` (name, `description`, `type`) VALUES (?, ?, ?)";
        }

        System.out.println(query);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Définir les valeurs des paramètres
            stmt.setString(1, autreExigence.getName());
            stmt.setString(2, autreExigence.getDescription());
            stmt.setString(3, autreExigence.getType());

            if (isUpdate)  // Ajouter l'ID pour une mise à jour
                stmt.setInt(4, autreExigence.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                if (!isUpdate) {
                    // Si c'était une insertion, retourner l'ID généré
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1);
                        }
                    }
                } else {
                    // Si c'était une mise à jour, retourner 0 pour indiquer le succès
                    return 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Retourner -1 en cas d'erreur
        return -1;
    }


    public static AutreExigence get(int idAutreExigence) {
        AutreExigence autreExigence = new AutreExigence();

        String sql = "SELECT `id`, `name`, `description`, `type` FROM `autreexigence` WHERE id=? ";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAutreExigence);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                autreExigence.setId(rs.getInt("id"));
                autreExigence.setName(rs.getString("name"));
                autreExigence.setDescription(rs.getString("description"));
                autreExigence.setType(rs.getString("type"));
            }
            return autreExigence;
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static boolean update(AutreExigence autreExigence) {
//        String query = "UPDATE `autreexigence` " +
//                       "SET name=?,`description`=?,`type`=?" +
//                       "WHERE id=?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
//
//            // Définir les valeurs des paramètres
//            stmt.setString(1, autreExigence.getName());
//            stmt.setString(2, autreExigence.getDescription());
//            stmt.setString(3, autreExigence.getType());
//            stmt.setInt(4, autreExigence.getId());
//
//            // Exécuter la requête
//            int rowsInserted = stmt.executeUpdate();
//
//            return rowsInserted > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false; // Retourne -1 en cas d'erreur
//    }
}
