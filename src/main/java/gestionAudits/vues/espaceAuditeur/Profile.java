package gestionAudits.vues.espaceAuditeur;

import gestionAudits.controller.GestionUsersController;
import gestionAudits.controller.SerializationToJson;
import gestionAudits.models.Session;
import gestionAudits.models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Profile extends JPanel {

    private final JLabel usernameTextField;
    private JLabel nameLabel, usernameLabel, emailLabel, roleLabel;
    private JTextField nameTextField;
    private JButton editButton;
    private User userconnected;


    public Profile() {
        // Initialisation des informations de l'utilisateur
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        userconnected=Session.userConnected;

        // Configurer le JPanel avec une disposition GridBagLayout pour plus de flexibilité
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Espacement entre les composants
        setBackground(new Color(245, 245, 245)); // Couleur de fond douce

        // Ajouter un titre pour la section du profil
        JLabel titleLabel = new JLabel("Profil Utilisateur");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(50, 50, 50)); // Couleur du texte
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);
        
        // Ligne 1: Nom
        nameLabel = new JLabel("Nom : ");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nameLabel.setForeground(new Color(70, 70, 70));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(nameLabel, gbc);

        nameTextField = new JTextField(userconnected.getName(),20);
        nameTextField.setFocusable(true);
        nameTextField.setFont(new Font("Arial", Font.PLAIN, 16));
        nameTextField.setBackground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(nameTextField, gbc);

        // Ligne 2: Username
        usernameLabel = new JLabel("Username : ");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameLabel.setForeground(new Color(70, 70, 70));
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(usernameLabel, gbc);

        usernameTextField = new JLabel(userconnected.getUsername());
        usernameTextField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameTextField.setBackground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(usernameTextField, gbc);

        // Ligne 3: Email
        emailLabel = new JLabel("Email : ");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailLabel.setForeground(new Color(70, 70, 70));
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(emailLabel, gbc);

        JLabel emailTextField = new JLabel(userconnected.getEmail());
        emailTextField.setFont(new Font("Arial", Font.PLAIN, 16));
        emailTextField.setBackground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(emailTextField, gbc);

        // Ligne 4: Role
        roleLabel = new JLabel("Role : ");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        roleLabel.setForeground(new Color(70, 70, 70));
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(roleLabel, gbc);

        JLabel roleTextField = new JLabel(userconnected.getRoleId()==1?"Admin":"Auditeur");
        roleTextField.setFont(new Font("Arial", Font.PLAIN, 16));
        roleTextField.setBackground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(roleTextField, gbc);

        // Espacement vertical
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(Box.createVerticalStrut(20), gbc);  // Espacement

        // Boutons Modifier et Sauvegarder
        editButton = new JButton("Modifier");
        editButton.setFont(new Font("Arial", Font.PLAIN, 16));
        editButton.setBackground(new Color(34, 150, 243));  // Couleur bleue pour le bouton
        editButton.setForeground(Color.WHITE);
        editButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));  // Bordure pour plus de visibilité
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = nameTextField.getText().trim().replaceAll("\\s+","_").toLowerCase();

                userconnected.setName(nameTextField.getText().trim());
                userconnected.setUsername(username);
                if(GestionUsersController.modifierUser(userconnected)) {
                    SerializationToJson<Session> serializationToJson = new SerializationToJson();
                    Session session=new Session(userconnected);
                    serializationToJson.serialize(session,"session.json");
                    usernameTextField.setText(username);
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        add(editButton, gbc);

    }

}
