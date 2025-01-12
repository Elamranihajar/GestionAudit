package gestionAudits.vues.espaceAuditeur;

import gestionAudits.controller.GestionAuditsController;

import gestionAudits.controller.GestionSystemExigenceController;
import gestionAudits.models.Audit;
import gestionAudits.models.Session;
import gestionAudits.models.SystemeExigence;
import gestionAudits.vues.espaceAdmin.gestionSystemExigence.GestionSystemExigence;
import gestionAudits.vues.espaceAdmin.gestionSystemExigence.SystemExigentForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.List;

public class ListAudit extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private List<SystemeExigence> listAudits;

    public ListAudit() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Titre
        JLabel titleLabel = new JLabel("Lists des Audits");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Panel pour le titre et le bouton
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout(10, 10));
        headerPanel.add(titleLabel, BorderLayout.NORTH);
     //   headerPanel.add(addButton, BorderLayout.LINE_START);
        headerPanel.setBackground(Color.WHITE);
        add(headerPanel, BorderLayout.NORTH);


        String[] columnNames = {"ID", "description", "type","date debut","date fin","status"};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnNames);
        table = new JTable(tableModel);

        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(135, 206, 250));
        listerAudits();

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
    public void listerAudits() {
        tableModel.setRowCount(0);
        listAudits= GestionSystemExigenceController.getSystemExigence(Session.userConnected.getId());
       System.out.println(listAudits.size());
       System.out.println(Session.userConnected.getId());
        for (SystemeExigence systemeExigence : listAudits) {
            System.out.println(systemeExigence.getId());
            System.out.println(systemeExigence.getAudit().getId());
            System.out.println(systemeExigence.getAudit().getDescription());
            tableModel.addRow(new Object[]{
                    systemeExigence.getId(),
                    systemeExigence.getAudit().getDescription(),
                    systemeExigence.getAudit().getType(),
                    systemeExigence.getAudit().getDateDebut(),
                    systemeExigence.getAudit().getDateFin(),
                    systemeExigence.getAudit().getStatus()
            });
        }
    }




    private JPopupMenu createContextMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem lancerAuditMenuItem = new JMenuItem("Lancer Audit");
        lancerAuditMenuItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                lancerAudit(selectedRow);
            }
        });


        popupMenu.add(lancerAuditMenuItem);
        return popupMenu;
    }


    private SystemeExigence getInfoSAuditSelected(int selectedRow) {
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        return listAudits.stream()
                         .filter(s->s.getId()==id)
                         .findFirst().orElse(null);
    }




    private void lancerAudit(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.");
            return;
        }
        SystemeExigence systemeExigence=getInfoSAuditSelected(selectedRow);
        //modifer etat audit de planifier a En cours
        Audit audit=systemeExigence.getAudit();
        System.out.println("i : "+audit);
        if(audit.getStatus().equals("Planifie")) {
            System.out.println("i1 : "+audit);
            audit.setStatus("En cours");
            GestionAuditsController.modifierAudit(audit);
        }
        LancerAudit dialog = new LancerAudit((Frame) SwingUtilities.getWindowAncestor(this),systemeExigence);
        dialog.setVisible(true);
        systemeExigence= dialog.getSystemeExigence();
        if (dialog.isSubmit()) {
            System.out.println(systemeExigence.getId()+"  "+systemeExigence.getStatus()+"  "+systemeExigence.getConstats());
        }

    }


}
