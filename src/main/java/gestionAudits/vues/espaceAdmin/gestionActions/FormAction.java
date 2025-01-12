package gestionAudits.vues.espaceAdmin.gestionActions;

import com.toedter.calendar.JCalendar;
import gestionAudits.controller.GestionActionController;
import gestionAudits.controller.GestionUsersController;
import gestionAudits.data.GestionActionData;
import gestionAudits.models.*;
import gestionAudits.models.Action;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FormAction extends JDialog {

    private JTextField nom;
    private JTextArea description;
    private JTextField dateDebutField;
    private JTextField dateFinField;

    private boolean isSubmitted = false;
    private String typeOperation;
    private Action action;
    private List<User> userIntervenants = GestionUsersController.getUsers();
    private List<String> roles = List.of("Intervenant", "Responsable Exécution", "Responsable Processus", "Responsable Systeme Management");
    private ActionIntervenant actionIntervenant=new ActionIntervenant();
    private DefaultTableModel intervenantsTableModel;
    private SystemeExigence systemeExigence=new SystemeExigence();
    private List<ActionIntervenant> actionIntervenants=new ArrayList<>();
    public FormAction(Frame parent, Action action, String typeOperation,SystemeExigence systemeExigence) {
        super(parent, typeOperation + " Action", true);
        this.typeOperation = typeOperation;
        this.action = action;
        this.systemeExigence=systemeExigence;
        createForm(parent);
    }

    private void createForm(Frame parent) {
        setSize(600, 800);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Labels et champs

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nom :"), gbc);

        gbc.gridx = 1;
        nom = new JTextField();
        nom.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(nom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Description :"), gbc);

        gbc.gridx = 1;
        description = new JTextArea(3, 20);
        description.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(description, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Date début :"), gbc);

        gbc.gridx = 1;
        dateDebutField = new JTextField(20);
        formPanel.add(dateDebutField, gbc);
        addCalendarPopup(dateDebutField);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Date fin :"), gbc);

        gbc.gridx = 1;
        dateFinField = new JTextField(20);
        formPanel.add(dateFinField, gbc);
        addCalendarPopup(dateFinField);


        intervenantsTableModel = new DefaultTableModel(new Object[]{"Nom", "Rôle"}, 0);
        JTable intervenantsTable = new JTable(intervenantsTableModel);
        JScrollPane tableScrollPane = new JScrollPane(intervenantsTable);
        tableScrollPane.setPreferredSize(new Dimension(400, 100));

        if (typeOperation.equals("Modifier")) {
            nom.setText(this.action.getNom());
            description.setText(this.action.getDescription());
            dateDebutField.setText(this.action.getDateDebut().toString());
            dateFinField.setText(this.action.getDateFin().toString());
            listerIntervenant(action.getId());
        }

        // Section pour les intervenants
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Intervenant :"), gbc);

        JComboBox<User> intervenantsComboBox = new JComboBox<>(userIntervenants.toArray(new User[0]));
        gbc.gridx = 1;
        formPanel.add(intervenantsComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Rôle :"), gbc);

        JComboBox<String> rolesComboBox = new JComboBox<>(roles.toArray(new String[0]));
        gbc.gridx = 1;
        formPanel.add(rolesComboBox, gbc);

        JButton addIntervenantButton = new JButton("Ajouter Intervenant");
        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(addIntervenantButton, gbc);
        addIntervenantButton.setBackground(new Color(34, 220, 11));
        addIntervenantButton.setForeground(Color.WHITE);

        addIntervenantButton.addActionListener(e -> {
            User selectedUser = (User) intervenantsComboBox.getSelectedItem();
            String selectedRole = (String) rolesComboBox.getSelectedItem();
            System.out.println(selectedUser.string());
            System.out.println(selectedRole);


            if (selectedUser != null && selectedRole != null) {
                actionIntervenant=new ActionIntervenant();
                actionIntervenant.setAction(action);
                actionIntervenant.setIntervenant(selectedUser);
                actionIntervenant.setRole(selectedRole);
                System.out.println(actionIntervenant);
                actionIntervenants.add(actionIntervenant);
                if(this.typeOperation.equals("Modifier"))
                    GestionActionController.ajouterActionIntervenant(actionIntervenant);
                intervenantsTableModel.insertRow(0,new Object[]{selectedUser.getName(), selectedRole});
            }
        });

        // Table des intervenants


        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        formPanel.add(tableScrollPane, gbc);

// Création d'un JPopupMenu pour la table des intervenants
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteMenuItem = new JMenuItem("Supprimer");
        popupMenu.add(deleteMenuItem);

// Ajouter un écouteur de souris pour détecter le clic droit sur la table
        intervenantsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }

            private void showPopup(MouseEvent e) {
                if (e.isPopupTrigger() && intervenantsTable.getSelectedRow() != -1) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

// Action pour supprimer une ligne lorsque "Supprimer" est sélectionné
        deleteMenuItem.addActionListener(e -> {
            int selectedRow = intervenantsTable.getSelectedRow();
            if (selectedRow != -1) {
                // Supprimer de la table
                String intervenantName = (String) intervenantsTableModel.getValueAt(selectedRow, 0);
                intervenantsTableModel.removeRow(selectedRow);

                ActionIntervenant actionIntervenant=actionIntervenants.stream()
                                                                        .filter(a->a.getIntervenant().getName().equals(intervenantName))
                                                                        .findFirst().orElse(null);


                if(actionIntervenant!=null)
                    GestionActionController.supprimerActionIntervenant(actionIntervenant.getId());

            } else {
                JOptionPane.showMessageDialog(this,
                        "Veuillez sélectionner un intervenant à supprimer.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });




        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitButton = new JButton("Valider");
        submitButton.setBackground(new Color(7, 187, 241));
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(e -> onSubmit());

        JButton cancelButton = new JButton("Annuler");
        cancelButton.setBackground(new Color(255, 99, 71));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> onCancel());

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void listerIntervenant(int actionId) {

            actionIntervenants=GestionActionController.listerActionIntervenants(actionId);
            actionIntervenants.forEach(actionIntervenant -> {
                intervenantsTableModel.addRow(new Object[]{
                        actionIntervenant.getIntervenant().getName(),
                        actionIntervenant.getRole()
                });
            });
    }

    private void addCalendarPopup(JTextField textField) {
        textField.setEditable(false);
        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JDialog calendarDialog = new JDialog();
                calendarDialog.setModal(true);
                calendarDialog.setSize(300, 300);
                calendarDialog.setLocationRelativeTo(textField);

                JCalendar calendar = new JCalendar();
                calendarDialog.add(calendar);

                calendar.addPropertyChangeListener("calendar", evt -> {
                    textField.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getDate()));
                    calendarDialog.dispose();
                });

                calendarDialog.setVisible(true);
            }
        });
    }

    private void onSubmit() {
        if (validateFields()) {
            if (action == null)
                action = new Action();

            action.setNom(nom.getText());
            action.setDescription(description.getText());
            action.setStatus("En cours");
            try {
                action.setDateDebut(Date.valueOf(dateDebutField.getText().trim()));
                action.setDateFin(Date.valueOf(dateFinField.getText().trim()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            action.setSystemeExigence(systemeExigence);
            actionIntervenants.forEach(System.out::println);
            if(this.typeOperation.equals("Ajouter"))
                isSubmitted=GestionActionController.ajouterAction(action,actionIntervenants);
            else
                isSubmitted=GestionActionController.modifierAction(action);
            dispose();
        }
    }

    public Action getAction() {
        return action;
    }

    private boolean validateFields() {
        if (nom.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom est requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (description.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La description est requise.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (dateDebutField.getText().isEmpty() || dateFinField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Les dates sont requises.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            if (Date.valueOf(dateDebutField.getText().trim()).
                    after(Date.valueOf(dateFinField.getText().trim()))) {
                JOptionPane.showMessageDialog(this, "La date de début doit être avant la date de fin.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Format de date invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void onCancel() {
        isSubmitted = false;
        dispose();
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }
}
