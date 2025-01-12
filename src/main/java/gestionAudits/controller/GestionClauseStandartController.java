package gestionAudits.controller;

import gestionAudits.data.GestionClauseStandartData;
import gestionAudits.models.Clause;
import gestionAudits.models.ClauseStandard;
import gestionAudits.models.Standard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GestionClauseStandartController {


    public static boolean associationExiste(int id, List<ClauseStandard> clauseStandards) {
        return clauseStandards.stream()
                .anyMatch(clauseStandard ->
                        id == clauseStandard.getClause().getId() ||
                        id == clauseStandard.getStandard().getId());
    }


    public static List<Clause> listeClauses() {
        return GestionClauseStandartData.listerClause();
    }

    public static List<Standard> listeStandards() {
        return GestionClauseStandartData.listerStandard();
    }

    public static boolean ajouterClause(Clause clause) {
        System.out.println(clause);
        return GestionClauseStandartData.insertClause(clause);

    }

    public static boolean ajouterStandart(Standard standart) {
        System.out.println(standart);
        return GestionClauseStandartData.insertStandart(standart);
    }

    public static boolean associeStandartAClause(int clauseId, int standartId) {
        return GestionClauseStandartData.associeStandartAclause(clauseId, standartId);
    }



    public static boolean modifierClause(Clause clause) {
        System.out.println("idC : "+clause.getId());
        return GestionClauseStandartData.updateClause(clause);
    }

    public static boolean modifierStandard(Standard standard) {
        System.out.println("idS : "+standard.getId());
        return GestionClauseStandartData.updateStandart(standard);
    }

    public static boolean supprimerStandart(int id) {
        GestionClauseStandartData.deleteAssociation(id,"standard");
        return GestionClauseStandartData.deleteStandart(id);
    }

    public static boolean supprimerClause(int id) {
        GestionClauseStandartData.deleteAssociation(id,"clause");
        return GestionClauseStandartData.deleteClause(id);
    }

    public static List<ClauseStandard> listerClauseStandard() {
        return GestionClauseStandartData.listerClauseStandard();
    }

    public static void modiferAssociation(int id,int selectedClauseId, int selectedStandardId) {
        GestionClauseStandartData.updateAssociation(id,selectedClauseId,selectedStandardId);
    }

    public static boolean supprimerAssociation(int id) {
        return GestionClauseStandartData.deleteAssociation(id,"");
    }


}
