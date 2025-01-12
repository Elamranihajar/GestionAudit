package gestionAudits.vues.authentification;

import gestionAudits.controller.SerializationToJson;
import gestionAudits.controller.authentification.AuthentificationControllerWithActiveDirectory;
import gestionAudits.controller.authentification.AuthentificationControllerWithGoogle;
import gestionAudits.controller.authentification.AuthentificationControllerWithLogin;
import gestionAudits.models.Session;
import gestionAudits.models.User;
import gestionAudits.vues.MainPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Auth {
    private JFrame frame;
    public Auth() {
        createAndShowGUI();
    }
    private void createAndShowGUI() {
        frame = new JFrame("Gestion des Audits - Authentification");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        //les composants principaux
        frame.add(headerCompenant(), BorderLayout.NORTH);
        frame.add(loginCompenant(), BorderLayout.CENTER);
        frame.add(footerCompenant(), BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null); // Centrer la fenêtre
        frame.setVisible(true);
    }
    private JPanel headerCompenant() {
        JPanel headerCompenantPanel = new JPanel();
        headerCompenantPanel.setBackground(new Color(13, 177, 227));
        headerCompenantPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel headerCompenantLabel = new JLabel("Bienvenue dans l'application de gestion des audits");
        headerCompenantLabel.setForeground(Color.WHITE);
        headerCompenantLabel.setFont(new Font("Arial", Font.BOLD, 16));

        headerCompenantPanel.add(headerCompenantLabel);
        return headerCompenantPanel;
    }

    // Panel principal pour la connexion
    private JPanel loginCompenant() {
        JPanel loginCompenantPanel = new JPanel();
        loginCompenantPanel.setLayout(new GridBagLayout());
        loginCompenantPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Icône de connexion
        JLabel iconLabel = new JLabel(insertIcone("src/main/resources/icones/loginIcone.png",64,64));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginCompenantPanel.add(iconLabel, gbc);

        // Champ de texte pour le loginCompenant
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginCompenantPanel.add(new JLabel("Nom d'utilisateur :"), gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(20);
        loginCompenantPanel.add(usernameField, gbc);

        // Champ de mot de passe
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginCompenantPanel.add(new JLabel("Mot de passe :"), gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        loginCompenantPanel.add(passwordField, gbc);


        JLabel eyeLabel = new JLabel(insertIcone("src/main/resources/icones/eye-closed.png", 24, 24));
        eyeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 2;
        gbc.gridy = 2;
        loginCompenantPanel.add(eyeLabel, gbc);

        // Ajouter un écouteur sur l'icône pour gérer l'affichage/masquage du mot de passe
        eyeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (passwordField.getEchoChar() == '\u0000') {  // Le mot de passe est visible
                    passwordField.setEchoChar('*');  // Masquer le mot de passe
                    eyeLabel.setIcon(insertIcone("src/main/resources/icones/eye-closed.png", 24, 24)); // Changer l'icône
                } else {
                    passwordField.setEchoChar('\u0000');  // Afficher le mot de passe
                    eyeLabel.setIcon(insertIcone("src/main/resources/icones/eye-open.png", 24, 24)); // Changer l'icône
                }
            }
        });




        // Bouton de connexion
        JButton loginCompenantButton = new JButton("Se connecter");
        loginCompenantButton.setBackground(new Color(51, 153, 102)); // Vert
        loginCompenantButton.setForeground(Color.WHITE);
        loginCompenantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleloginCompenant(usernameField.getText(), new String(passwordField.getPassword()));
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginCompenantPanel.add(loginCompenantButton, gbc);

        // Boutons supplémentaires
        gbc.gridy = 4;
        JPanel otherOptionsPanel = createOtherloginCompenantOptionsPanel(usernameField.getText(), new String(passwordField.getPassword()));
        loginCompenantPanel.add(otherOptionsPanel, gbc);

        return loginCompenantPanel;
    }

    // Panel pour les autres options de connexion
    private JPanel createOtherloginCompenantOptionsPanel(String username, String motpass) {
        JPanel otherOptionsPanel = new JPanel();
        otherOptionsPanel.setLayout(new FlowLayout());

        JButton googleButton = new JButton("Connexion Google",insertIcone("src/main/resources/icones/loginGoogle.png",16,16));
        googleButton.setBackground(Color.WHITE);
        googleButton.addActionListener(e -> handleGoogleloginCompenant());
        otherOptionsPanel.add(googleButton);

        JButton adButton = new JButton("Connexion Active Directory",insertIcone("src/main/resources/icones/loginActiveDirctory.png",16,16));
        adButton.setBackground(Color.WHITE);
        adButton.addActionListener(e -> handleActiveDirectoryloginCompenant(username,motpass));
        otherOptionsPanel.add(adButton);




        return otherOptionsPanel;
    }

    // Panel de pied de page
    private JPanel footerCompenant() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 240, 240)); // Gris clair
        footerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JLabel footerLabel = new JLabel("© 2024 Gestion des Audits. Tous droits réservés.");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        footerPanel.add(footerLabel);

        return footerPanel;
    }

    // Fonction pour gérer la connexion classique
    private void handleloginCompenant(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } else {
            AuthentificationControllerWithLogin login=new AuthentificationControllerWithLogin(username, password);
            if (login.isAuthentificationOk()){
                creatSession(AuthentificationControllerWithLogin.authUser);
            }
            else
                JOptionPane.showMessageDialog(frame, "Nom d'utilisateur ou mot de passe incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void creatSession(User userInfo){
        Session session=new Session(userInfo);
//        Serialization<Session> serialization=new Serialization<>();
//        serialization.serializeObject("session.ser",session);
        SerializationToJson<Session> serializationToJson=new SerializationToJson<>();

        serializationToJson.serialize(session,"session.json");


        Session.userConnected=session.getUser();
        frame.dispose();
        SwingUtilities.invokeLater(() -> {
            new MainPage().setVisible(true);
        });
    }

    private void handleGoogleloginCompenant(){
//        JOptionPane.showMessageDialog(frame, "Connexion via Google non encore implémentée.", "Info", JOptionPane.INFORMATION_MESSAGE);
        try {
            AuthentificationControllerWithGoogle login=new AuthentificationControllerWithGoogle();
            creatSession(login.getInfoUser());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erreur de connexion", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Fonction pour gérer la connexion via Active Directory
    private void handleActiveDirectoryloginCompenant(String username, String motpass) {
       User userInfo= AuthentificationControllerWithActiveDirectory.authenticate(username, motpass);
    }

    private ImageIcon insertIcone(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }
}
