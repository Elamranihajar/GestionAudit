package gestionAudits.vues.espaceAdmin.gestionSystemExigence;

import gestionAudits.models.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class SystemeExigenceDetails extends JDialog {
    public SystemeExigenceDetails(Frame parent, SystemeExigence systemeExigence) {
        super(parent, "Détails du Système d'Exigence", true);

        setLayout(new BorderLayout(10, 10));
        setSize(600, 700);
        setLocationRelativeTo(parent);

        // Panneau principal avec disposition verticale
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Bloc 1: Informations Générales
        JPanel generalInfoPanel = createBlock("Informations Générales", new String[][]{
                {"ID", String.valueOf(systemeExigence.getId())},
                {"Statut", systemeExigence.getStatus()},
                {"Exclusion", systemeExigence.isExclu() ? "Oui" : "Non"},
                {"Motif d'Exclusion", systemeExigence.getMotifExclusion()}
        });
        mainPanel.add(generalInfoPanel);

        // Bloc 2: Informations de l'Auditeur
        User auditeur = systemeExigence.getAuditeur();
        JPanel auditeurPanel = createBlock("Auditeur", new String[][]{
                {"Nom", auditeur.getName()},
                {"Email", auditeur.getEmail()},
                {"Domaine", auditeur.getDomain()}
        });
        mainPanel.add(auditeurPanel);

        // Bloc 3: Autre Exigence
        AutreExigence autreExigence = systemeExigence.getAutreExigence();
        JPanel autreExigencePanel = createBlock("Autre Exigence", new String[][]{
                {"Nom", autreExigence.getName()},
                {"Description", autreExigence.getDescription()},
                {"Type", autreExigence.getType()}
        });
        mainPanel.add(autreExigencePanel);

        // Bloc 4: Clause Standard
        ClauseStandard clauseStandard = systemeExigence.getClauseStandard();
        JPanel clausePanel = createBlock("Clause Standard", new String[][]{
                {"Référence", clauseStandard.getClause().getReference()},
                {"Description", clauseStandard.getClause().getDescription()}
        });
        mainPanel.add(clausePanel);

        // Bloc 5: Système de Management
        SystemeManagement systemeManagement = systemeExigence.getSystemeManagement();
        JPanel systemeManagementPanel = createBlock("Système de Management", new String[][]{
                {"Nom", systemeManagement.getNom()},
                {"Description", systemeManagement.getDescription()},
                {"Nombre de Personnes", String.valueOf(systemeManagement.getNbPersonnes())},
                {"Organisation", systemeManagement.getOrganisation().getNom()},
                {"Responsable", systemeManagement.getResponsable().getName()}
        });
        mainPanel.add(systemeManagementPanel);

        // Bouton Fermer
        JButton closeButton = new JButton("Fermer");        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        // Ajouter tout au cadre
        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Crée un bloc avec un titre et des informations sous forme de tableau.
     */
    private JPanel createBlock(String title, String[][] data) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(data.length, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), title, TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14), Color.BLACK));

        for (String[] row : data) {
            panel.add(new JLabel(row[0] + ":"));
            panel.add(new JLabel(row[1]));
        }

        return panel;
    }
}
