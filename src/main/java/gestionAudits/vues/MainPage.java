package gestionAudits.vues;

import gestionAudits.models.Session;
import gestionAudits.vues.espaceAdmin.Dashboard;
import gestionAudits.vues.espaceAdmin.gestionAudits.GestionAudit;
import gestionAudits.vues.espaceAdmin.gestionClausesStandards.GestionStandardsClauses;
import gestionAudits.vues.espaceAdmin.gestionOrganisation.GestionOrganisation;
import gestionAudits.vues.espaceAdmin.gestionProcessus.GestionProcessus;
import gestionAudits.vues.espaceAdmin.gestionSites.GestionSite;
import gestionAudits.vues.espaceAdmin.gestionUsers.GestionUser;
import gestionAudits.vues.espaceAdmin.gestion_systemes_management.GestionSystemManagement;
import gestionAudits.vues.espaceAuditeur.ListAudit;
import gestionAudits.vues.espaceAuditeur.Profile;

import javax.swing.*;
import java.awt.*;

public class MainPage extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public MainPage() {

        String espace=Session.userConnected.getRoleId()==1?"Admin":"Auditeur";
        setTitle("Gestion des Audits - Espace "+espace);
        // Définir la taille et la position pour occuper tout l'écran disponible
        GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle screenBounds = graphics.getMaximumWindowBounds();
        setSize(screenBounds.width, screenBounds.height);
        setLocation(screenBounds.x, screenBounds.y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel principal avec CardLayout
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Ajout des différentes vues (content)
        if(Session.userConnected.getRoleId()==1)
            useCaseAdmin();
        else
            useCaseAuditeur();

        // Créer un conteneur pour ajouter uniquement des marges autour de `contentPanel`
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false); // Assure que le panneau ne masque pas l'arrière-plan
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Marges autour de contentPanel
        contentWrapper.add(contentPanel, BorderLayout.CENTER);

        // Ajouter des marges pour la barre latérale
        JPanel sidebarWrapper = new JPanel(new BorderLayout());
        sidebarWrapper.setOpaque(false); // Assure que le panneau ne masque pas l'arrière-plan
        sidebarWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20)); // Marge à droite de la barre latérale
        sidebarWrapper.add(new SideBarPanel(this), BorderLayout.CENTER);

        // Ajout des composants au JFrame
        add(sidebarWrapper, BorderLayout.WEST);
        add(contentWrapper, BorderLayout.CENTER);
    }

    private void useCaseAuditeur() {
        contentPanel.add(new ListAudit(),"LancerAudit");
        contentPanel.add(new Profile(),"profile");
    }

    private void useCaseAdmin(){
        contentPanel.add(new Dashboard(),"Dashboard");
        contentPanel.add(new GestionOrganisation(), "GestionOrganisation");
        contentPanel.add(new GestionStandardsClauses(), "GestionStandardsClauses");
        contentPanel.add(new GestionSite(), "GestionSites");
        contentPanel.add(new GestionUser(), "GestionUsers");
        contentPanel.add(new GestionSystemManagement(), "GestionSystemManagement");
        contentPanel.add(new GestionProcessus(), "GestionProcessus");
        contentPanel.add(new GestionAudit(),"GestionAudit");
    }

    public void switchToView(String viewName) {
        cardLayout.show(contentPanel, viewName);
    }
}
