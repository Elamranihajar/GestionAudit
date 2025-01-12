package gestionAudits.vues.espaceAdmin.gestionSystemExigence;

import gestionAudits.controller.GestionSitesController;
import gestionAudits.controller.GestionSystemExigenceController;
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

public class GestionSystemExigence1 extends JDialog {

    private DefaultTableModel tableModel;
    private JTable table;
    private List<SystemeExigence> systemeExigences;
    private Audit audit;
    public GestionSystemExigence1(Frame parent,Audit audit) {
        super(parent,"détail sur audit : "+audit.getDescription(), true);


       this.audit = audit;

        setLayout(new BorderLayout(10, 10));
        setSize(550, 750);
        setLocationRelativeTo(parent);
        setBackground(Color.WHITE);

        // Titre
        JLabel titleLabel = new JLabel("Gestion des Systemes Exigences");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Bouton Ajouter une Organisation
        JButton addButton = new JButton("Ajouter");
        addButton.setBackground(new Color(7, 187, 241));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setFont(new Font("Arial", Font.PLAIN, 14));
        addButton.addActionListener(e ->showGestionSytemModal());

        // Panel pour le titre et le bouton
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout(10, 10));
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(addButton, BorderLayout.LINE_START);
        headerPanel.setBackground(Color.WHITE);
        add(headerPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID","Statut","Exclusion","Motif Exclusion"};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnNames);
        listerSystemesExigences(this.audit);
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
    public void listerSystemesExigences(Audit audit) {
        systemeExigences= GestionSystemExigenceController.getSystemExigence(audit);
        for (SystemeExigence systemeExigence : systemeExigences) {
            tableModel.addRow(new Object[]{
                    systemeExigence.getId(),
                    systemeExigence.getStatus(),
                    systemeExigence.isExclu() ? "exclu" : "non exclu",
                    systemeExigence.getMotifExclusion()
            });
        }
    }


    private void showGestionSytemModal() {
//        SiteFormModal dialog = new SiteFormModal((Frame) SwingUtilities.getWindowAncestor(this),null,"Ajouter");
//        dialog.setVisible(true);
//        Site site = dialog.getSite();
//        if (dialog.isSubmitted()){
//            sitesList= GestionSitesController.listerSites();
//            int newIdSite=sitesList.getLast().getId();
//            tableModel.addRow(new Object[]{newIdSite,site.getName(),site.getAddress(),site.getDescription(),site.getOrganization().getNom(),site.getOrganization().getAdresse()});
//        }
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

        JMenuItem detailsMenuItem = new JMenuItem("plus details");
        detailsMenuItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                showDetails(selectedRow);
            }
        });


        popupMenu.add(editMenuItem);
        popupMenu.add(deleteMenuItem);
        popupMenu.add(detailsMenuItem);
        return popupMenu;
    }

    private void showDetails(int selectedRow) {
        SystemeExigence systemeExigence =getInfoSystemSelected(selectedRow);
        SystemeExigenceDetails systemeExigenceDetails=new SystemeExigenceDetails((Frame) SwingUtilities.getWindowAncestor(this),systemeExigence);
        systemeExigenceDetails.setVisible(true);
    }

    private SystemeExigence getInfoSystemSelected(int selectedRow) {
        int id = (int) tableModel.getValueAt(selectedRow, 0);


        return systemeExigences.stream()
                               .filter(s->s.getId()==id)
                               .findFirst().orElse(null);
    }




    private void onEditRow(int selectedRow) {
//        if (selectedRow == -1) {
//            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.");
//            return;
//        }
//        Site site=getInfoSiteSelected(selectedRow);
//        SiteFormModal dialog = new SiteFormModal((Frame) SwingUtilities.getWindowAncestor(this),site,"Modifier");
//        dialog.setVisible(true);
//        site= dialog.getSite();
//        System.out.println(site);
//        if (dialog.isSubmitted()) {
//            tableModel.setValueAt(site.getId(),selectedRow,0);
//            tableModel.setValueAt(site.getName(),selectedRow,1);
//            tableModel.setValueAt(site.getAddress(),selectedRow,2);
//            tableModel.setValueAt(site.getDescription(),selectedRow,3);
//            tableModel.setValueAt(site.getOrganization().getNom(),selectedRow,4);
//            tableModel.setValueAt(site.getOrganization().getAdresse(),selectedRow,5);
//        }

    }

    private void onDeleteRow(int selectedRow) {
//        if (selectedRow == -1) {
//            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.");
//            return;
//        }
//        Site site=getInfoSiteSelected(selectedRow);
//        System.out.println(site);
//        int confirmation = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer ce site ?", "Confirmation", JOptionPane.YES_NO_OPTION);
//        if (confirmation == JOptionPane.YES_OPTION) {
//            boolean isDeleted = GestionSitesController.suppprimerSite(site.getId());
//            if (isDeleted){
//                tableModel.removeRow(selectedRow);
//            }
//
//        }

    }
}
