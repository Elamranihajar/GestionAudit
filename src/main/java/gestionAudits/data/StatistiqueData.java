package gestionAudits.data;

import gestionAudits.models.Action;
import gestionAudits.models.ActionStatistique;
import gestionAudits.models.SytemExigenceStatistique;
import gestionAudits.models.Statistiques;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatistiqueData {

    public static Statistiques getStatistiqueAudits(){
        String sql="SELECT\n" +
                "\tCOUNT(*) AS audits,\n" +
                "    SUM(CASE WHEN status = 'En cours' THEN 1 ELSE 0 END) AS audits_en_cours,\n" +
                "    SUM(CASE WHEN status = 'Terminé' THEN 1 ELSE 0 END) AS audits_termines,\n" +
                "    SUM(CASE WHEN status = 'Planifié' THEN 1 ELSE 0 END) AS audits_planifies\n" +
                "FROM Audit";
        Statistiques statistique=new Statistiques();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
               statistique.setNbrAudits(rs.getInt("audits"));
               statistique.setNbrAuditsEnCours(rs.getInt("audits_en_cours"));
               statistique.setNbrAuditsTerminer(rs.getInt("audits_termines"));
               statistique.setNbrAuditsPlanifer(rs.getInt("audits_planifies"));
            }
            return statistique;
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
    public static int getStatistiqueAction(){
        String sql="SELECT COUNT(*) as nbrActions FROM `action`";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            int resultat = 0;
            while (rs.next())
                resultat=rs.getInt("nbrActions");
            return resultat;
        }catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

    }

    public static List<SytemExigenceStatistique> nbrConformNonConformParAudit(){
        String sql="SELECT\n" +
                "SUM(CASE WHEN status = 'Conforme' THEN 1 ELSE 0 END) * 100.0 / COUNT(*) AS pourcentage_respectees,\n" +
                "SUM(CASE WHEN status = 'Non Conforme' THEN 1 ELSE 0 END) * 100.0 / COUNT(*) AS pourcentage_non_respectees,\n" +
                "COUNT(id) AS nbr_system, \n" +
                "audit_id\n" +
                "FROM SystemeExigence\n" +
                "GROUP BY audit_id";
        List<SytemExigenceStatistique> nbrConformNonConformParAudit=new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            int resultat = 0;
            while (rs.next())
                nbrConformNonConformParAudit.add(
                        new SytemExigenceStatistique(
                                rs.getInt("audit_id"),
                                rs.getInt("pourcentage_respectees"),
                                rs.getInt("pourcentage_non_respectees"),
                                rs.getInt("nbr_system")
                        )
                );
            return nbrConformNonConformParAudit;
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static List<ActionStatistique> nbrActionParAudit(){
        String sql="SELECT audit.id,COUNT(action.id) as \"nbrAction\"," +
                " SUM(CASE WHEN action.status = 'En cours' THEN 1 ELSE 0 END) as prc_enCours," +
                " SUM(CASE WHEN action.status = 'Terminer' THEN 1 ELSE 0 END) as prc_terminer " +
                "FROM audit,systemeexigence,action " +
                "WHERE action.systemeexigence_id=systemeexigence.id " +
                "AND systemeexigence.audit_id=audit.id " +
                "GROUP BY audit.id;";
        List<ActionStatistique> nbrActionParAudit=new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            int resultat = 0;
            while (rs.next())
                nbrActionParAudit.add(
                        new ActionStatistique(
                                rs.getInt("id"),
                                rs.getInt("nbrAction"),
                                rs.getFloat("prc_enCours"),
                                rs.getFloat("prc_terminer")
                        )
                );
            return nbrActionParAudit;
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }


}
