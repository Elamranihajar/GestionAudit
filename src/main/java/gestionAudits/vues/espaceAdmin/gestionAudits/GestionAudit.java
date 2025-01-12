package gestionAudits.vues.espaceAdmin.gestionAudits;

import gestionAudits.controller.GestionAuditsController;

import gestionAudits.controller.GestionSystemExigenceController;
import gestionAudits.models.Audit;
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

public class GestionAudit extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private List<Audit> auditsList;

    public GestionAudit() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Titre
        JLabel titleLabel = new JLabel("Gestion des Audits");
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


        String[] columnNames = {"ID", "description", "type","date debut","date fin","status"};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnNames);
        //listerAudits();
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
        auditsList= GestionAuditsController.listAudits();
        for (Audit audit : auditsList)
            tableModel.addRow(new Object[]{
                    audit.getId(),
                    audit.getDescription(),
                    audit.getType(),
                    audit.getDateDebut().toString(),
                    audit.getDateFin().toString(),
                    audit.getStatus()
            });
    }


    private void showGestionSiteModal() {
        AuditFormModal dialog = new AuditFormModal((Frame) SwingUtilities.getWindowAncestor(this),null,"Ajouter");
        dialog.setVisible(true);
        Audit audit = dialog.getAudit();
        if (dialog.isSubmitted()){
            GestionAuditsController.ajouterAudit(audit);
            listerAudits();
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

        JMenuItem exigenceMenuItem = new JMenuItem("Défine système d'exigence");
        exigenceMenuItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                definieSystemExigence(selectedRow);
            }
        });

        JMenuItem detailAuditMenuItem = new JMenuItem("plus de détails");
        detailAuditMenuItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                showDetailsAudit(selectedRow);
            }
        });



        popupMenu.add(editMenuItem);
        popupMenu.add(deleteMenuItem);
        popupMenu.add(exigenceMenuItem);
        popupMenu.add(detailAuditMenuItem);
        return popupMenu;
    }

    private void showDetailsAudit(int selectedRow) {
        Audit audit=getInfoSAuditSelected(selectedRow);
        List<SystemeExigence> list=GestionSystemExigenceController.getSystemExigence(audit);
        if(list!=null && !list.isEmpty()){
            System.out.println(list.size());
            GestionSystemExigence dialog = new GestionSystemExigence((Frame) SwingUtilities.getWindowAncestor(this), list);
            dialog.setVisible(true);
        }else
            JOptionPane.showMessageDialog(this,
                    "Aucun détail pour cet audit.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
    }

    private void definieSystemExigence(int selectedRow) {
        Audit audit=getInfoSAuditSelected(selectedRow);
        SystemExigentForm dialog = new SystemExigentForm((Frame) SwingUtilities.getWindowAncestor(this), audit,null);
        dialog.setVisible(true);
    }

    private Audit getInfoSAuditSelected(int selectedRow) {
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String description = (String) tableModel.getValueAt(selectedRow, 1);
        String type = (String) tableModel.getValueAt(selectedRow, 2);
        Date dateDebut = Date.valueOf((String) tableModel.getValueAt(selectedRow, 3));
        Date dateFin = Date.valueOf((String) tableModel.getValueAt(selectedRow, 4));
        String status = (String) tableModel.getValueAt(selectedRow, 5);
        return new Audit(id,description,type,dateDebut,dateFin,status);
    }




    private void onEditRow(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.");
            return;
        }
        Audit audit=getInfoSAuditSelected(selectedRow);
        System.out.println(audit);
        AuditFormModal dialog = new AuditFormModal((Frame) SwingUtilities.getWindowAncestor(this),audit,"Modifier");
        dialog.setVisible(true);
        audit= dialog.getAudit();
        if (dialog.isSubmitted()) {
            System.out.println("mod : "+audit);
            GestionAuditsController.modifierAudit(audit);
            setInfoAuditSelected(selectedRow,audit);
        }

    }

    public void setInfoAuditSelected(int selectedRow, Audit audit) {
        tableModel.setValueAt(audit.getId(),selectedRow,0);
        tableModel.setValueAt(audit.getDescription(),selectedRow,1);
        tableModel.setValueAt(audit.getType(),selectedRow,2);
        tableModel.setValueAt(audit.getDateDebut().toString(),selectedRow,3);
        tableModel.setValueAt(audit.getDateFin().toString(),selectedRow,4);
        tableModel.setValueAt(audit.getStatus(),selectedRow,5);
    }

    private void onDeleteRow(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.");
            return;
        }
        Audit audit=getInfoSAuditSelected(selectedRow);

        int confirmation = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer ce site ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            boolean isDeleted =GestionAuditsController.supprimerAudit(audit.getId());
            if (isDeleted){
                tableModel.removeRow(selectedRow);
            }

        }

    }
}
