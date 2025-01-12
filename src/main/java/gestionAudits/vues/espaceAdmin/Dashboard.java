package gestionAudits.vues.espaceAdmin;

import gestionAudits.controller.GestionAuditsController;
import gestionAudits.controller.StatistiqueController;
import gestionAudits.models.ActionStatistique;
import gestionAudits.models.Audit;
import gestionAudits.models.SytemExigenceStatistique;
import gestionAudits.models.Statistiques;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Dashboard extends JPanel {
    Statistiques statistiques;
    JPanel nombreSystemExigence=new JPanel();
    public Dashboard() {
        this.statistiques= StatistiqueController.getStatistiques();
        createUIComponents();
    }

    private void createUIComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Section 1 : Statistiques générales (en haut)
        add(createStatsPanel(), BorderLayout.NORTH);

        // Section 2 et 3 : Graphiques côte à côte (au centre)
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.add(createGraphContainerPanel());
//        centerPanel.add(createCompliancePanel());
        add(centerPanel, BorderLayout.CENTER);

        // Section 4 : Autre contenu (en bas)
      //  add(creat, BorderLayout.SOUTH);

    }

    // Section 1 : Statistiques générales
    private  JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Statistiques Générales"));
        panel.add(createStatCard("Audits Planifier", String.valueOf(this.statistiques.getNbrAuditsPlanifer())));
        panel.add(createStatCard("Audits En cours", String.valueOf(this.statistiques.getNbrAuditsEnCours())));
        panel.add(createStatCard("Audits Terminer", String.valueOf(this.statistiques.getNbrAuditsTerminer())));
        return panel;
    }

    // Section 2 et 3 : Graphiques Action et Conformité des Exigences dans un seul conteneur
    private JPanel createGraphContainerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel graphContainer = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Status d'audit"));
        // Liste des audits pour JComboBox
        List<Audit> audits = GestionAuditsController.listAudits();
        JComboBox<Audit> auditComboBox = new JComboBox<>(audits.toArray(new Audit[0]));

        // Initialisation des datasets pour les deux graphiques
        DefaultCategoryDataset actionsDataset = new DefaultCategoryDataset();
        DefaultPieDataset complianceDataset = new DefaultPieDataset();

        // Initialisation des graphiques
        JFreeChart actionsChart = createActionsChart(actionsDataset);
        JFreeChart complianceChart = createComplianceChart(complianceDataset);

        // Ajout des graphiques au conteneur
        graphContainer.add(new ChartPanel(complianceChart));
        graphContainer.add(new ChartPanel(actionsChart));


        // Mise à jour des graphiques selon la sélection d'un audit
        auditComboBox.addActionListener(e -> {
            Audit selectedAudit = (Audit) auditComboBox.getSelectedItem();
            if (selectedAudit != null) {
                // Mettre à jour les données des graphiques pour l'audit sélectionné
                updateActionStatisticsData(actionsDataset, selectedAudit.getId());
                updateComplianceData(complianceDataset, selectedAudit.getId());
                actionsChart.fireChartChanged();
                complianceChart.fireChartChanged();
            }
        });

        // Ajouter le JComboBox au dessus des graphiques
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Sélectionner un Audit :"));
        topPanel.add(auditComboBox,FlowLayout.CENTER);
        panel.add(topPanel, BorderLayout.NORTH);

        // Ajouter les graphiques au centre du panneau
        panel.add(graphContainer, BorderLayout.CENTER);

        return panel;
    }

    // Méthode pour créer le graphique des Actions
    private JFreeChart createActionsChart(DefaultCategoryDataset dataset) {
        ActionStatistique s=this.statistiques.getNbrActionParAudit().getFirst();

        dataset.addValue(s.getPrcEnCours(), "Actions", "En Cours");
        dataset.addValue(s.getPrcTerminer(), "Actions", "Terminés");

        return ChartFactory.createBarChart(
                "Les Actions",
                "Statut",
                "Nombre",
                dataset
        );
    }

    // Méthode pour créer le graphique de Conformité des Exigences
    private JFreeChart createComplianceChart(DefaultPieDataset dataset) {

        SytemExigenceStatistique s=this.statistiques.getNbrConformNonConformParAudit().getFirst();
        dataset.setValue("Conform",s.getPourcentageRespectees());
        dataset.setValue("Non Conform", s.getPourcentageNonRespectees());

        return ChartFactory.createPieChart(
                "Les Exigences",
                dataset,
                true,
                true,
                false
        );
    }

    // Mise à jour des données pour le graphique des Actions
    private void updateActionStatisticsData(DefaultCategoryDataset dataset, int auditId) {
        // Exemple de mise à jour des données (remplacez par vos vrais calculs)
        ActionStatistique s=this.statistiques.getNbrActionParAudit()
                        .stream().filter(st->st.getIdAudit()==auditId).findFirst().orElse(null);
        dataset.setValue(s.getPrcEnCours(), "Actions", "En Cours"); // Remplacer avec vos valeurs réelles
        dataset.setValue(s.getPrcTerminer(), "Actions", "Terminés");  // Remplacer avec vos valeurs réelles
    }

    // Mise à jour des données pour le graphique de Conformité des Exigences
    private void updateComplianceData(DefaultPieDataset dataset, int auditId) {
        // Exemple de mise à jour des données (remplacez par vos vrais calculs)

        SytemExigenceStatistique s=this.statistiques.getNbrConformNonConformParAudit()
                .stream().filter(st->st.getAuditId()==auditId).findFirst().orElse(null);

        dataset.setValue("Conform", s.getPourcentageRespectees());    // Remplacer avec vos valeurs réelles
        dataset.setValue("Non Conform", s.getPourcentageNonRespectees()); // Remplacer avec vos valeurs réelles
    }



