package gestionAudits.vues.espaceAdmin.gestion_systemes_management;


import gestionAudits.controller.GestionOrganisationController;
import gestionAudits.controller.GestionSitesController;
import gestionAudits.controller.GestionSystemManagementController;
import gestionAudits.controller.GestionUsersController;
import gestionAudits.models.Organisation;
import gestionAudits.models.Site;
import gestionAudits.models.SystemeManagement;
import gestionAudits.models.User;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SystemManagementFormModal extends JDialog {
  
    private JTextField name;
    private JTextArea description;
    private JTextField nbrPersonnel;
    private JComboBox<String> organisation;
    private JComboBox<String> responsable;
    private boolean isSubmitted = false;
    private String typeOperation;
    private int idSystem;
    private List<Organisation> organisationList;
    private List<User> listAudits;
    private SystemeManagement systemeManagement;

    public SystemManagementFormModal(Frame parent,SystemeManagement systemeManagement,String typeOperation) {
        super(parent, STR."\{typeOperation} user", true);
        organisationList=GestionOrganisationController.listerOrganisation();
        listAudits=GestionUsersController.listerAudits();
        this.typeOperation = typeOperation;
        idSystem=systemeManagement!=null ? systemeManagement.getId() : 0;
        createForm(parent,typeOperation,systemeManagement);

    }

    private void createForm(Frame parent,String typeOperation,SystemeManagement systemeManagement) {
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
        formPanel.add(new JLabel("nom:"), gbc);
        gbc.gridx = 1;
        name = new JTextField(20);
        name.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(name, gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("description:"), gbc);
        gbc.gridx = 1;
        description = new JTextArea(3,20);
        description.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(description, gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("nombre de personnes:"), gbc);
        gbc.gridx = 1;
        nbrPersonnel = new JTextField(20);
        nbrPersonnel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(nbrPersonnel, gbc);


        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("organisation:"), gbc);
        List<String> listOrganisationNames=organisationList.stream()
                .map(Organisation::getNom).toList();
        gbc.gridx = 1;
        organisation = new JComboBox<>(listOrganisationNames.toArray(new String[0]));
        formPanel.add(organisation, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("responsable:"), gbc);
        List<String> listResponsableNames=listAudits.stream()
                                                    .map(User::getName)
                                                    .toList();
        gbc.gridx = 1;
        responsable = new JComboBox<>(listResponsableNames.toArray(new String[0]));
        formPanel.add(responsable, gbc);


        if (typeOperation.equals("Modifier")){
            name.setText(systemeManagement.getNom());
            description.setText(systemeManagement.getDescription());
            nbrPersonnel.setText(String.valueOf(systemeManagement.getNbPersonnes()));
            organisation.setSelectedItem(systemeManagement.getOrganisation().getNom());
            responsable.setSelectedItem(systemeManagement.getResponsable().getName());
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

    public SystemeManagement getSystemManagement() {
        return systemeManagement;
    }

    private void onSubmit() {

        if (validateFields()) {
            systemeManagement=new SystemeManagement();
            systemeManagement.setId(idSystem);
            systemeManagement.setNom(getName());
            systemeManagement.setDescription(getDescription());
            systemeManagement.setNbPersonnes(getNbPersonnes());
            Organisation org=organisationList.stream()
                                             .filter(organisation1->organisation1.getNom().equals(getOrganisation()))
                                             .findFirst().orElse(null);
            systemeManagement.setOrganisation(org);
            User responsable=listAudits.stream()
                                        .filter(user->user.getName().equals(getResponsable()))
                                        .findFirst().orElse(null);
            systemeManagement.setResponsable(responsable);
            if(typeOperation.equals("Ajouter"))
                GestionSystemManagementController.ajouterSystem(systemeManagement);
            else
                GestionSystemManagementController.modifierSystem(systemeManagement);

            isSubmitted = true;
            dispose();
        }
    }


    private void onCancel() {
        isSubmitted = false;
        dispose();
    }

    private boolean validateFields() {
        if (name.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom est requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (description.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "L'adresse e-mail est requise.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(nbrPersonnel.getText().trim());
        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Nombre de personne doit etre un nombre", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }

    @Override
    public String getName() {
        return name.getText().trim();
    }
    public int getNbPersonnes() {return Integer.parseInt(nbrPersonnel.getText().trim());}
    public void setNbrPersonnel(JTextField nbrPersonnel) {this.nbrPersonnel=nbrPersonnel;}
    public void setName(JTextField name) {
        this.name = name;
    }
    public String getDescription() {
        return description.getText().trim();
    }

    public void setDescription(JTextArea description) {
        this.description = description;
    }

    public String getOrganisation(){
        return this.organisation.getSelectedItem().toString();
    }

    public String getResponsable(){
        return this.responsable.getSelectedItem().toString();
    }

    //public void setResponsable(JTextArea responsable) {this.responsable}

    public SystemeManagement getsystemeManagement() {return systemeManagement;}
}
