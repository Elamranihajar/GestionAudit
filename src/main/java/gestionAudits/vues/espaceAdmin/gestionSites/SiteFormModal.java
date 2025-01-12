package gestionAudits.vues.espaceAdmin.gestionSites;


import gestionAudits.controller.GestionOrganisationController;
import gestionAudits.controller.GestionSitesController;
import gestionAudits.models.Organisation;
import gestionAudits.models.Site;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SiteFormModal extends JDialog {
  
    private JTextField name;
    private JTextArea address;
    private JTextArea description;
    private JComboBox<String> organisation;
    private boolean isSubmitted = false;
    private String typeOperation;
    private int idSite;
    private List<Organisation> organisationList;
    private Site site;

    public SiteFormModal(Frame parent, Site site,String typeOperation) {
        super(parent, STR."\{typeOperation} Site", true);
        this.typeOperation = typeOperation;
        idSite=site!=null ? site.getId() : 0;
        organisationList= GestionOrganisationController.listerOrganisation();
        createForm(parent,typeOperation,site);

    }

    private void createForm(Frame parent,String typeOperation,Site site) {
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
        name = new JTextField();
        name.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(new JScrollPane(name), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("address:"), gbc);

        gbc.gridx = 1;
        address = new JTextArea(3, 20);
        address.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(new JScrollPane(address), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("description:"), gbc);

        gbc.gridx = 1;
        description = new JTextArea(3, 20);
        description.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(new JScrollPane(description), gbc);



        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Type:"), gbc);
        List<String> listOrganisationNames=organisationList.stream()
                                                            .map(Organisation::getNom).toList();
        gbc.gridx = 1;
        organisation = new JComboBox<>(listOrganisationNames.toArray(new String[0]));
        formPanel.add(organisation, gbc);

        if (typeOperation.equals("Modifier")){
            name.setText(site.getName());
            address.setText(site.getAddress());
            description.setText(site.getDescription());
            organisation.setSelectedItem(site.getOrganization().getNom());
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

    public Site getSite() {
        return site;
    }

    private void onSubmit() {
        Organisation organisationSelected=organisationList.stream()
                                                          .filter(org->org.getNom().equals(getOrganisation()))
                                                          .findFirst().orElse(null);
        if (validateFields()) {
            site=new Site(idSite,getName(),getAddress(),getDescription(),organisationSelected);
            System.out.println(site);

            if(typeOperation.equals("Ajouter"))
                GestionSitesController.ajouterSite(site);
            else
                GestionSitesController.modiferSite(site);


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
            JOptionPane.showMessageDialog(this, "le nom de site est requise.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (address.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "L'adresse est requise.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (description.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La description est requise.", "Erreur", JOptionPane.ERROR_MESSAGE);
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

    public String getAddress() {
        return address.getText().trim();
    }

    public void setAddress(JTextArea address) {
        this.address = address;
    }

    public String getDescription() {
        return description.getText().trim();
    }

    public void setDescription(JTextArea description) {
        this.description = description;
    }

    public String getOrganisation() {
        return organisation.getSelectedItem().toString();
    }

    public void setOrganisation(JComboBox<String> organisation) {
        this.organisation = organisation;
    }
}
