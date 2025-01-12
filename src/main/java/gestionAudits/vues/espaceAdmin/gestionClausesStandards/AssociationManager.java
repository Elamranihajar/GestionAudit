package gestionAudits.vues.espaceAdmin.gestionClausesStandards;

import gestionAudits.controller.GestionClauseStandartController;
import gestionAudits.models.Clause;
import gestionAudits.models.ClauseStandard;
import gestionAudits.models.Standard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssociationManager extends JDialog {

    private JTable associationsTable;
    private DefaultTableModel tableModel;
    private List<ClauseStandard> clauseStandards;

    public AssociationManager(Frame parent) {
        super(parent);
        setSize(600, 400);
        setTitle("Gestion des Associations");
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10,10));
        
        // Charger les données initiales
        clauseStandards = GestionClauseStandartController.listerClauseStandard();

        // Configuration du tableau
        String[] columnNames = {"Standard", "Clause"};
        tableModel = new DefaultTableModel(columnNames, 0);
        loadTableData();
        
        associationsTable = new JTable(tableModel);
        associationsTable.setRowHeight(25);
        associationsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        associationsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        associationsTable.getTableHeader().setBackground(new Color(100, 149, 237));
        associationsTable.getTableHeader().setForeground(Color.WHITE);

        // Ajouter un menu contextuel pour modifier/supprimer
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem modifyItem = new JMenuItem("Modifier");
        JMenuItem deleteItem = new JMenuItem("Supprimer");
        
        popupMenu.add(modifyItem);
        popupMenu.add(deleteItem);

        associationsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = associationsTable.rowAtPoint(e.getPoint());
                    associationsTable.setRowSelectionInterval(row, row);
                    popupMenu.show(associationsTable, e.getX(), e.getY());
                }
            }
        });

        // Action Modifier
        modifyItem.addActionListener(e -> manageAssociation(true));

        // Action Supprimer
        deleteItem.addActionListener(e -> deleteAssociation());

        JScrollPane scrollPane = new JScrollPane(associationsTable);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        // Bouton Ajouter une association
        JButton addButton = new JButton("Ajouter une association");
        addButton.setBackground(new Color(34, 139, 34)); // Vert foncé
        addButton.setForeground(Color.WHITE); // Texte en blanc
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createLineBorder(new Color(0, 100, 0), 2)); // Bordure verte foncée
        addButton.addActionListener(e -> manageAssociation(false));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(addButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);


    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        for (ClauseStandard entry : clauseStandards) {
            tableModel.addRow(new Object[]{
                entry.getStandard().getDescription(),
                entry.getClause().getDescription()
            });
        }
    }

    private void manageAssociation(boolean isModify) {
        List<Integer> standardIds = GestionClauseStandartController.listeStandards()
                .stream()
                .map(Standard::getId)
                .collect(Collectors.toList());
        List<Integer> clauseIds = GestionClauseStandartController.listeClauses()
                .stream()
                .map(Clause::getId)
                .collect(Collectors.toList());

        if (standardIds.isEmpty() || clauseIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun standard ou clause disponible pour l'association.");
            return;
        }

        JComboBox<Integer> standardComboBox = new JComboBox<>(standardIds.toArray(new Integer[0]));
        JComboBox<Integer> clauseComboBox = new JComboBox<>(clauseIds.toArray(new Integer[0]));
        int id = 0;
        if (isModify) {
            int selectedRow = associationsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Aucune ligne sélectionnée.");
                return;
            }

            ClauseStandard selectedEntry = clauseStandards.get(selectedRow);
            standardComboBox.setSelectedItem(selectedEntry.getStandard().getId());
            clauseComboBox.setSelectedItem(selectedEntry.getClause().getId());
            id = selectedEntry.getId();

        }

        Object[] message = {
                "Sélectionnez un standard :", standardComboBox,
                "Sélectionnez une clause :", clauseComboBox
        };

        int option = JOptionPane.showConfirmDialog(this, message, isModify ? "Modifier une association" : "Ajouter une association", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int selectedStandardId = (Integer) standardComboBox.getSelectedItem();
            int selectedClauseId = (Integer) clauseComboBox.getSelectedItem();

            if(isModify)
                GestionClauseStandartController.modiferAssociation(id,selectedClauseId,selectedStandardId);
            else
                GestionClauseStandartController.associeStandartAClause(selectedClauseId, selectedStandardId);
            JOptionPane.showMessageDialog(this, isModify ? "Association modifiée avec succès." : "Association ajoutée avec succès.");

            // Recharger les données après ajout ou modification
            clauseStandards = GestionClauseStandartController.listerClauseStandard();
            loadTableData();
        }
    }

    private void deleteAssociation() {
        int selectedRow = associationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Aucune ligne sélectionnée.");
            return;
        }

        ClauseStandard selectedEntry = clauseStandards.get(selectedRow);
        int option = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cette association ?", "Supprimer", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
             GestionClauseStandartController.supprimerAssociation(selectedEntry.getId());
            JOptionPane.showMessageDialog(this, "Association supprimée avec succès.");

            // Recharger les données après suppression
            clauseStandards = GestionClauseStandartController.listerClauseStandard();
            loadTableData();
        }
    }
}
