package gestionAudits.vues.espaceAdmin.gestionSites;

import gestionAudits.controller.GestionSitesController;
import gestionAudits.data.GestionSiteData;
import gestionAudits.models.Organisation;
import gestionAudits.models.Site;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GestionSite extends JPanel {

    private DefaultTableModel tableModel;
    private List<Object[]> data = new ArrayList<>();
    private String[] columnNames = {"Nom Organisation", "Adresse"};
    private JTable table;
    private List<Site> sitesList;

    public GestionSite() {
        sitesList = GestionSitesController.listerSites();

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Titre
        JLabel titleLabel = new JLabel("Gestion des Sites");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Bouton Ajouter une Organisation
        JButton addButton = new JButton("Ajouter");
        addButton.setBackground(new Color(7, 187, 241));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setFont(new Font("Arial", Font.PLAIN, 14));
        addButton.addActionListener(e ->showGestionSiteModal());

        // Panel pour le titre et le bouton
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout(10, 10));
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(addButton, BorderLayout.LINE_START);
        headerPanel.setBackground(Color.WHITE);
        add(headerPanel, BorderLayout.NORTH);


        String[] columnNames = {"ID", "name", "address", "description","organisation","address organisation"};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnNames);
        listerSites();
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
    public void listerSites() {
        sitesList= GestionSitesController.listerSites();

        for (Site site : sitesList)
            tableModel.addRow(new Object[]{site.getId(),site.getName(),site.getAddress(),site.getDescription(),site.getOrganization().getNom(),site.getOrganization().getAdresse()});
    }


    private void showGestionSiteModal() {
        SiteFormModal dialog = new SiteFormModal((Frame) SwingUtilities.getWindowAncestor(this),null,"Ajouter");
        dialog.setVisible(true);
        Site site = dialog.getSite();
        if (dialog.isSubmitted()){
            sitesList= GestionSitesController.listerSites();
            int newIdSite=sitesList.getLast().getId();
            tableModel.addRow(new Object[]{newIdSite,site.getName(),site.getAddress(),site.getDescription(),site.getOrganization().getNom(),site.getOrganization().getAdresse()});
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

    private Site getInfoSiteSelected(int selectedRow) {
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        String address = (String) tableModel.getValueAt(selectedRow, 2);
        String description = (String) tableModel.getValueAt(selectedRow, 3);
        String organisation = (String) tableModel.getValueAt(selectedRow, 4);
        String addressOrg = (String) tableModel.getValueAt(selectedRow, 5);
        return new Site(id,name,address,description,new Organisation(organisation,addressOrg));
    }

    private void onEditRow(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.");
            return;
        }
        Site site=getInfoSiteSelected(selectedRow);
        SiteFormModal dialog = new SiteFormModal((Frame) SwingUtilities.getWindowAncestor(this),site,"Modifier");
        dialog.setVisible(true);
        site= dialog.getSite();
        System.out.println(site);
        if (dialog.isSubmitted()) {
            tableModel.setValueAt(site.getId(),selectedRow,0);
            tableModel.setValueAt(site.getName(),selectedRow,1);
            tableModel.setValueAt(site.getAddress(),selectedRow,2);
            tableModel.setValueAt(site.getDescription(),selectedRow,3);
            tableModel.setValueAt(site.getOrganization().getNom(),selectedRow,4);
            tableModel.setValueAt(site.getOrganization().getAdresse(),selectedRow,5);
        }

    }

    private void onDeleteRow(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.");
            return;
        }
        Site site=getInfoSiteSelected(selectedRow);
        System.out.println(site);
        int confirmation = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer ce site ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            boolean isDeleted = GestionSitesController.suppprimerSite(site.getId());
            if (isDeleted){
                tableModel.removeRow(selectedRow);
            }

        }

    }
}
