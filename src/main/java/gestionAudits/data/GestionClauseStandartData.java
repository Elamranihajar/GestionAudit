package gestionAudits.data;

import gestionAudits.models.Clause;
import gestionAudits.models.ClauseStandard;
import gestionAudits.models.Organisation;
import gestionAudits.models.Standard;
import org.checkerframework.checker.units.qual.C;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class GestionClauseStandartData {

    public static List<Clause> listerClause(){
        String query="SELECT * FROM clause";
        List<Clause> list=new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            try {
                while(rs.next())
                    list.add(new Clause(rs.getInt("id"),rs.getString("description"),rs.getString("reference")));
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static List<Standard> listerStandard(){
        String query="SELECT * FROM standard";
        List<Standard> list=new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            try {
                while(rs.next())
                    list.add(new Standard(rs.getInt("id"),rs.getString("description"),rs.getString("reference")));
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean insertClause(Clause clause){

        String insert="INSERT INTO clause (description,reference) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(insert);) {

            stmt.setString(1, clause.getDescription());
            stmt.setString(2, clause.getReference());


            int rowsInserted = stmt.executeUpdate();


            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean insertStandart(Standard standard){

        String insert="INSERT INTO standard (description,reference) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insert);) {

            stmt.setString(1, standard.getDescription());
            stmt.setString(2, standard.getReference());


            int rowsInserted = stmt.executeUpdate();


            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean associeStandartAclause(int clauseID,int standardID){
        String insert="INSERT INTO classestandard (standard_id,clause_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insert);) {

            stmt.setInt(1, standardID);
            stmt.setInt(2, clauseID);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateClause(Clause clause){
        System.out.println(clause);
        String query = "UPDATE clause SET description = ?, reference = ? WHERE id = ? ";
        System.out.println(query);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1,clause.getDescription());
            stmt.setString(2, clause.getReference());
            stmt.setInt(3,clause.getId());
            int rowsInserted = stmt.executeUpdate();

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateStandart(Standard standard){
        System.out.println(standard);
        String query = "UPDATE standard SET description = ?, reference = ? WHERE id = ? ";
        System.out.println(query);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1,standard.getDescription());
            stmt.setString(2, standard.getReference());
            stmt.setInt(3,standard.getId());

            int rowsInserted = stmt.executeUpdate();

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteAssociation(int id,String typeClause){
        String query;
        if(typeClause.equals("standard"))
            query = "DELETE FROM classestandard WHERE standard_id = ?";
        else if(typeClause.equals("clause"))
            query = "DELETE FROM classestandard WHERE clause_id = ?";
        else
            query = "DELETE FROM classestandard WHERE id= ?";

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

    public static boolean deleteStandart(int id){
        String query = "DELETE FROM standard WHERE id = ?";
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
    public static boolean deleteClause(int id){
        String query = "DELETE FROM clause WHERE id = ?";
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

    public static List<ClauseStandard> listerClauseStandard(){
        String query="SELECT cs.*,\n" +
                "\t   c.description as descriptionClause,\n" +
                "       c.reference as referenceClause,\n" +
                "       s.description as descriptionStandart,\n" +
                "       s.reference as referenceStandart\n" +
                "FROM `classestandard` as cs,clause as c,standard as s\n" +
                "WHERE cs.standard_id=s.id\n" +
                "AND\t  cs.clause_id=c.id;";

        List<ClauseStandard> list=new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            try {
                while(rs.next())
                    list.add(new ClauseStandard(
                            rs.getInt("id"),
                            new Standard(
                                    rs.getInt("standard_id"),
                                    rs.getString("descriptionStandart"),
                                    rs.getString("referenceStandart")),
                            new Clause(
                                    rs.getInt("clause_id"),
                                    rs.getString("descriptionClause"),
                                    rs.getString("referenceClause"))
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

    public static ClauseStandard getClauseStandard(int id){
        String query="SELECT cs.*,\n" +
                "\t     c.description as descriptionClause,\n" +
                "       c.reference as referenceClause,\n" +
                "       s.description as descriptionStandart,\n" +
                "       s.reference as referenceStandart\n" +
                "FROM `classestandard` as cs,clause as c,standard as s\n" +
                "WHERE cs.id=? " +
                "AND cs.standard_id=s.id\n" +
                "AND  cs.clause_id=c.id;";

       ClauseStandard clauseStandard=null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            try {
                while(rs.next()) {
                    Standard standard=new Standard(
                            rs.getInt("standard_id"),
                            rs.getString("descriptionStandart"),
                            rs.getString("referenceStandart"));
                    Clause clause=new Clause(
                            rs.getInt("clause_id"),
                            rs.getString("descriptionClause"),
                            rs.getString("referenceClause"));

                    clauseStandard = new ClauseStandard(
                            rs.getInt("id"),
                            standard,
                            clause
                    );
                }
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clauseStandard;
    }


    public static boolean updateAssociation(int id,int clauseId, int standardId) {
        String query = "UPDATE classestandard SET clause_id = ?, standard_id = ? WHERE id = ? ";
        System.out.println(query);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1,clauseId);
            stmt.setInt(2,standardId);
            stmt.setInt(3,id);

            int rowsInserted = stmt.executeUpdate();

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
