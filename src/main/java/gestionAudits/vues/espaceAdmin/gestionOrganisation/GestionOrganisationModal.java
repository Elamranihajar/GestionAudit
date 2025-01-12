package gestionAudits.vues.espaceAdmin.gestionOrganisation;

import gestionAudits.controller.GestionOrganisationController;
import gestionAudits.models.Organisation;

import javax.swing.*;
import java.awt.*;

public class GestionOrganisationModal extends JDialog {

    private JTextField nameField;
    private JTextField addressField;
    private int typeOpration;//ajouter=1/modifier=2
    private boolean isSubmitted;
    private int id;
    public GestionOrganisationModal(Frame parent,int id,String name, String address) {
        this(parent);
        nameField.setText(name);
        addressField.setText(address);
        this.id=id;
        System.out.println("name: "+name+" address: "+address);
        this.typeOpration=2;
    }


    public GestionOrganisationModal(Frame parent) {
        super(parent,"Gestion Organisation", true);
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(parent);
        this.typeOpration=1;
        // Panel du formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Label et champ pour le nom de l'organisation
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nom de l'Organisation:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        // Label et champ pour l'adresse
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Adresse:"), gbc);
        gbc.gridx = 1;
        addressField = new JTextField(20);
        formPanel.add(addressField, gbc);




        // Boutons de contrôle
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitButton = new JButton(typeOpration==1 ? "Ajouter" : "Modifier" );
        JButton cancelButton = new JButton("Annuler");

        submitButton.setBackground(new Color(7, 187, 241));
        submitButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(255, 99, 71));
        cancelButton.setForeground(Color.WHITE);

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Gestion des actions
        submitButton.addActionListener(e -> onSubmit());
        cancelButton.addActionListener(e -> onCancel());
    }

    private void onSubmit() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        System.out.println(typeOpration);
        // Validation des champs
        if (name.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "tous les champs sont requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Si tout est valide
        isSubmitted = true;
        boolean resultat=false;
        if(typeOpration==2)
            resultat= GestionOrganisationController.modifierOrganisation(new Organisation(id,name,address));
        else
            resultat= GestionOrganisationController.ajouterOrganisation(new Organisation(name,address));
        if(resultat)
            JOptionPane.showMessageDialog(this, "Organisation ajoutée: " + name);
        else
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'organisation: " + name, "Erreur", JOptionPane.ERROR_MESSAGE);
        dispose();
    }

    private void onCancel() {
        isSubmitted = false;
        dispose();
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }

    public String getOrganizationName() {
        return nameField.getText().trim();
    }

    public String getOrganizationAddress() {
        return addressField.getText().trim();
    }
}
