package gestionAudits.vues.espaceAdmin.gestionSystemExigence;

import gestionAudits.controller.GestionClauseStandartController;
import gestionAudits.controller.GestionSystemExigenceController;
import gestionAudits.controller.GestionSystemManagementController;
import gestionAudits.controller.GestionUsersController;
import gestionAudits.models.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SystemExigentForm extends JDialog {

    private JComboBox<User> auditeurComboBox;
    private JComboBox<ClauseStandard> clauseStandardComboBox;
    private JComboBox<SystemeManagement> systemeManagementComboBox;
    private boolean isSubmitted = false;
    private String typeOperation;
    private Audit audit;
    private List<User> listAuditeur;
    private List<ClauseStandard> listClauseStandard;
    private List<SystemeManagement> listSystemManagement;
    private JTextField autreExigenceNameField;
    private JTextArea autreExigenceDescriptionField;
    private JComboBox<String> autreExigenceTypeComboBox;
    private SystemeExigence systemeExigence;
    private JCheckBox autreExigenceCheckbox;

    public SystemExigentForm(Frame parent, Audit audit,SystemeExigence systemeExigence) {
        super(parent, "Définir un system d'exigence", true);
        this.audit = audit;
        listAuditeur = GestionUsersController.listerAudits();
        listClauseStandard= GestionClauseStandartController.listerClauseStandard();
        listSystemManagement= GestionSystemManagementController.listeSystemeManagements();
        this.systemeExigence = systemeExigence;
        System.out.println(this.audit);
        createForm(parent);
    }

    private void createForm(Frame parent) {
        setSize(450, 350);
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
        formPanel.add(new JLabel("Auditeur :"), gbc);

        gbc.gridx = 1;
        auditeurComboBox=new JComboBox<>(listAuditeur.toArray(new User[0]));
        auditeurComboBox.setPreferredSize(new Dimension(300, 30));
        formPanel.add(auditeurComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("clause/standard :"), gbc);

        gbc.gridx = 1;
        clauseStandardComboBox = new JComboBox<>(listClauseStandard.toArray(new ClauseStandard[0]));
        clauseStandardComboBox.setPreferredSize(new Dimension(300, 30));
        formPanel.add(clauseStandardComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Système de management :"), gbc);

        gbc.gridx = 1;
        systemeManagementComboBox = new JComboBox<>(listSystemManagement.toArray(new SystemeManagement[0]));
        systemeManagementComboBox.setPreferredSize(new Dimension(300, 30));
        formPanel.add(systemeManagementComboBox, gbc);

        // Checkbox pour afficher "Autre exigence"
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        autreExigenceCheckbox = new JCheckBox("Ajouter une autre exigence");
        formPanel.add(autreExigenceCheckbox, gbc);

        // Panel pour "Autre exigence" (masqué au départ)
        JPanel autreExigencePanel = new JPanel(new GridBagLayout());
        autreExigencePanel.setBorder(BorderFactory.createTitledBorder("Autre Exigence"));
        autreExigencePanel.setVisible(false); // Masquer au départ


        GridBagConstraints subGbc = new GridBagConstraints();
        subGbc.fill = GridBagConstraints.HORIZONTAL;
        subGbc.insets = new Insets(5, 5, 5, 5);

        subGbc.gridx = 0;
        subGbc.gridy = 0;
        autreExigencePanel.add(new JLabel("Nom :"), subGbc);

        subGbc.gridx = 1;
        autreExigenceNameField = new JTextField(30);
        autreExigencePanel.add(autreExigenceNameField, subGbc);

        subGbc.gridx = 0;
        subGbc.gridy = 1;
        autreExigencePanel.add(new JLabel("Description :"), subGbc);

        subGbc.gridx = 1;
        autreExigenceDescriptionField = new JTextArea(3, 20);
        autreExigenceDescriptionField.setLineWrap(true);
        autreExigenceDescriptionField.setWrapStyleWord(true);
        autreExigencePanel.add(autreExigenceDescriptionField, subGbc);

        subGbc.gridx = 0;
        subGbc.gridy = 2;
        autreExigencePanel.add(new JLabel("Type :"), subGbc);
        subGbc.gridx = 1;
        autreExigenceTypeComboBox = new JComboBox<>(new String[]{"Type 1", "Type 2", "Type 3"});
        autreExigencePanel.add(autreExigenceTypeComboBox, subGbc);

        // Ajouter le panneau au formulaire principal
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(autreExigencePanel, gbc);

        // Action pour afficher/masquer le bloc
        autreExigenceCheckbox.addActionListener(e -> {
            setSize(550,650);
            autreExigencePanel.setVisible(autreExigenceCheckbox.isSelected());
        });

        if (systemeExigence != null) { //modifer un system d'exigence
            auditeurComboBox.setSelectedItem(systemeExigence.getAuditeur());
            clauseStandardComboBox.setSelectedItem(systemeExigence.getClauseStandard());
            systemeManagementComboBox.setSelectedItem(systemeExigence.getSystemeManagement());
            if(systemeExigence.getAutreExigence().getName()!=null) {
                autreExigenceCheckbox.setSelected(true);
                autreExigencePanel.setVisible(true);
                autreExigenceNameField.setText(systemeExigence.getAutreExigence().getName());
                autreExigenceDescriptionField.setText(systemeExigence.getAutreExigence().getDescription());
                autreExigenceTypeComboBox.setSelectedItem(systemeExigence.getAutreExigence().getType());
            }
        }


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

    private void onSubmit() {
            SystemeExigence systemeExigence=new SystemeExigence();
            systemeExigence.setAudit(audit);
            systemeExigence.setAuditeur((User) auditeurComboBox.getSelectedItem());
            systemeExigence.setClauseStandard((ClauseStandard) clauseStandardComboBox.getSelectedItem());
            systemeExigence.setSystemeManagement((SystemeManagement) systemeManagementComboBox.getSelectedItem());
        if(autreExigenceCheckbox.isSelected()) {
            AutreExigence autreExigence = new AutreExigence();
            autreExigence.setName(autreExigenceNameField.getText().trim());
            autreExigence.setDescription(autreExigenceDescriptionField.getText().trim());
            autreExigence.setType(autreExigenceTypeComboBox.getSelectedItem().toString());
            systemeExigence.setAutreExigence(autreExigence);
        };
        if(this.systemeExigence!=null) {
            systemeExigence.getAutreExigence().setId(this.systemeExigence.getAutreExigence().getId());
            systemeExigence.setId(this.systemeExigence.getId());
            GestionSystemExigenceController.modiferSystemExigence(systemeExigence);
        }else
            GestionSystemExigenceController.ajouterSystemExigence(systemeExigence);
        isSubmitted = true;
        dispose();
    }

    private void onCancel() {
        isSubmitted = false;
        dispose();
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }
}
