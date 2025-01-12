package gestionAudits.vues.espaceAdmin.gestionProcessus;


import gestionAudits.controller.GestionOrganisationController;
import gestionAudits.controller.GestionSystemManagementController;
import gestionAudits.controller.GestionUsersController;
import gestionAudits.controller.GestionProcessusController;
import gestionAudits.models.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProcessusFormModal extends JDialog {
  
    private JTextField name;
    private JTextArea description;
    private JComboBox<String> organisation;
    private JComboBox<String> responsable;
    private boolean isSubmitted = false;
    private String typeOperation;
    private int id;
    private List<Organisation> organisationList;
    private List<User> listAudits;
    private Processus processus;

    public ProcessusFormModal(Frame parent, Processus processus, String typeOperation) {
        super(parent, STR."\{typeOperation} user", true);
        organisationList=GestionOrganisationController.listerOrganisation();
        listAudits=GestionUsersController.listerAudits();
        this.typeOperation = typeOperation;
        this.processus = processus;
        id=processus!=null ? processus.getId() : 0;
        System.out.println("form : " +this.processus);
        System.out.println(id);
        createForm(parent,typeOperation,processus);

    }

    private void createForm(Frame parent,String typeOperation,Processus processus) {
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
            name.setText(processus.getName());
            description.setText(processus.getDescription());
            organisation.setSelectedItem(processus.getOrganization().getNom());
            responsable.setSelectedItem(processus.getResponsable().getName());
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

        if (validateFields()) {
            if(processus==null)
                processus=new Processus();
            processus.setId(id);
            processus.setName(getName());
            processus.setDescription(getDescription());
            Organisation org=organisationList.stream()
                                             .filter(organisation1->organisation1.getNom().equals(getOrganisation()))
                                             .findFirst().orElse(null);
            processus.setOrganization(org);
            User responsable=listAudits.stream()
                                        .filter(user->user.getName().equals(getResponsable()))
                                        .findFirst().orElse(null);
            processus.setResponsable(responsable);
            if(typeOperation.equals("Ajouter"))
                GestionProcessusController.ajouterProcessus(processus);
            else
                GestionProcessusController.modifierProcessus(processus);

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
        return true;
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }

    @Override
    public String getName() {
        return name.getText().trim();
    }

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

    public Processus getprocessus() {return processus;}
}
