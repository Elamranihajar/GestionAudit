package gestionAudits.vues.espaceAdmin.gestionProcessus;

import gestionAudits.controller.*;
import gestionAudits.data.GestionSiteData;
import gestionAudits.models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GestionProcessus extends JPanel {

    private DefaultTableModel tableModel;
    private List<Object[]> data = new ArrayList<>();
    private String[] columnNames = {"Nom Organisation", "Adresse"};
    private JTable table;
    private List<Organisation> organisationList;
    private List<User> auditsList;
    private List<Processus> processList;
    public GestionProcessus() {
        organisationList = GestionOrganisationController.listerOrganisation();
        auditsList= GestionUsersController.listerAudits();
        processList = GestionProcessusController.listerProcessus();

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Titre
        JLabel titleLabel = new JLabel("Gestion des processus");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Bouton Ajouter une Organisation
        JButton addButton = new JButton("Ajouter");
        addButton.setBackground(new Color(7, 187, 241));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setFont(new Font("Arial", Font.PLAIN, 14));
        addButton.addActionListener(e ->showProcessusModal());

        // Panel pour le titre et le bouton
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout(10, 10));
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(addButton, BorderLayout.LINE_START);
        headerPanel.setBackground(Color.WHITE);
        add(headerPanel, BorderLayout.NORTH);


        String[] columnNames = {"ID","nom","description","organisation","responsable"};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnNames);
        listerProcessus();
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
    public void listerProcessus() {
        processList=GestionProcessusController.listerProcessus();
        tableModel.setRowCount(0);
        for (Processus processus : processList) {
            tableModel.addRow(new Object[]{
                    processus.getId(),
                    processus.getName(),
                    processus.getDescription(),
                    processus.getOrganization().getNom(),
                    processus.getResponsable().getName()
            });
        }
    }

    private void showProcessusModal() {
        ProcessusFormModal dialog = new ProcessusFormModal((Frame) SwingUtilities.getWindowAncestor(this),null,"Ajouter");
        dialog.setVisible(true);
        Processus processus = dialog.getprocessus();
       // int id=processList.getLast().getId()+1;
        if (dialog.isSubmitted()){
           listerProcessus();
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

    private Processus getInfoProcessusSelected(int selectedRow) {
        Processus processus = new Processus();
        processus.setId(Integer.parseInt(tableModel.getValueAt(selectedRow,0).toString()));
        processus.setName(tableModel.getValueAt(selectedRow,1).toString());
        processus.setDescription(tableModel.getValueAt(selectedRow, 2).toString());
        String organisationName = tableModel.getValueAt(selectedRow, 3).toString();
        Organisation org=organisationList.stream()
                                        .filter(organisation1->organisation1.getNom().equals(organisationName))
                                        .findFirst().orElse(null);
        processus.setOrganization(org);
        String responsableName=tableModel.getValueAt(selectedRow, 4).toString();
        User responsable=auditsList.stream()
                .filter(user->user.getName().equals(responsableName))
                .findFirst().orElse(null);
        processus.setResponsable(responsable);
        return processus;
    }

    public void setInfoSystemSelected(int selectedRow, Processus processus) {
        tableModel.setValueAt(processus.getId(),selectedRow,0);
        tableModel.setValueAt(processus.getName(),selectedRow,1);
        tableModel.setValueAt(processus.getDescription(),selectedRow,2);
        tableModel.setValueAt(processus.getOrganization().getNom(),selectedRow,3);
        tableModel.setValueAt(processus.getResponsable().getName(),selectedRow,4);
    }

   private void onEditRow(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.");
            return;
        }
        Processus processus=getInfoProcessusSelected(selectedRow);
        System.out.println(processus);
        ProcessusFormModal dialog = new ProcessusFormModal((Frame) SwingUtilities.getWindowAncestor(this),processus,"Modifier");
        dialog.setVisible(true);
        processus= dialog.getprocessus();
        System.out.println(processus);
        if (dialog.isSubmitted()) {
           setInfoSystemSelected(selectedRow,processus);
        }

    }

    private void onDeleteRow(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.");
            return;
        }
        int id=getInfoProcessusSelected(selectedRow).getId();
        System.out.println(id);
        int confirmation = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer ce site ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
           boolean isDeleted =GestionProcessusController.supprimerProcessus(id);
            if (isDeleted){
                tableModel.removeRow(selectedRow);
            }

        }

    }
}
