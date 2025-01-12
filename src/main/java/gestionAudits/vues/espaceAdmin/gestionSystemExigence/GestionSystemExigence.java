package gestionAudits.vues.espaceAdmin.gestionSystemExigence;

import gestionAudits.controller.GestionPreuvsController;
import gestionAudits.models.*;
import gestionAudits.controller.GestionSystemExigenceController;
import gestionAudits.vues.espaceAdmin.gestionActions.ListerAction;
import gestionAudits.vues.espaceAuditeur.AddPreuveDialog;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.List;

public class GestionSystemExigence extends JDialog {

    private JComboBox<SystemeExigence> comboBox;  // ComboBox pour les systèmes d'exigence
    private JPanel detailPanel;  // Panneau des détails du système d'exigence
    private List<SystemeExigence> systemeExigences;  // Liste des systèmes d'exigence
    private List<Preuve> preuveList;
    public GestionSystemExigence(Frame parent,List<SystemeExigence> systemeExigences) {
        super(parent, "Détail d'audit", true);
        this.systemeExigences =systemeExigences;
        createUIComponents(parent);
    }

    private void createUIComponents(Frame parent) {
        setLayout(new BorderLayout(10, 10));
        setSize(600, 600);
        setLocationRelativeTo(parent);

        // ComboBox pour la sélection des systèmes d'exigence
        comboBox = new JComboBox<>(systemeExigences.toArray(new SystemeExigence[0]));
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setPreferredSize(new Dimension(300, 30));
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Afficher les détails du système d'exigence sélectionné
                SystemeExigence selectedSysteme = (SystemeExigence) comboBox.getSelectedItem();
                if (selectedSysteme != null) {
                    displayDetails(selectedSysteme);
                }
            }
        });


        // Ajouter le ComboBox dans le panneau supérieur
        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.add(new JLabel("Sélectionner un système d'exigence:"));
        comboBoxPanel.add(comboBox);
        add(comboBoxPanel, BorderLayout.NORTH);

        // Panneau des détails
        detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Diviser l'interface en deux parties
        JScrollPane detailScrollPane = new JScrollPane(detailPanel);
        add(detailScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton modifyButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        JButton gererAction=new JButton("Gerer les actions");

        modifyButton.setBackground(new Color(46, 204, 113)); // Vert
        modifyButton.setForeground(Color.WHITE); // Texte en blanc
        modifyButton.setFocusPainted(false); // Supprime le contour bleu par défaut
        modifyButton.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96))); // Bordure vert foncé
        modifyButton.addActionListener(e->modiferSystem());

// Couleur pour "Supprimer" (rouge)
        deleteButton.setBackground(new Color(231, 76, 60)); // Rouge
        deleteButton.setForeground(Color.WHITE); // Texte en blanc
        deleteButton.setFocusPainted(false); // Supprime le contour bleu par défaut
        deleteButton.setBorder(BorderFactory.createLineBorder(new Color(192, 57, 43))); // Bord
        deleteButton.addActionListener(e->supprimerSystem());

        gererAction.setBackground(new Color(13, 156, 228)); // Rouge
        gererAction.setForeground(Color.WHITE); // Texte en blanc
        gererAction.setFocusPainted(false); // Supprime le contour bleu par défaut
        gererAction.setBorder(BorderFactory.createLineBorder(new Color(13, 156, 228))); // Bord
        gererAction.addActionListener(e->gererAction());

        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(gererAction);

// Ajouter le panneau des boutons sous le comboBoxPanel
        add(buttonPanel, BorderLayout.AFTER_LAST_LINE);
        displayDetails(systemeExigences.getFirst());
    }

    private void gererAction() {
        SystemeExigence selectedSysteme = (SystemeExigence) comboBox.getSelectedItem();
        ListerAction dialog=new ListerAction((Frame) SwingUtilities.getWindowAncestor(this),selectedSysteme);
        dialog.setVisible(true);
    }

    private void modiferSystem(){
        SystemeExigence selectedSysteme = (SystemeExigence) comboBox.getSelectedItem();
        Audit audit=selectedSysteme.getAudit();
        System.out.println("id autre : "+selectedSysteme.getAutreExigence().getId());
        SystemExigentForm dialog = new SystemExigentForm((Frame) SwingUtilities.getWindowAncestor(this), audit,selectedSysteme);
        dialog.setVisible(true);
    }
    private void supprimerSystem(){
        SystemeExigence selectedSysteme = (SystemeExigence) comboBox.getSelectedItem();
        if(GestionSystemExigenceController.supprimer(selectedSysteme))
            JOptionPane.showMessageDialog(this,"Supprimer avec succes","Information",JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel listPreuvs(List<Preuve> preuveList) {
        // Panel principal contenant tout le contenu
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),"list des preuves", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14), Color.BLACK));
        // Panel supérieur pour le bouton "Ajouter"
