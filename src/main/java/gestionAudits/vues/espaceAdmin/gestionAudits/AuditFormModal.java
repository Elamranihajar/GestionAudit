package gestionAudits.vues.espaceAdmin.gestionAudits;


import com.toedter.calendar.JCalendar;
import gestionAudits.models.Audit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class AuditFormModal extends JDialog {


    private JTextArea description;
    private JComboBox<String> type;
    private JTextField dateDebutField;
    private JTextField dateFinField;
    private boolean isSubmitted = false;
    private String typeOperation;
    private int idAudit;
    private List<Audit> auditList;
    private Audit audit;

    public AuditFormModal(Frame parent, Audit audit, String typeOperation) {
        super(parent, typeOperation + " Audit", true);
        this.typeOperation = typeOperation;
        idAudit = audit != null ? audit.getId() : 0;
        this.audit = audit;
        System.out.println(this.audit);
        createForm(parent, typeOperation, audit);
    }

    private void createForm(Frame parent, String typeOperation, Audit audit) {
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
        formPanel.add(new JLabel("Description :"), gbc);

        gbc.gridx = 1;
        description = new JTextArea(3, 20);
        description.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(description, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Type :"), gbc);

        gbc.gridx = 1;
        type = new JComboBox<>(new String[]{"intern", "extern"});
        formPanel.add(type, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Date début :"), gbc);

        gbc.gridx = 1;
        dateDebutField = new JTextField(20);
        formPanel.add(dateDebutField, gbc);
        // Ajouter l'interaction pour afficher un calendrier
        addCalendarPopup(dateDebutField);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Date fin :"), gbc);

        gbc.gridx = 1;
        dateFinField = new JTextField(20);
        formPanel.add(dateFinField, gbc);
        // Ajouter l'interaction pour afficher un calendrier
        addCalendarPopup(dateFinField);

        if (typeOperation.equals("Modifier")){
           description.setText(this.audit.getDescription());
           type.setSelectedItem(this.audit.getType());
           dateDebutField.setText(this.audit.getDateDebut().toString());
           dateFinField.setText(this.audit.getDateFin().toString());
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

    private void addCalendarPopup(JTextField textField) {
        textField.setEditable(false); // Rendre le champ non éditable
        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Créer un calendrier dans une boîte de dialogue
                JDialog calendarDialog = new JDialog();
                calendarDialog.setModal(true);
                calendarDialog.setSize(300, 300);
                calendarDialog.setLocationRelativeTo(textField);

                JCalendar calendar = new JCalendar();
                calendarDialog.add(calendar);

                calendar.addPropertyChangeListener("calendar", evt -> {
                    textField.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getDate()));
                    calendarDialog.dispose(); // Fermer la boîte de dialogue
                });

                calendarDialog.setVisible(true);
            }
        });
    }

    private void onSubmit() {
        if (validateFields()) {
            if(audit==null)
                audit=new Audit();

            audit.setDescription(description.getText());
            audit.setStatus("En cours");
            audit.setType(type.getSelectedItem().toString());
            try {
                audit.setDateDebut(Date.valueOf(dateDebutField.getText().trim()));
                audit.setDateFin(Date.valueOf(dateFinField.getText().trim()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            isSubmitted = true;
            dispose();
        }
    }

    public Audit getAudit() {return audit;}

    private boolean validateFields() {
        if (description.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La description est requise.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (dateDebutField.getText().isEmpty() || dateFinField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Les dates sont requises.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            if (Date.valueOf(dateDebutField.getText().trim()).
                    after(Date.valueOf(dateFinField.getText().trim()))) {
                JOptionPane.showMessageDialog(this, "La date de début doit être avant la date de fin.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Format de date invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void onCancel() {
        isSubmitted = false;
        dispose();
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }
}
