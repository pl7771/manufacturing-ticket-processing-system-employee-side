package org.tile.ticketing_system.gui.subscreens.overview;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.tile.ticketing_system.domein.controllers.DomainControllerAbstraction;
import org.tile.ticketing_system.gui.general.MainBorderPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GrafiekScherm extends TabPane {
	
	protected final DomainControllerAbstraction controllerForLoggedInUser;
	
	// -----------CONSTRUCTOR-------------------
	public GrafiekScherm(MainBorderPane mainBorderPane) {
		this.controllerForLoggedInUser = mainBorderPane.getControllerForLoggedInUser();
		this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
	}
	
	protected PieChart createPieChart(Map<String, Long> data, String titleChart) {
		PieChart pieChart = new PieChart();
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(data.entrySet().stream().map(e ->{
			return new PieChart.Data(e.getKey(), e.getValue());
		}).collect(Collectors.toList()));

		pieChart.setData(pieChartData);
		pieChart.setTitle(titleChart);
		return pieChart;
	}
	
	protected BarChart<String, Number> createBarChart(Map<String, ? extends Number> data, String titleChart, String titleX, String titleY) {
		// AXIS
				CategoryAxis xAxis = new CategoryAxis();
				xAxis.setLabel(titleX);
				NumberAxis yAxis = new NumberAxis();
				yAxis.setLabel(titleY);
				
				BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

				// DATASET1
				XYChart.Series series = new XYChart.Series<>();

				// Geeft per type een lijst van tickets
				data.entrySet().forEach(e ->{
					series.getData().add(new XYChart.Data(e.getKey(), e.getValue()));
				});

				// Voeg DATASET1 toe aan de BarChart
				barChart.getData().add(series);

				// OPMAAK
				barChart.setTitle(titleChart);
				barChart.setLegendVisible(false);

				for (Node n : barChart.lookupAll(".default-color0.chart-bar")) {
					n.setStyle("-fx-bar-fill: GREENYELLOW;");
				}
				return barChart;
	}
	
	protected LineChart<String, Number> createLineChart(Map<String, ? extends Number> data, String titleChart, String titleX, String titleY) {
		// AXIS
				CategoryAxis xAxis = new CategoryAxis();
				xAxis.setLabel(titleX);
				NumberAxis yAxis = new NumberAxis();
				yAxis.setLabel(titleY);
				
				LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

				// DATASET1
				XYChart.Series series = new XYChart.Series<>();

				// Geeft per type een lijst van tickets
				data.entrySet().forEach(e ->{
					series.getData().add(new XYChart.Data(e.getKey(), e.getValue()));
				});

				// Voeg DATASET1 toe aan de BarChart
				lineChart.getData().add(series);

				// OPMAAK
				lineChart.setTitle(titleChart);
				lineChart.setLegendVisible(false);
				
				return lineChart;
	}
	
	protected void tekenKPI(Accordion ref, String titleKPI, Map<String, String> data, Chart chart) {
		VBox kpiBox = new VBox();
		List<HBox> rows = data.entrySet().stream().map(e ->{
			HBox row = new HBox();
			Label kpiNaam = new Label(e.getKey());
			Label kpiWaarde = new Label(e.getValue());
			kpiNaam.setStyle("-fx-font: 16 arial;");
			kpiWaarde.setStyle("-fx-font: 16 arial;");
			if(e.getValue() == null) {
				kpiNaam.setUnderline(true);
				kpiNaam.setStyle("-fx-font: 18 arial;");
			}
			row.getChildren().add(kpiNaam);
			row.getChildren().add(kpiWaarde);
			return row;
			}).collect(Collectors.toList());
		kpiBox.getChildren().addAll(rows);
		if(chart != null) {
			kpiBox.getChildren().add(chart);
		}	
		ref.getPanes().add(new TitledPane(titleKPI, kpiBox));
	}
	
	protected void tekenKPI(Accordion ref, String titleKPI, Map<String, String> data) {
		tekenKPI(ref, titleKPI, data, null);
	}

	protected void tekenTab(String title, Chart chart) {
		VBox pane = new VBox(chart);
		pane.setAlignment(Pos.TOP_CENTER);
		double size = 0.9;
		double maxSize = 650;
		pane.setMaxHeight(Double.MAX_VALUE);
		pane.setMaxWidth(Double.MAX_VALUE);
		chart.prefWidthProperty().bind(this.widthProperty().multiply(size));
		chart.prefHeightProperty().bind(this.heightProperty().multiply(size));
		chart.setMaxHeight(maxSize);
		chart.setMaxWidth(maxSize*1.5);
		Tab tab = new Tab(title, pane);
		getTabs().add(tab);
	}
	
	protected void tekenTab(String title, Accordion kpiBox) {
		VBox pane = new VBox(kpiBox);
		Tab tab = new Tab(title, pane);
		getTabs().add(tab);
	}

}
