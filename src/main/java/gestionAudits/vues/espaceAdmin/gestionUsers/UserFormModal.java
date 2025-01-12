package gestionAudits.vues.espaceAdmin.gestionUsers;


import gestionAudits.controller.GestionOrganisationController;
import gestionAudits.controller.GestionSitesController;
import gestionAudits.controller.GestionUsersController;
import gestionAudits.models.Organisation;
import gestionAudits.models.Site;
import gestionAudits.models.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class UserFormModal extends JDialog {
  
    private JTextField name;
    private JTextField email;
    // private JPasswordField motpass;
    private JComboBox<String> roles;
    private boolean isSubmitted = false;
    private String typeOperation;
    private int idUser;
  //  private List<Organisation> organisationList;
    private User user;

    public UserFormModal(Frame parent, User user,String typeOperation) {
        super(parent, STR."\{typeOperation} user", true);
        this.typeOperation = typeOperation;
        idUser=user!=null ? user.getId() : 0;
        //organisationList= GestionOrganisationController.listerOrganisation();
        createForm(parent,typeOperation,user);

    }

    private void createForm(Frame parent,String typeOperation,User user) {
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
        formPanel.add(new JLabel("email:"), gbc);

        gbc.gridx = 1;
        email = new JTextField(20);
        email.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(email, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("role:"), gbc);

        gbc.gridx = 1;
        roles = new JComboBox<>(new String[]{"admin","auditeur"});
        formPanel.add(roles, gbc);

        if (typeOperation.equals("Modifier")){
            name.setText(user.getName());
            email.setText(user.getEmail());
            roles.setSelectedItem(user.getRoleId()==1 ? "admin" : "auditeur");
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

    public User getSite() {
        return user;
    }

    private void onSubmit() {

        if (validateFields()) {
            user=new User();
            user.setId(idUser);
            user.setName(getName());
            user.setEmail(getEmail());
            user.setRoleId(getRole().equals("admin")?1:2);
            System.out.println(user);

            if(typeOperation.equals("Ajouter"))
                GestionUsersController.ajouterUser(user);
            else
                GestionUsersController.modifierUser(user);

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
        if (email.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "L'adresse e-mail est requise.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isValidEmail(email.getText().trim())) {
            JOptionPane.showMessageDialog(this, "L'adresse e-mail est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-.]+@[\\w-]+(\\.[a-zA-Z]+)+$";
        return Pattern.matches(emailRegex, email);
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

    public String getEmail() {
        return email.getText().trim();
    }

    public void setEmail(JTextField email) {
        this.email = email;
    }

    public String getRole(){return this.roles.getSelectedItem().toString();}

    public User getUser() {return user;}
}
