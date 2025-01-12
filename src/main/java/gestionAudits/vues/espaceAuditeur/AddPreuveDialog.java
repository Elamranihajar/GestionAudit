package gestionAudits.vues.espaceAuditeur;

import gestionAudits.models.Preuve;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;

public class AddPreuveDialog extends JDialog {

    private JTextField preuveNameField;
    private JLabel selectedFileLabel;
    private JTextField urlField;
    private JRadioButton fileOption;
    private JRadioButton urlOption;
    private JButton importFileButton;
    private File selectedFile;
    private Preuve preuve;
    private boolean isSubmit;

    public AddPreuveDialog(Frame parent) {
        super(parent, "Ajouter une preuve", true);
        setSize(600, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Panel principal
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Champ pour le nom de la preuve
        JLabel proofNameLabel = new JLabel("Nom de la preuve :");
        proofNameLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        formPanel.add(proofNameLabel, gbc);

        gbc.gridx = 1;
        preuveNameField = new JTextField();
        preuveNameField.setFont(new Font("Roboto", Font.PLAIN, 14));
        preuveNameField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0, 153, 204), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(preuveNameField, gbc);

        // Options pour importer un fichier ou saisir une URL
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel chooseOptionLabel = new JLabel("Choisissez une option :");
        chooseOptionLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        formPanel.add(chooseOptionLabel, gbc);

        gbc.gridx = 1;
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionsPanel.setBackground(Color.WHITE);

        fileOption = new JRadioButton("Importer un fichier");
        fileOption.setFont(new Font("Roboto", Font.PLAIN, 14));
        fileOption.setBackground(Color.WHITE);
        fileOption.setSelected(false); // Option par défaut

        urlOption = new JRadioButton("Saisir une URL");
        urlOption.setFont(new Font("Roboto", Font.PLAIN, 14));
        urlOption.setBackground(Color.WHITE);

        ButtonGroup optionGroup = new ButtonGroup();
        optionGroup.add(fileOption);
        optionGroup.add(urlOption);

        optionsPanel.add(fileOption);
        optionsPanel.add(urlOption);

        formPanel.add(optionsPanel, gbc);

        // Champ pour l'importation de fichier
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel fileLabel = new JLabel("Importer un fichier :");
        fileLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        formPanel.add(fileLabel, gbc);

        gbc.gridx = 1;
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filePanel.setBackground(Color.WHITE);
        importFileButton = new JButton("Importer");
        importFileButton.setFont(new Font("Roboto", Font.BOLD, 14));
        importFileButton.setBackground(new Color(52, 152, 219));
        importFileButton.setForeground(Color.WHITE);
        importFileButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        importFileButton.setFocusPainted(false);
        importFileButton.addActionListener(e -> selectFile());
        filePanel.add(importFileButton);

        selectedFileLabel = new JLabel("Aucun fichier sélectionné");
        selectedFileLabel.setFont(new Font("Roboto", Font.ITALIC, 12));
        selectedFileLabel.setForeground(Color.GRAY);
        filePanel.add(selectedFileLabel);

        filePanel.setVisible(false);
        fileLabel.setVisible(false);


        formPanel.add(filePanel, gbc);

        // Champ pour l'URL
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel urlLabel = new JLabel("Saisir une URL :");
        urlLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        formPanel.add(urlLabel, gbc);

        gbc.gridx = 1;
        urlField = new JTextField();
        urlField.setFont(new Font("Roboto", Font.PLAIN, 14));
        urlField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0, 153, 204), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(urlField, gbc);

        urlOption.setSelected(true);
        // Listener pour activer/désactiver les champs selon l'option choisie
        fileOption.addActionListener(e -> {
//            importFileButton.setEnabled(true);
//            selectedFileLabel.setEnabled(true);
            filePanel.setVisible(true);
            fileLabel.setVisible(true);
            urlLabel.setVisible(false);
            urlField.setVisible(false);
        });

        urlOption.addActionListener(e -> {
            filePanel.setVisible(false);
            fileLabel.setVisible(false);
            urlLabel.setVisible(true);
            urlField.setVisible(true);
        });

        add(formPanel, BorderLayout.CENTER);

        // Bouton Enregistrer
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);

        JButton saveButton = new JButton("Enregistrer");
        saveButton.setFont(new Font("Roboto", Font.BOLD, 16));
        saveButton.setBackground(new Color(46, 204, 113));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(e -> saveProof());
        buttonPanel.add(saveButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }


    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sélectionner un fichier");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Ajouter un filtre pour restreindre les types de fichiers (par exemple : PDF, images, documents)
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Fichiers PDF", "pdf"));
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images (JPEG, PNG)", "jpg", "jpeg", "png"));
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Documents Word (DOC, DOCX)", "doc", "docx"));
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Tous les fichiers", "*"));

        // Afficher le sélecteur de fichiers
        int result = fileChooser.showOpenDialog(this);

        // Gérer la sélection
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            selectedFileLabel.setText("Fichier sélectionné : " + selectedFile.getName());
            selectedFileLabel.setForeground(new Color(34, 153, 84)); // Couleur verte pour indiquer le succès
        } else {
            selectedFileLabel.setText("Aucun fichier sélectionné");
            selectedFileLabel.setForeground(Color.GRAY); // Couleur grise pour indiquer l'absence de sélection
        }
    }



    private void saveProof() {
        String preuveName = preuveNameField.getText();
        if (preuveName.isEmpty() ||
                (fileOption.isSelected() && selectedFile == null) ||
                (urlOption.isSelected() && urlField.getText().isEmpty())) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        preuve = new Preuve();
        preuve.setName(preuveName);
        if (fileOption.isSelected()) {
            preuve.setUrl(selectedFile.toURI().toString());
        } else {
            preuve.setUrl(urlField.getText());
        }
        isSubmit = true;
        dispose();
    }

    public Preuve getPreuve() {
        return preuve;
    }

    public boolean isSubmit() {
        return isSubmit;
    }
}
