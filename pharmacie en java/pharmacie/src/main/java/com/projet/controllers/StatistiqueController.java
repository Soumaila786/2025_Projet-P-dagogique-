package com.projet.controllers;

import java.util.Map;

import com.projet.services.DBConnection;
import com.projet.services.VenteService;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

public class StatistiqueController {
    @FXML
    private AnchorPane anchorStatistiques;
    @FXML
    private LineChart<String, Number> barcChartStatistiquesAnnee;
    @FXML
    private LineChart<String, Number> barcChartStatistiquesMois;
    @FXML
    private LineChart<String, Number> barcChartStatistiquesSemaine;
    @FXML
    private BarChart<String, Number> barcChartStatistiquesVentesJour;
    private VenteService venteService;


    @FXML
    private void initialize() {
        
        try{
            venteService = new VenteService(DBConnection.getConnection());
            chargerVentesDuJour();
            chargerVentesSemaine();
            chargerVentesParMois();
            chargerVentesParAnnee();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chargerVentesDuJour() {
        try {
            Map<String, Double> ventesJour = venteService.getVentesParHeureAujourdhui();
            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.setName("Ventes du jour");
            for (Map.Entry<String, Double> entry : ventesJour.entrySet()) {
                serie.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            barcChartStatistiquesVentesJour.getData().clear();
            barcChartStatistiquesVentesJour.getData().add(serie);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chargerVentesSemaine() {
        try {
            Map<String, Double> ventesSemaine = venteService.getVentesParJourSemaine();
            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.setName("Ventes de la semaine");
            for (Map.Entry<String, Double> entry : ventesSemaine.entrySet()) {
                serie.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            barcChartStatistiquesSemaine.getData().clear();
            barcChartStatistiquesSemaine.getData().add(serie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chargerVentesParMois() {
        try {
            Map<String, Double> ventesMois = venteService.getVentesParMois();

            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.setName("Ventes mensuelles");

            for (Map.Entry<String, Double> entry : ventesMois.entrySet()) {
                serie.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            barcChartStatistiquesMois.getData().clear();
            barcChartStatistiquesMois.getData().add(serie);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chargerVentesParAnnee() {
        try {
            Map<String, Double> ventesAnnee = venteService.getVentesParAnnee();

            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.setName("Ventes annuelles");

            for (Map.Entry<String, Double> entry : ventesAnnee.entrySet()) {
                serie.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            barcChartStatistiquesAnnee.getData().clear();
            barcChartStatistiquesAnnee.getData().add(serie);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
