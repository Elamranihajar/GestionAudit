package gestionAudits.vues.espaceAdmin.gestionOrganisation;

import gestionAudits.controller.GestionOrganisationController;
import gestionAudits.models.Organisation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GestionOrganisation extends JPanel {

    private DefaultTableModel tableModel;
    private List<Object[]> data = new ArrayList<>();
    private String[] columnNames = {"Nom Organisation", "Adresse"};
    private JTable table;
    private List<Organisation> listOrganisation;

    public GestionOrganisation() {
        listOrganisation = GestionOrganisationController.listerOrganisation();

        for (Organisation o : listOrganisation)
            data.add(new Object[]{o.getNom(), o.getAdresse()});

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Titre
        JLabel titleLabel = new JLabel("Gestion des Organisations");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Bouton Ajouter une Organisation
        JButton addButton = new JButton("Ajouter Organisation");
        addButton.setBackground(new Color(7, 187, 241));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setFont(new Font("Arial", Font.PLAIN, 14));
        addButton.addActionListener(e -> showGestionOrganisationModal());

        // Panel pour le titre et le bouton
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout(10, 10));
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(addButton, BorderLayout.LINE_START);
        headerPanel.setBackground(Color.WHITE);
        add(headerPanel, BorderLayout.NORTH);

        // Table des Organisations
        tableModel = new DefaultTableModel(getDataArray(), columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Désactiver l'édition directe
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(135, 206, 250));

        // Ajouter menu contextuel pour clic droit
        JPopupMenu popupMenu = createContextMenu();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger() && table.getSelectedRow() != -1) {
                    popupMenu.show(table, e.getX(), e.getY());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private Object[][] getDataArray() {
        return data.toArray(new Object[0][]);
    }

    private void showGestionOrganisationModal() {
        GestionOrganisationModal dialog = new GestionOrganisationModal((Frame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);

        if (dialog.isSubmitted()) {
            String name = dialog.getOrganizationName();
            String address = dialog.getOrganizationAddress();

            // Ajouter les données dans le modèle de table
            data.add(new Object[]{name, address});
            tableModel.setDataVector(getDataArray(), columnNames);
         //   JOptionPane.showMessageDialog(this, "Nouvelle organisation ajoutée:\nNom: " + name + "\nAdresse: " + address);
        }
    }

    private JPopupMenu createContextMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem editMenuItem = new JMenuItem("Modifier");
        editMenuItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                onEditRow(selectedRow);
            }
        });

        JMenuItem deleteMenuItem = new JMenuItem("Supprimer");
        deleteMenuItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                onDeleteRow(selectedRow);
            }
        });

        popupMenu.add(editMenuItem);
        popupMenu.add(deleteMenuItem);
        return popupMenu;
    }

    private void onEditRow(int row) {
        String currentName = (String) tableModel.getValueAt(row, 0);
        String currentAddress = (String) tableModel.getValueAt(row, 1);
        int id = listOrganisation.get(row).getId();

        GestionOrganisationModal dialog = new GestionOrganisationModal((Frame) SwingUtilities.getWindowAncestor(this), id, currentName, currentAddress);
        dialog.setVisible(true);

        if (dialog.isSubmitted()) {
            String newName = dialog.getOrganizationName();
            String newAddress = dialog.getOrganizationAddress();
            // Mise à jour des données dans le tableau
            data.set(row, new Object[]{newName, newAddress});
            tableModel.setDataVector(getDataArray(), columnNames);
           // JOptionPane.showMessageDialog(this, "Organisation modifiée:\nNom: " + newName + "\nAdresse: " + newAddress);
        }
    }

    private void onDeleteRow(int row) {
        int confirmation = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer cette organisation ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            int id = listOrganisation.get(row).getId();
            boolean isDeleted = GestionOrganisationController.supprimerOrganisation(id);
            if (isDeleted) {
                data.remove(row);
                tableModel.setDataVector(getDataArray(), columnNames);
            }
        }
    }

}
