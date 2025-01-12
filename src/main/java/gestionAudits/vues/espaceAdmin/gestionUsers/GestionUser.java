package gestionAudits.vues.espaceAdmin.gestionUsers;

import gestionAudits.controller.GestionUsersController;
import gestionAudits.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GestionUser extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private List<User> usersList;

    public GestionUser() {

        usersList=GestionUsersController.getUsers();

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Titre
        JLabel titleLabel = new JLabel("Gestion des Users");
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


        String[] columnNames = {"username", "email","role"};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnNames);
        listerUsers();
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



    public void listerUsers() {
        usersList=GestionUsersController.getUsers();
        for (User user : usersList) {
            String role=user.getRoleId()==1 ? "admin" : "auditeur";
            tableModel.addRow(new Object[]{user.getName(), user.getEmail(), role});
        }
    }


    private void showGestionSiteModal() {
        UserFormModal dialog = new UserFormModal((Frame) SwingUtilities.getWindowAncestor(this),null,"Ajouter");
        dialog.setVisible(true);
        User user = dialog.getUser();
        if (dialog.isSubmitted()){
            System.out.println(user);
            String role=user.getRoleId()==1 ? "admin" : "auditeur";
            tableModel.addRow(new Object[]{user.getName(),user.getEmail(),role});
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

    private User getInfoUserSelected(int selectedRow) {
        String name = (String) tableModel.getValueAt(selectedRow, 0);
        String email = (String) tableModel.getValueAt(selectedRow, 1);
        String role = (String) tableModel.getValueAt(selectedRow, 2);
        User user=new User();
        user.setName(name);
        user.setEmail(email);
        user.setRoleId(role.equals("admin")?1:2);
        return user;
    }

    private void onEditRow(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.");
            return;
        }
        User user=getInfoUserSelected(selectedRow);
        int id=usersList.get(selectedRow).getId();
        user.setId(id);
        System.out.println(user);
        UserFormModal dialog = new UserFormModal((Frame) SwingUtilities.getWindowAncestor(this),user,"Modifier");
        dialog.setVisible(true);
        user= dialog.getUser();
        System.out.println(user);
        if (dialog.isSubmitted()) {
            System.out.println(user);
            tableModel.setValueAt(user.getName(),selectedRow,0);
            tableModel.setValueAt(user.getEmail(),selectedRow,1);
            tableModel.setValueAt(user.getRoleId()==1?"admin":"auditeur" ,selectedRow,2);
        }

    }

    private void onDeleteRow(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.");
            return;
        }
        User user=getInfoUserSelected(selectedRow);
       // System.out.println(site);
        int confirmation = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer ce site ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            System.out.println(user);
            //boolean isDeleted = GestionSitesController.suppprimerSite(site.getId());
            int id=usersList.get(selectedRow).getId();
            boolean isDeleted=GestionUsersController.supprimerUser(id);
            if (isDeleted)
                tableModel.removeRow(selectedRow);
        }

    }
}