//    // Section 2 : Graphique des audits par statut
//    private  JPanel createAuditStatusChartPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setBorder(BorderFactory.createTitledBorder("Statut des Actions"));
//        panel.add(createStatCard("Nombre des Actions", String.valueOf(this.statistiques.getNbrActionParAudit().getFirst().getNbrActions())), BorderLayout.NORTH);
//
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        dataset.addValue(this.statistiques.getNbrActionParAudit().getFirst().getPrcEnCours(), "Actions", "En Cours");
//        dataset.addValue(this.statistiques.getNbrActionParAudit().getFirst().getPrcTerminer(), "Actions", "Terminés");
//      //  dataset.addValue(this.statistiques.getNbrAuditsPlanifer(), "Actions", "Planifiés");
//
//        JFreeChart barChart = ChartFactory.createBarChart(
//                "Statut des Audits",
//                "Statut",
//                "Nombre",
//                dataset
//        );
//
//        ChartPanel chartPanel = new ChartPanel(barChart);
//        chartPanel.setPreferredSize(new Dimension(550, 400));
//        panel.add(chartPanel, BorderLayout.CENTER);
//
//        return panel;
//    }
//
//    private JPanel createCompliancePanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setBorder(BorderFactory.createTitledBorder("Conformité des Exigences"));
//
//        // Liste des audits
//        List<Audit> audits = GestionAuditsController.listAudits();
//        JComboBox<Audit> auditComboBox = new JComboBox<>(audits.toArray(new Audit[0]));
//
//        // Carte dynamique pour les statistiques
//        JPanel statCardPanel = new JPanel(new BorderLayout());
//        JPanel nombreSystemExigence = createStatCard("Nombre de systèmes d'exigence", "0");
//
//        // Initialisation de la carte avec le premier audit
//        if (!audits.isEmpty()) {
//            Audit firstAudit = audits.get(0);
//            SytemExigenceStatistique firstStat = statistiques.getNbrConformNonConformParAudit()
//                    .stream()
//                    .filter(stat -> stat.getAuditId() == firstAudit.getId())
//                    .findFirst()
//                    .orElse(null);
//
//            if (firstStat != null) {
//                nombreSystemExigence = createStatCard("Nombre de systèmes d'exigence", String.valueOf(firstStat.getNbrSytem()));
//            }
//            statCardPanel.add(nombreSystemExigence, BorderLayout.CENTER);
//        }
//
//        // Initialisation du graphique avec les données du premier audit
//        DefaultPieDataset pieDataset = new DefaultPieDataset();
//        if (!audits.isEmpty()) {
//            updateComplianceData(pieDataset, audits.get(0).getId());
//        }
//
//        JFreeChart pieChart = ChartFactory.createPieChart(
//                "Exigences Respectées vs Non Respectées",
//                pieDataset,
//                true,
//                true,
//                false
//        );
//
//        ChartPanel chartPanel = new ChartPanel(pieChart);
//        chartPanel.setPreferredSize(new Dimension(550, 400));
//
//        // Mise à jour lors de la sélection d'un audit
//        auditComboBox.addActionListener(e -> {
//            Audit selectedAudit = (Audit) auditComboBox.getSelectedItem();
//            if (selectedAudit != null) {
//                // Mise à jour des statistiques
//                SytemExigenceStatistique selectedStat = statistiques.getNbrConformNonConformParAudit()
//                        .stream()
//                        .filter(stat -> stat.getAuditId() == selectedAudit.getId())
//                        .findFirst()
//                        .orElse(null);
//
//                if (selectedStat != null) {
//                    JPanel updatedStatCard = createStatCard(
//                            "Nombre de systèmes d'exigence",
//                            String.valueOf(selectedStat.getNbrSytem())
//                    );
//
//                    // Remplacer l'ancienne carte par la nouvelle
//                    statCardPanel.removeAll();
//                    statCardPanel.add(updatedStatCard, BorderLayout.CENTER);
//                    statCardPanel.revalidate();
//                    statCardPanel.repaint();
//
//                    // Mise à jour du graphique
//                    updateComplianceData(pieDataset, selectedAudit.getId());
//                    pieChart.fireChartChanged();
//                }
//            }
//        });
//
//        // Ajouter le JComboBox en haut du panneau
//        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        topPanel.add(new JLabel("Sélectionner un Audit :"));
//        topPanel.add(auditComboBox);
//        panel.add(topPanel, BorderLayout.NORTH);
//
//        // Ajouter la carte dynamique
//        panel.add(statCardPanel, BorderLayout.CENTER);
//
//        // Ajouter le graphique en bas
//        panel.add(chartPanel, BorderLayout.SOUTH);
//
//        return panel;
//    }
//
//    // Mise à jour des données de conformité en fonction de l'audit sélectionné
//    private  void updateComplianceData(DefaultPieDataset dataset, int audit) {
//        // Ici, vous récupérez les données depuis la base de données en fonction de l'audit sélectionné
//        // Exemples de données fictives pour la démonstration
//
//        SytemExigenceStatistique sytemExigenceStatistique =this.statistiques.getNbrConformNonConformParAudit()
//                .stream().filter(a->a.getAuditId()==audit).findFirst().orElse(null);
//       // nombreSystemExigence=createStatCard("Nombre de system d'éxigence", String.valueOf(sytemExigenceStatistique.getNbrSytem()));
//        dataset.setValue("Conform", sytemExigenceStatistique.getPourcentageRespectees());
//        dataset.setValue("Non Conform", sytemExigenceStatistique.getPourcentageNonRespectees());
//
//    }

    // Section 4 : Autre contenu (actions correctives)
    private static JPanel createOtherSection() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Actions Correctives"));
        panel.add(createStatCard("Actions En Cours", "15"));
        panel.add(createStatCard("Actions Terminées", "10"));
        return panel;
    }

    // Méthode utilitaire pour créer une carte de statistique
    private static JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        valueLabel.setForeground(new Color(0, 102, 204));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }



}
