package gestionAudits.vues;

import gestionAudits.controller.authentification.AuthentificationControllerWithLogin;
import gestionAudits.models.Session;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SideBarPanel extends JPanel {
    private final MainPage mainFrame;

    public SideBarPanel(MainPage mainFrame) {
        this.mainFrame = mainFrame;

        // Configuration principale
        setPreferredSize(new Dimension(300, 800));
        setBackground(new Color(7, 128, 195)); // Couleur améliorée pour un bleu plus doux
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Organisation verticale

        // Titre
        String espace=Session.userConnected.getRoleId()==1?"Admin":"Auditeur";
        JLabel titleLabel = new JLabel("Espace "+ espace, SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(titleLabel);

        // Bloc utilisateur
        add(createUserPanel());

        // Espacement entre le bloc utilisateur et les boutons
        add(Box.createVerticalStrut(20));

        // Conteneur pour les boutons
        add(createButtonPanel());

        // Espacement dynamique pour pousser le bouton de déconnexion vers le bas
        add(Box.createVerticalGlue());

        // Bouton Déconnexion
        JButton logoutButton =createLogoutButton();
        logoutButton.addActionListener(e -> {
            if(AuthentificationControllerWithLogin.deconnexion()) {
                System.exit(0);
            }
        });
        add(logoutButton);
    }

    /**
     * Crée le panneau utilisateur avec la photo et le nom.
     */
    private JPanel createUserPanel() {
        JPanel userPanel = new JPanel();
        userPanel.setBackground(new Color(10, 120, 180)); // Même couleur de fond
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS)); // Alignement vertical
        userPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        )); // Marges internes avec bordure blanche

        // Photo de l'utilisateur
        try {
            ImageIcon userImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icones/loginIcone.png")));
            Image scaledImage = userImage.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); // Redimensionner
            JLabel userPhoto = new JLabel(new ImageIcon(scaledImage));
            userPhoto.setAlignmentX(CENTER_ALIGNMENT); // Centré horizontalement
            userPanel.add(userPhoto);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Photo non disponible");
            errorLabel.setForeground(Color.WHITE);
            errorLabel.setAlignmentX(CENTER_ALIGNMENT);
            userPanel.add(errorLabel);
        }

        // Espacement
        userPanel.add(Box.createVerticalStrut(10));

        // Nom de l'utilisateur
        JLabel userName = new JLabel(Session.userConnected.getName());
        userName.setForeground(Color.WHITE);
        userName.setFont(new Font("Arial", Font.PLAIN, 16));
        userName.setAlignmentX(CENTER_ALIGNMENT);
        userPanel.add(userName);

        return userPanel;
    }

    /**
     * Crée le panneau contenant les boutons de navigation.
     */
    private JPanel createButtonPanel() {
       if (Session.userConnected.getRoleId() == 1)
          return  useCaseAdmin();
        else
          return useCaseAuditeur();
    }

    private JPanel useCaseAuditeur() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(10, 120, 180));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Alignement vertical

        buttonPanel.add(createButton("Mes Audits", "/icones/audit.png", "LancerAudit"));
        buttonPanel.add(Box.createVerticalStrut(10)); // Espacement

        buttonPanel.add(createButton("Profile", "/icones/audit.png", "profile"));
        buttonPanel.add(Box.createVerticalStrut(10)); // Espacement

        return buttonPanel;
    }

    private JPanel useCaseAdmin(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(10, 120, 180));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Alignement vertical

        buttonPanel.add(createButton("Dashboard", "/icones/dashboard", "Dashboard"));
        buttonPanel.add(Box.createVerticalStrut(10)); // Espacement

        buttonPanel.add(createButton("Gérer des Organisations", "/icones/audit.png", "GestionOrganisation"));
        buttonPanel.add(Box.createVerticalStrut(10)); // Espacement

        buttonPanel.add(createButton("Gérer des Sites", "/icones/nonconformity.png", "GestionSites"));
        buttonPanel.add(Box.createVerticalStrut(10)); // Espacement

        buttonPanel.add(createButton("Définir les Processus", "/icones/nonconformity.png", "GestionProcessus"));
        buttonPanel.add(Box.createVerticalStrut(10)); // Espacement

        buttonPanel.add(createButton("Gérer les Standards et Clauses ", "/icones/dashboa.png", "GestionStandardsClauses"));
        buttonPanel.add(Box.createVerticalStrut(10)); // Espacement

        buttonPanel.add(createButton("Gestion des System de Management", "/icones/nonconformity.png", "GestionSystemManagement"));
        buttonPanel.add(Box.createVerticalStrut(10)); // Espacement

        buttonPanel.add(createButton("Planification et suivre les audits", "/icones/nonconformity.png", "GestionAudit"));
        buttonPanel.add(Box.createVerticalStrut(10)); // Espacement

        buttonPanel.add(createButton("Gestion des Users", "/icones/nonconformity.png", "GestionUsers"));
        buttonPanel.add(Box.createVerticalStrut(10)); // Espacement

        return buttonPanel;
    }



    private JButton createButton(String text, String iconPath, String actionCommand) {
        JButton button = new JButton(text);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.setBackground(new Color(10, 120, 180)); // Fond bleu
        button.setForeground(Color.WHITE); // Texte blanc
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(10, 120, 180), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Ajouter une icône si disponible
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(iconPath)));
            Image scaledIcon = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledIcon));
            button.setIconTextGap(10); // Espacement entre texte et icône
        } catch (Exception e) {
            System.err.println("Icône introuvable pour le bouton : " + text);
        }

        button.setHorizontalAlignment(SwingConstants.LEFT); // Texte aligné à gauche
        button.setActionCommand(actionCommand);
        button.addActionListener(e -> mainFrame.switchToView(actionCommand));

        // Effet de survol
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 153, 204));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(10, 120, 180));
            }
        });

        return button;
    }

    private JButton createLogoutButton() {
        JButton logoutButton = createButton("Déconnexion", "/icones/logout.png", "Logout");
        logoutButton.setAlignmentX(CENTER_ALIGNMENT);
        return logoutButton;
    }
}
