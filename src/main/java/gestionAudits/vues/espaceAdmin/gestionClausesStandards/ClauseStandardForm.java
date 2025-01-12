package gestionAudits.vues.espaceAdmin.gestionClausesStandards;


import gestionAudits.controller.GestionClauseStandartController;
import gestionAudits.models.Clause;
import gestionAudits.models.Standard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ClauseStandardForm extends JDialog {

    private JTextArea descriptionField;
    private JTextArea referenceField;
    private JComboBox<String> typeComboBox;
    private boolean isSubmitted = false;
    private String typeOperation;
    private String typeClause;
    private int id;

    public ClauseStandardForm(Frame parent,String title,String type,int id,String description,String reference,String typeClause) {
        super(parent, title, true);
        typeOperation=type;
        this.id=id;
        if (type.equals("Ajouter"))
            createForm(parent,type,"","","");
        else  //modifer
            createForm(parent,type,description,reference,typeClause);

    }

    private void createForm(Frame parent,String type,String description,String reference,String typeClause) {
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
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        descriptionField = new JTextArea(3, 20);
        descriptionField.setText(description);
        descriptionField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(new JScrollPane(descriptionField), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Référence:"), gbc);

        gbc.gridx = 1;
        referenceField = new JTextArea(3, 20);
        referenceField.setText(reference);
        referenceField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(new JScrollPane(referenceField), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Type:"), gbc);

        gbc.gridx = 1;
        typeComboBox = new JComboBox<>(new String[]{"Standard", "Clause"});
        typeComboBox.setSelectedItem(typeClause);
        formPanel.add(typeComboBox, gbc);

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
        if (validateFields()) {
            typeClause = (String) typeComboBox.getSelectedItem();
            assert  typeClause != null;
            if(typeOperation.equals("Ajouter"))
                ajouter();
            else
                modifier();

            isSubmitted = true;
            dispose();
        }
    }

    private void ajouter(){
        if (typeClause.equals("Clause"))
            GestionClauseStandartController.ajouterClause(new Clause(
                    getDescription(),
                    getReference()
            ));
        else if (typeClause.equals("Standard"))
            GestionClauseStandartController.ajouterStandart(new Standard(
                    getDescription(),
                    getReference()
            ));
    }
    private void modifier(){
        if (typeClause.equals("Clause"))
            GestionClauseStandartController.modifierClause(new Clause(
                    id,
                    getDescription(),
                    getReference()
            ));
        else if (typeClause.equals("Standard"))
            GestionClauseStandartController.modifierStandard(new Standard(
                    id,
                    getDescription(),
                    getReference()
            ));
    }

    private void onCancel() {
        isSubmitted = false;
        dispose();
    }

    private boolean validateFields() {
        if (descriptionField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La description est requise.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (referenceField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La référence est requise.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }

    public String getDescription() {
        return descriptionField.getText().trim();
    }

    public String getReference() {
        return referenceField.getText().trim();
    }

    public String getClauseType() {
        return typeClause;
    }


}
