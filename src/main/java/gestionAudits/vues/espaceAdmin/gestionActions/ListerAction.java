package gestionAudits.vues.espaceAdmin.gestionActions;

import gestionAudits.controller.GestionActionController;
import gestionAudits.controller.GestionAuditsController;

import gestionAudits.controller.GestionSystemExigenceController;
import gestionAudits.models.Action;
import gestionAudits.models.Audit;
import gestionAudits.models.SystemeExigence;
import gestionAudits.vues.espaceAdmin.gestionSystemExigence.GestionSystemExigence;
import gestionAudits.vues.espaceAdmin.gestionSystemExigence.SystemExigentForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.List;

public class ListerAction extends JDialog {

    private DefaultTableModel tableModel;
    private JTable table;
    private List<Audit> auditsList;
    private SystemeExigence systemeExigence;



    public ListerAction(Frame parent,SystemeExigence systemeExigence) {
        super(parent, "Liste des actions ", true);
        setSize(450, 350);


        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setLocationRelativeTo(parent);
        this.systemeExigence=systemeExigence;

        System.out.println(systemeExigence);

        // Titre
        JLabel titleLabel = new JLabel("Gestion des Actions");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Bouton Ajouter une Organisation
        JButton addButton = new JButton("Ajouter");
        addButton.setBackground(new Color(7, 187, 241));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setFont(new Font("Arial", Font.PLAIN, 14));
        addButton.addActionListener(e -> showFormAction());

        // Panel pour le titre et le bouton
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout(10, 10));
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(addButton, BorderLayout.LINE_START);
        headerPanel.setBackground(Color.WHITE);
        add(headerPanel, BorderLayout.NORTH);


        String[] columnNames = {"ID","nom","description","date debut","date fin"};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnNames);
        table = new JTable(tableModel);

        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(135, 206, 250));
        listerAction();

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
    public void listerAction() {
        tableModel.setRowCount(0);
        List<Action> list=GestionActionController.listerActions(systemeExigence.getId());
        for(Action action:list){
            tableModel.addRow(new Object[]{
                    action.getId(),
                    action.getNom(),
                    action.getDescription(),
                    action.getDateDebut().toString(),
                    action.getDateFin().toString()
            });
        }
    }


    private void showFormAction() {
        FormAction dialog = new FormAction((Frame) SwingUtilities.getWindowAncestor(this),null,"Ajouter",systemeExigence);
        dialog.setVisible(true);
        if(dialog.isSubmitted())
            listerAction();
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

        JMenuItem exigenceMenuItem = new JMenuItem("List des intervenant");
        exigenceMenuItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
             //   definieSystemExigence(selectedRow);
            }
        });



        popupMenu.add(editMenuItem);
        popupMenu.add(deleteMenuItem);
        popupMenu.add(exigenceMenuItem);
        return popupMenu;
    }

//    private void showDetailsAudit(int selectedRow) {
//        Audit audit=getInfoSAuditSelected(selectedRow);
//        List<SystemeExigence> list=GestionSystemExigenceController.getSystemExigence(audit);
//        if(list!=null && !list.isEmpty()){
//            GestionSystemExigence dialog = new GestionSystemExigence((Frame) SwingUtilities.getWindowAncestor(this), list);
//            dialog.setVisible(true);
//        }else
//            JOptionPane.showMessageDialog(this,
//                    "Aucun détail pour cet audit.",
//                    "Information",
//                    JOptionPane.INFORMATION_MESSAGE);
//    }

//    private void definieSystemExigence(int selectedRow) {
//        Audit audit=getInfoSAuditSelected(selectedRow);
//        SystemExigentForm dialog = new SystemExigentForm((Frame) SwingUtilities.getWindowAncestor(this), audit,null);
//        dialog.setVisible(true);
//    }

    private Action getInfoSActoinSelected(int selectedRow) {

        Action action=new Action();
        action.setId((int) tableModel.getValueAt(selectedRow,0));
        action.setNom((String) tableModel.getValueAt(selectedRow,1) );
        action.setDescription((String) tableModel.getValueAt(selectedRow,2));
        action.setDateDebut(Date.valueOf((String) tableModel.getValueAt(selectedRow,3) ));
        action.setDateFin(Date.valueOf((String) tableModel.getValueAt(selectedRow,4) ));
        return action;
    }




    private void onEditRow(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.");
            return;
        }
        Action action=getInfoSActoinSelected(selectedRow);

        FormAction dialog = new FormAction((Frame) SwingUtilities.getWindowAncestor(this),action,"Modifier",systemeExigence);
        dialog.setVisible(true);
        if (dialog.isSubmitted()) {
            listerAction();
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
        Action action=getInfoSActoinSelected(selectedRow);
        if(!GestionActionController.supprimerAction(action.getId()))
            JOptionPane.showMessageDialog(this,"Erreur lors de suppression","Erreur",JOptionPane.ERROR_MESSAGE);
        listerAction();
    }
}
