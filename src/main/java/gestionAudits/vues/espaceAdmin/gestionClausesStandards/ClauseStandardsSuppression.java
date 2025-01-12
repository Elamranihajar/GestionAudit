package gestionAudits.vues.espaceAdmin.gestionClausesStandards;

import gestionAudits.controller.GestionClauseStandartController;
import gestionAudits.models.ClauseStandard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ClauseStandardsSuppression {

    // Méthode pour afficher la fenêtre modale
    private boolean isSuppression;
    private boolean showAssociation;
    private void createUIComponents(JFrame parentFrame,String typeClause,int clauseId) {
        JDialog dialog = new JDialog(parentFrame, STR."Danger: \{typeClause} associée", true);
        dialog.setLayout(new BorderLayout());

        // Message d'alerte
        String autreTypeClause = typeClause.equals("Clause") ? "standard" : "clause";

        Icon dangerIcon = UIManager.getIcon("OptionPane.errorIcon");

        // Panel principal pour le message et l'icône
        JPanel messagePanel = new JPanel(new BorderLayout(10, 10));
        JLabel iconLabel = new JLabel(dangerIcon);
        messagePanel.add(iconLabel, BorderLayout.WEST);

        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>"
                + "Cette "+typeClause+" est associée à un ou plusieurs "+autreTypeClause+".<br>" +
                "Donc,Cette action supprimera également toutes les associations" +
                "liées à cette clause.<br>" +
                "Voulez-vous continuer ?"
                + "</div></html>", JLabel.CENTER);
        messageLabel.setForeground(Color.RED);
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        dialog.add(messagePanel, BorderLayout.CENTER);

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Bouton Supprimer
        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                supprimer(clauseId,typeClause);
                dialog.dispose();
            }
        });
        buttonPanel.add(deleteButton);

        // Bouton Voir les associations
        JButton viewAssociationsButton = new JButton("Voir les associations");
        viewAssociationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAssociation=true;
                dialog.dispose();
            }
        });
        buttonPanel.add(viewAssociationsButton);

        // Bouton Annuler
        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        buttonPanel.add(cancelButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Paramètres de la fenêtre modale
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }
    public ClauseStandardsSuppression(JFrame parentFrame, int clauseId, List<ClauseStandard> clauseStandardList, String typeClause) {
        // Création de la fenêtre modale

        if(!GestionClauseStandartController.associationExiste(clauseId,clauseStandardList))
            supprimer(clauseId,typeClause);
        else
            createUIComponents(null,typeClause,clauseId);// afficher message pour indiquer que les association de cet clause sera supprimer
    }

    private void supprimer(int id,String typeClause){
        if (typeClause.equals("Standard"))
            GestionClauseStandartController.supprimerStandart(id);
        else
            GestionClauseStandartController.supprimerClause(id);
        isSuppression=true;
    }

    public boolean isSuppression() {
        return isSuppression;
    }

    public void setSuppression(boolean suppression) {
        isSuppression = suppression;
    }

    public boolean isShowAssociation() {
        return showAssociation;
    }

    public void setShowAssociation(boolean showAssociation) {
        this.showAssociation = showAssociation;
    }
}