//        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        JButton addButton = new JButton("Ajouter");
//        addButton.setFont(new Font("Arial", Font.BOLD, 14));
//        addButton.setBackground(new Color(76, 175, 80)); // Vert pour un bouton "Ajouter"
//        addButton.setForeground(Color.WHITE);
//        addButton.setFocusPainted(false);
//        addButton.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(new Color(60, 140, 70)),
//                BorderFactory.createEmptyBorder(5, 15, 5, 15)
//        ));
//        addButton.addActionListener(e->{
//            AddPreuveDialog dialog=new AddPreuveDialog((Frame) SwingUtilities.getWindowAncestor(this));
//            dialog.setVisible(true);
//            if (dialog.isSubmit()) {
//                Preuve preuve=dialog.getPreuve();
//                preuve.setSystemeExigence(this.systemeExigence);
//                if(GestionPreuvsController.addPreuve(preuve)){
//                    this.newList.add(preuve);
//                    this.preuveList.add(preuve);
//                    displayDetails(this.systemeExigence, this.preuveList);
//                }else
//                    JOptionPane.showMessageDialog(this,"Erreur lors d'ajoute cet preuve","Erreur",JOptionPane.ERROR_MESSAGE);
//            }
//        });
//        headerPanel.add(addButton);
        if (preuveList == null || preuveList.isEmpty()) {
            // Panel pour afficher le message "Aucune preuve disponible"
            JPanel emptyPanel = new JPanel(new BorderLayout());
            JLabel noDataLabel = new JLabel("Aucune preuve disponible", JLabel.CENTER);
            noDataLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            noDataLabel.setForeground(Color.GRAY);

            // Ajouter le message au panneau principal
            emptyPanel.add(noDataLabel, BorderLayout.CENTER);
//            emptyPanel.add(addButton, BorderLayout.SOUTH); // Bouton Ajouter en dessous

            mainPanel.add(emptyPanel, BorderLayout.CENTER);
        }else {
            // Scrollable panel pour la liste des preuves
            JPanel preuveLinksPanel = new JPanel();
            preuveLinksPanel.setLayout(new GridBagLayout());
            preuveLinksPanel.setBackground(Color.WHITE);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 10, 5, 10);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;

            int row = 0;
            for (Preuve preuve : preuveList) {
                gbc.gridx = 0;
                gbc.gridy = row;
                JLabel preuveLabel = new JLabel(preuve.getName() + " : ");
                preuveLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                preuveLabel.setForeground(Color.BLACK);
                preuveLinksPanel.add(preuveLabel, gbc);

                gbc.gridx = 1;
                JButton preuveButton = new JButton("Voir");
                preuveButton.setFont(new Font("Arial", Font.PLAIN, 12));
                preuveButton.setForeground(Color.BLUE);
                preuveButton.setBorderPainted(false);
                preuveButton.setContentAreaFilled(false);
                preuveButton.setFocusPainted(false);

                // Action pour ouvrir l'URL de la preuve
                preuveButton.addActionListener(e -> {
                    try {
                        Desktop.getDesktop().browse(new URI(preuve.getUrl())); // Ouvre l'URL dans le navigateur par défaut
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(mainPanel, "Erreur lors de l'ouverture de la preuve.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                });

                preuveLinksPanel.add(preuveButton, gbc);

//                gbc.gridx = 2;
//                JButton supprimerButton = new JButton("Suprimer");
//                supprimerButton.setFont(new Font("Arial", Font.PLAIN, 12));
//                supprimerButton.setForeground(Color.RED);
//                supprimerButton.setBorderPainted(false);
//                supprimerButton.setContentAreaFilled(false);
//                supprimerButton.setFocusPainted(false);
//
//                supprimerButton.addActionListener(e->{
//                    if(GestionPreuvsController.supprimerPreuve(preuve)) {
//                        this.preuveList.remove(preuve);
//                        displayDetails(systemeExigence, this.preuveList);
//                    }else
//                        JOptionPane.showMessageDialog(this,"Erreur lors de suppression","Error",JOptionPane.ERROR_MESSAGE);
//                });
//
//                preuveLinksPanel.add(supprimerButton, gbc);

                row++;
            }

            JScrollPane scrollPane = new JScrollPane(preuveLinksPanel);
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            scrollPane.getVerticalScrollBar().setUnitIncrement(10);

            // Ajout des panels au panel principal
            //  mainPanel.add(headerPanel, BorderLayout.NORTH);
            mainPanel.add(scrollPane, BorderLayout.CENTER);
        }

        return mainPanel;
    }


    private void displayDetails(SystemeExigence se) {
        detailPanel.removeAll();

        preuveList= GestionPreuvsController.getPreuves(se.getId());
        // Bloc 1: Informations Générales
        JPanel generalInfoPanel = createBlock("Informations Générales", new String[][]{
                {"ID", String.valueOf(se.getId())},
                {"Statut", se.getStatus()},
                {"Exclusion", se.isExclu() ? "Oui" : "Non"},
                {"Motif d'Exclusion", se.getMotifExclusion()}
        }, se);
        detailPanel.add(generalInfoPanel);

        detailPanel.add(listPreuvs(preuveList));

        // Bloc 2: Informations de l'Auditeur
        User auditeur = se.getAuditeur();
        JPanel auditeurPanel = createBlock("Auditeur", new String[][]{
                {"Nom", auditeur.getName()},
                {"Email", auditeur.getEmail()},
                {"Domaine", auditeur.getDomain()}
        }, se);
        detailPanel.add(auditeurPanel);

        // Bloc 3: Autre Exigence
        AutreExigence autreExigence = se.getAutreExigence();
        JPanel autreExigencePanel = createBlock("Autre Exigence", new String[][]{
                {"Nom", autreExigence.getName()},
                {"Description", autreExigence.getDescription()},
                {"Type", autreExigence.getType()}
        },se);
        detailPanel.add(autreExigencePanel);

        // Bloc 4: Clause Standard
        ClauseStandard clauseStandard = se.getClauseStandard();
        JPanel clausePanel = createBlock("Clause Standard", new String[][]{
                {"Référence Clause", clauseStandard.getClause().getReference()},
                {"Description Clause", clauseStandard.getClause().getDescription()},
                {"Référence Standard", clauseStandard.getStandard().getReference()},
                {"Description Standard", clauseStandard.getStandard().getDescription()}
        }, se);
        detailPanel.add(clausePanel);

        // Bloc 5: Système de Management
        SystemeManagement systemeManagement = se.getSystemeManagement();
        JPanel systemeManagementPanel = createBlock("Système de Management", new String[][]{
                {"Nom", systemeManagement.getNom()},
                {"Description", systemeManagement.getDescription()},
                {"Nombre de Personnes", String.valueOf(systemeManagement.getNbPersonnes())},
                {"Organisation", systemeManagement.getOrganisation().getNom()},
                {"Responsable", systemeManagement.getResponsable().getName()}
        }, se);
        detailPanel.add(systemeManagementPanel);

        // Revalidate et repaint pour actualiser l'interface
        detailPanel.revalidate();
        detailPanel.repaint();
    }

    /**
     * Crée un bloc avec un titre et des informations sous forme de tableau.
     */
    private JPanel createBlock(String title, String[][] data, SystemeExigence se) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(data.length, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), title, TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14), Color.BLACK));
        if(title.equals("Autre Exigence") && se.getAutreExigence().getName()==null ) {
            System.out.println("Autre Exigence");
            panel.add(new JLabel("Aucun autre exigence"));
            return panel;
        }

        for (String[] row : data) {
            panel.add(new JLabel(row[0] + ":"));
            panel.add(new JLabel(row[1]));
        }

        return panel;
    }
}
