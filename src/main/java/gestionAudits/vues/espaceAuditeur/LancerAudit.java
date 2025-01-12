package gestionAudits.vues.espaceAuditeur;

import gestionAudits.controller.GestionPreuvsController;
import gestionAudits.controller.RensgnierStatusController;
import gestionAudits.controller.SerializationToJson;
import gestionAudits.models.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class LancerAudit extends JDialog {

    private JPanel detailPanel;  // Panneau des détails du système d'exigence
    private JTextArea constatsField, ecartsField, motifExclusionField;
    private JComboBox<String> statutComboBox;
    private SystemeExigence systemeExigence;  // Liste des systèmes d'exigence
    private JButton saveButton;
    private boolean isSubmit;
    private List<Preuve> preuveList;
    private List<Preuve> newList;
    private JCheckBox isExclus;


    public LancerAudit(Frame parent,SystemeExigence systemeExigence) {
        super(parent, "Détail des systèmes d'exigence", true);
        this.systemeExigence = systemeExigence;
        statutComboBox = new JComboBox<>(new String[]{"Conforme", "Non conforme", "Exclu"});
        motifExclusionField = new JTextArea(2, 20);
        motifExclusionField.setEditable(false);
        constatsField = new JTextArea(2, 20);
        ecartsField = new JTextArea(2, 20);
        newList = new ArrayList<>();
        isExclus=new JCheckBox();
        preuveList= GestionPreuvsController.getPreuves(systemeExigence.getId());
        createUIComponents(parent);
    }

    private void createUIComponents(Frame parent) {
        setLayout(new BorderLayout(10, 10));
        setSize(900, 600);
        setLocationRelativeTo(parent);

        // Panneau des détails
        detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        statutComboBox.setSelectedItem(systemeExigence.getStatus());
        motifExclusionField.setText(systemeExigence.getMotifExclusion());
        constatsField.setText(systemeExigence.getConstats());
        ecartsField.setText(systemeExigence.getEcarts());

        displayDetails(systemeExigence,this.preuveList);


        JScrollPane detailScrollPane = new JScrollPane(detailPanel);
        add(detailScrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton enregistrerModifications = new JButton("Enregistrer les modifications");

        enregistrerModifications.setBackground(new Color(46, 204, 113)); // Vert
        enregistrerModifications.setForeground(Color.WHITE); // Texte en blanc
        enregistrerModifications.setFocusPainted(false); // Supprime le contour bleu par défaut
        enregistrerModifications.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96))); // Bordure vert foncé
        enregistrerModifications.addActionListener(e -> {
            boolean isInsert= RensgnierStatusController.ajouterRenseignerStatus(getSystemeExigence("En observation"),this.newList);
                if(isInsert){
                    JOptionPane.showMessageDialog(this,"les modification enregistrer avec succes","Succes",JOptionPane.INFORMATION_MESSAGE);
                }


        });
        buttonPanel.add(enregistrerModifications);
        JButton clotureAudit = new JButton("Cloture Audit");

        clotureAudit.setBackground(new Color(8, 185, 237)); // Vert
        clotureAudit.setForeground(Color.WHITE); // Texte en blanc
        clotureAudit.setFocusPainted(false); // Supprime le contour bleu par défaut
        clotureAudit.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96))); // Bordure vert foncé
        clotureAudit.addActionListener(e->{

            boolean isInsert=RensgnierStatusController.ajouterRenseignerStatus(getSystemeExigence(),this.newList);
            if(isInsert){
                JOptionPane.showMessageDialog(this,"audit est cloturé avec succes","Succes",JOptionPane.INFORMATION_MESSAGE);
            }
        });

        buttonPanel.add(clotureAudit);


        add(buttonPanel, BorderLayout.AFTER_LAST_LINE);

    }


    public SystemeExigence getSystemeExigence(String status) {
        SystemeExigence systemeExigence1=getSystemeExigence();
        systemeExigence1.setStatus(status);
        return systemeExigence1;
    }

    public SystemeExigence getSystemeExigence() {
        systemeExigence.setStatus(statutComboBox.getSelectedItem().toString());
        systemeExigence.setEcarts(ecartsField.getText());
        systemeExigence.setMotifExclusion(motifExclusionField.getText());
        systemeExigence.setConstats(constatsField.getText());
        systemeExigence.setAuditeur(Session.userConnected);
        systemeExigence.setExclu(isExclus.isSelected());
        return systemeExigence;
    }
    public boolean isSubmit() {
        return isSubmit;
    }


    private JPanel listPreuvs(List<Preuve> preuveList) {
        // Panel principal contenant tout le contenu
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),"list des preuves", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14), Color.BLACK));
        // Panel supérieur pour le bouton "Ajouter"
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Ajouter");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(76, 175, 80)); // Vert pour un bouton "Ajouter"
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 140, 70)),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        addButton.addActionListener(e->{
            AddPreuveDialog dialog=new AddPreuveDialog((Frame) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            if (dialog.isSubmit()) {
                Preuve preuve=dialog.getPreuve();
                preuve.setSystemeExigence(this.systemeExigence);
                if(GestionPreuvsController.addPreuve(preuve)){
                    this.newList.add(preuve);
                    this.preuveList.add(preuve);
                    displayDetails(this.systemeExigence, this.preuveList);
                }else
                    JOptionPane.showMessageDialog(this,"Erreur lors d'ajoute cet preuve","Erreur",JOptionPane.ERROR_MESSAGE);
            }
        });
        headerPanel.add(addButton);
        if (preuveList == null || preuveList.isEmpty()) {
            // Panel pour afficher le message "Aucune preuve disponible"
            JPanel emptyPanel = new JPanel(new BorderLayout());
            JLabel noDataLabel = new JLabel("Aucune preuve disponible", JLabel.CENTER);
            noDataLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            noDataLabel.setForeground(Color.GRAY);

            // Ajouter le message au panneau principal
            emptyPanel.add(noDataLabel, BorderLayout.CENTER);
            emptyPanel.add(addButton, BorderLayout.SOUTH); // Bouton Ajouter en dessous

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

                gbc.gridx = 2;
                JButton supprimerButton = new JButton("Suprimer");
                supprimerButton.setFont(new Font("Arial", Font.PLAIN, 12));
                supprimerButton.setForeground(Color.RED);
                supprimerButton.setBorderPainted(false);
                supprimerButton.setContentAreaFilled(false);
                supprimerButton.setFocusPainted(false);

                supprimerButton.addActionListener(e->{
                    if(GestionPreuvsController.supprimerPreuve(preuve)) {
                        this.preuveList.remove(preuve);
                        displayDetails(systemeExigence, this.preuveList);
                    }else
                        JOptionPane.showMessageDialog(this,"Erreur lors de suppression","Error",JOptionPane.ERROR_MESSAGE);
                });

                preuveLinksPanel.add(supprimerButton, gbc);

                row++;
            }

            JScrollPane scrollPane = new JScrollPane(preuveLinksPanel);
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            scrollPane.getVerticalScrollBar().setUnitIncrement(10);

            // Ajout des panels au panel principal
            mainPanel.add(headerPanel, BorderLayout.NORTH);
            mainPanel.add(scrollPane, BorderLayout.CENTER);
        }

        return mainPanel;
    }


    private void displayDetails(SystemeExigence se,List<Preuve> preuveList) {
        se.setConstats(constatsField.getText());
        se.setEcarts(ecartsField.getText());
        se.setMotifExclusion(motifExclusionField.getText());

        detailPanel.removeAll();
        JPanel sePanel = new JPanel();
        sePanel.setLayout(new BoxLayout(sePanel, BoxLayout.Y_AXIS));
        sePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Système d'exigence - ID: " + se.getId(), TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14), Color.BLACK));


        // Informations générales
        sePanel.add(createBlock("Informations Générales", new String[][]{
                {"ID", String.valueOf(se.getId())},
                {"Statut", se.getStatus()},
                {"constat : ",se.getConstats()},
                {"Ecart : ",se.getEcarts()},
                {"Exclusion", se.isExclu() ? "Oui" : "Non"},
                {"Motif d'Exclusion", se.getMotifExclusion()}
        }));

        //list des preuves
        sePanel.add(listPreuvs(preuveList));


        // Autre Exigence
        AutreExigence autreExigence = se.getAutreExigence();
        sePanel.add(createBlock("Autre Exigence", new String[][]{
                {"Nom", autreExigence.getName()},
                {"Description", autreExigence.getDescription()},
                {"Type", autreExigence.getType()}
        }));

        // Clause Standard
        ClauseStandard clauseStandard = se.getClauseStandard();
        sePanel.add(createBlock("Clause Standard", new String[][]{
                {"Référence Clause", clauseStandard.getClause().getReference()},
                {"Description Clause", clauseStandard.getClause().getDescription()},
                {"Référence Standard", clauseStandard.getStandard().getReference()},
                {"Description Standard", clauseStandard.getStandard().getDescription()}
        }));

        // Système de Management
        SystemeManagement systemeManagement = se.getSystemeManagement();
        sePanel.add(createBlock("Système de Management", new String[][]{
                {"Nom", systemeManagement.getNom()},
                {"Description", systemeManagement.getDescription()},
                {"Nombre de Personnes", String.valueOf(systemeManagement.getNbPersonnes())},
                {"Organisation", systemeManagement.getOrganisation().getNom()},
                {"Responsable", systemeManagement.getResponsable().getName()}
        }));

        detailPanel.add(sePanel);
        detailPanel.revalidate();
        detailPanel.repaint();
    }

    private JPanel createBlock(String title, String[][] data) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(data.length, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), title, TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14), Color.BLACK));
        if(title.equals("Informations Générales")){
            //id
            panel.add(new JLabel(data[0][0]));
            panel.add(new JLabel(data[0][1]));
            //status
            panel.add(new JLabel(data[1][0]));
            statutComboBox.setSelectedItem(data[1][1]);
            panel.add(statutComboBox);
            //constat
            panel.add(new JLabel(data[2][0]));
            constatsField.setText(data[2][1]);
            panel.add(constatsField);
            //ecart
            panel.add(new JLabel(data[3][0]));
            ecartsField.setText(data[3][1]);
            panel.add(ecartsField);
            //Exclusion
            panel.add(new JLabel(data[4][0]));
            isExclus.setSelected(data[4][1].equals("Oui"));
            panel.add(isExclus);
            isExclus.addActionListener(e->{
                motifExclusionField.setEditable(isExclus.isSelected());
            });
            //motif
            panel.add(new JLabel(data[5][0]));
            motifExclusionField.setEditable(isExclus.isSelected());
            motifExclusionField.setText(data[5][1]);
            panel.add(motifExclusionField);
            return panel;

        }

        for (String[] row : data) {
            panel.add(new JLabel(row[0] + ":"));
            panel.add(new JLabel(row[1]));
        }

        return panel;
    }
}
