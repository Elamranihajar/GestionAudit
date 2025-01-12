package gestionAudits.vues.espaceAdmin.gestionClausesStandards;

import gestionAudits.controller.GestionClauseStandartController;
import gestionAudits.models.Clause;
import gestionAudits.models.ClauseStandard;
import gestionAudits.models.Standard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GestionStandardsClauses extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Clause> clauseList;
    private List<Standard> standardList;
    private List<ClauseStandard> clauseStandards;

    public GestionStandardsClauses() {
        clauseList = GestionClauseStandartController.listeClauses();
        standardList = GestionClauseStandartController.listeStandards();
        clauseStandards = GestionClauseStandartController.listerClauseStandard();

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Titre + Boutons dans un panneau vertical
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout(10, 10));
        headerPanel.setBorder(new EmptyBorder(10, 0, 20, 0));

        // Titre
        JLabel titleLabel = new JLabel("Gestion des Standards et Clauses");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        // Boutons au-dessous du titre
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("Ajouter");
        styleButton(addButton, new Color(60, 179, 113));
        addButton.addActionListener(e -> showAddModal());

        JButton manageAssociationsButton = new JButton("Gestion Associations Clause Standards");
        styleButton(manageAssociationsButton, new Color(100, 149, 237));
        manageAssociationsButton.addActionListener(e -> manageAssociations());

        topButtonPanel.add(addButton);
        topButtonPanel.add(manageAssociationsButton);
        headerPanel.add(topButtonPanel, BorderLayout.LINE_START);

        add(headerPanel, BorderLayout.NORTH);

        // Table pour afficher les données
        String[] columnNames = {"ID", "Description", "Référence", "Type"};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnNames);
        listerClauseStandar();
        table = new JTable(tableModel);
        styleTable();

        // Ajouter menu contextuel pour les actions de clic droit
        JPopupMenu popupMenu = createContextMenu();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger() && table.getSelectedRow() != -1) {
                    popupMenu.show(table, e.getX(), e.getY());
                }
            }
        });

        // Ajouter la table dans un JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private void styleTable() {
        table.setRowHeight(30);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(135, 206, 250));
        table.setSelectionForeground(Color.BLACK);
    }

    private JPopupMenu createContextMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem editMenuItem = new JMenuItem("Modifier");
        editMenuItem.addActionListener(e -> editSelectedRow());
        popupMenu.add(editMenuItem);

        JMenuItem deleteMenuItem = new JMenuItem("Supprimer");
        deleteMenuItem.addActionListener(e -> deleteSelectedRow());
        popupMenu.add(deleteMenuItem);

        return popupMenu;
    }

    public void listerClauseStandar() {
        for (Clause c : clauseList)
            tableModel.addRow(new Object[]{c.getId(), c.getDescription(), c.getReference(), "Clause"});
        for (Standard s : standardList)
            tableModel.addRow(new Object[]{s.getId(), s.getDescription(), s.getReference(), "Standard"});
    }

    private void showAddModal() {
        ClauseStandardForm dialog = new ClauseStandardForm((Frame) SwingUtilities.getWindowAncestor(this), "Ajouter", "Ajouter", 0, "", "", "");
        dialog.setVisible(true);
        if (dialog.isSubmitted()) {
            String description = dialog.getDescription();
            String reference = dialog.getReference();
            String type = dialog.getClauseType();
            int newId = type.equals("Clause") ? clauseList.getLast().getId() + 1 : standardList.getLast().getId() + 1;
            tableModel.addRow(new Object[]{newId, description, reference, type});
        }
    }

    private void editSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.");
            return;
        }

        String description = (String) tableModel.getValueAt(selectedRow, 1);
        String reference = (String) tableModel.getValueAt(selectedRow, 2);
        String type = (String) tableModel.getValueAt(selectedRow, 3);
        int id = (int) tableModel.getValueAt(selectedRow, 0);

        ClauseStandardForm dialog = new ClauseStandardForm((Frame) SwingUtilities.getWindowAncestor(this), "Modifier", "Modifier", id, description, reference, type);
        dialog.setVisible(true);

        if (dialog.isSubmitted()) {
            tableModel.setValueAt(dialog.getDescription(), selectedRow, 1);
            tableModel.setValueAt(dialog.getReference(), selectedRow, 2);
            tableModel.setValueAt(dialog.getClauseType(), selectedRow, 3);
        }
    }

    private void deleteSelectedRow() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {
            int id=(int) tableModel.getValueAt(selectedRow, 0);
            String type = (String) tableModel.getValueAt(selectedRow, 3);
            ClauseStandardsSuppression clauseStandardsSuppression=new ClauseStandardsSuppression(null,id,clauseStandards,type);
            if (clauseStandardsSuppression.isSuppression())
                tableModel.removeRow(selectedRow);
            if(clauseStandardsSuppression.isShowAssociation())
                manageAssociations();
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à supprimer.");
        }
    }

    private void manageAssociations() {
        AssociationManager associationManager = new AssociationManager((Frame) SwingUtilities.getWindowAncestor(this));
        associationManager.setModal(true);
        associationManager.setVisible(true);
    }
}
