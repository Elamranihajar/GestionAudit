package gestionAudits.vues.espaceAdmin.gestion_systemes_management;

import gestionAudits.controller.GestionOrganisationController;
import gestionAudits.controller.GestionSitesController;
import gestionAudits.controller.GestionSystemManagementController;
import gestionAudits.controller.GestionUsersController;
import gestionAudits.data.GestionSiteData;
import gestionAudits.models.Organisation;
import gestionAudits.models.Site;
import gestionAudits.models.SystemeManagement;
import gestionAudits.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GestionSystemManagement extends JPanel {

    private DefaultTableModel tableModel;
    private List<Object[]> data = new ArrayList<>();
    private String[] columnNames = {"Nom Organisation", "Adresse"};
    private JTable table;
    private List<Organisation> organisationList;
    private List<User> auditsList;
    private List<SystemeManagement> systemeManagementList;
    public GestionSystemManagement() {
        organisationList = GestionOrganisationController.listerOrganisation();
        auditsList= GestionUsersController.listerAudits();
        systemeManagementList= GestionSystemManagementController.listeSystemeManagements();

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Titre
        JLabel titleLabel = new JLabel("Gestion des System de Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Bouton Ajouter une Organisation
        JButton addButton = new JButton("Ajouter");
        addButton.setBackground(new Color(7, 187, 241));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setFont(new Font("Arial", Font.PLAIN, 14));
        addButton.addActionListener(e ->showGestionSystemManagementModal());

        // Panel pour le titre et le bouton
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout(10, 10));
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(addButton, BorderLayout.LINE_START);
        headerPanel.setBackground(Color.WHITE);
        add(headerPanel, BorderLayout.NORTH);


        String[] columnNames = {"nom","description","nombre de personne","organisation","responsable"};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnNames);
        listerSystemManagement();
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
    public void listerSystemManagement() {
        for (SystemeManagement systemeManagement : systemeManagementList) {
            System.out.println(systemeManagement);
            tableModel.addRow(new Object[]{
                    systemeManagement.getNom(),
                    systemeManagement.getDescription(),
                    systemeManagement.getNbPersonnes(),
                    systemeManagement.getOrganisation().getNom(),
                    systemeManagement.getResponsable().getName()
            });
        }
    }

    private void showGestionSystemManagementModal() {
        SystemManagementFormModal dialog = new SystemManagementFormModal((Frame) SwingUtilities.getWindowAncestor(this),null,"Ajouter");
        dialog.setVisible(true);
        SystemeManagement systemeManagement = dialog.getSystemManagement();
        if (dialog.isSubmitted()){
           // sitesList= GestionSitesController.listerSites();
            //int newIdSite=sitesList.getLast().getId();
            tableModel.addRow(new Object[]{
                    systemeManagement.getNom(),
                    systemeManagement.getDescription(),
                    systemeManagement.getNbPersonnes(),
                    systemeManagement.getOrganisation().getNom(),
                    systemeManagement.getResponsable().getName()
            });
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

    private SystemeManagement getInfoSystemSelected(int selectedRow) {
        SystemeManagement systemeManagement = new SystemeManagement();
        systemeManagement.setId(systemeManagementList.get(selectedRow).getId());
        systemeManagement.setNom(tableModel.getValueAt(selectedRow, 0).toString());
        systemeManagement.setDescription(tableModel.getValueAt(selectedRow, 1).toString());
        systemeManagement.setNbPersonnes(Integer.parseInt(tableModel.getValueAt(selectedRow,2).toString()));
        String organisationName = tableModel.getValueAt(selectedRow, 3).toString();
        Organisation org=organisationList.stream()
                                        .filter(organisation1->organisation1.getNom().equals(organisationName))
                                        .findFirst().orElse(null);
        systemeManagement.setOrganisation(org);
        String responsableName=tableModel.getValueAt(selectedRow, 4).toString();
        User responsable=auditsList.stream()
                .filter(user->user.getName().equals(responsableName))
                .findFirst().orElse(null);
        systemeManagement.setResponsable(responsable);
        return systemeManagement;
    }

    public void setInfoSystemSelected(int selectedRow, SystemeManagement systemeManagement) {
        tableModel.setValueAt(systemeManagement.getNom(),selectedRow,0);
        tableModel.setValueAt(systemeManagement.getDescription(),selectedRow,1);
        tableModel.setValueAt(systemeManagement.getNbPersonnes(),selectedRow,2);
        tableModel.setValueAt(systemeManagement.getOrganisation().getNom(),selectedRow,3);
        tableModel.setValueAt(systemeManagement.getResponsable().getName(),selectedRow,4);
    }

   private void onEditRow(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.");
            return;
        }
        SystemeManagement systemeManagement=getInfoSystemSelected(selectedRow);

        SystemManagementFormModal dialog = new SystemManagementFormModal((Frame) SwingUtilities.getWindowAncestor(this),systemeManagement,"Modifier");
        dialog.setVisible(true);
        systemeManagement= dialog.getSystemManagement();
        System.out.println(systemeManagement);
        if (dialog.isSubmitted()) {
           setInfoSystemSelected(selectedRow,systemeManagement);
        }

    }

    private void onDeleteRow(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.");
            return;
        }
        int id=systemeManagementList.get(selectedRow).getId();
        System.out.println(id);
        int confirmation = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer ce site ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
           boolean isDeleted =GestionSystemManagementController.supprimerSysteme(id);
            if (isDeleted){
                tableModel.removeRow(selectedRow);
            }

        }

    }
}
