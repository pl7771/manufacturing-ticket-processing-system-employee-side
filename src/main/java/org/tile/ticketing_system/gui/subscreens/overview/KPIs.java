package org.tile.ticketing_system.gui.subscreens.overview;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.tile.ticketing_system.domein.controllers.DomainControllerAbstraction;
import org.tile.ticketing_system.gui.general.MainBorderPane;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.Chart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class KPIs extends GrafiekScherm {

	public KPIs(MainBorderPane mainBorderPane) {
		super(mainBorderPane);

		// teken KPIs
		tekenTicketKPI();
		tekenContractKPI();
	}
	
	private void tekenTicketKPI() {
		Accordion accordion = new Accordion();
		//kpi1
		tekenKPI(accordion, "tickets afgewerkt binnen de tijd", controllerForLoggedInUser.getKPITicketsAfgewerktBinnenTijd());
		//Kpi2
		Chart chart = createBarChart(
				controllerForLoggedInUser.getStatTicketsGemiddeldeOpenstaandPerMaand(),
				"openstaande tickets per dag, gemiddelde per maand",
				"maand",
				"openstaande tickets"
				);
		tekenKPI(accordion, "tickets openstaand per dag", controllerForLoggedInUser.getKPITicketsOpenstaandPerDag(), chart);

		//tab maken
		tekenTab("Tickets", accordion);	
	}
	
	private void tekenContractKPI() {
		Accordion accordion = new Accordion();
		//kpi1
		Chart chart = createBarChart(
				controllerForLoggedInUser.getStatContractenGemiddeldeLopendPerMaand(),
				"lopende contracten per dag, gemiddelde per maand",
				"maand",
				"lopende contracten"
				);
		tekenKPI(accordion, "Lopende contracten per dag", controllerForLoggedInUser.getKPIContractenLopendePerDag(), chart);
		

		//tab maken
		tekenTab("Contracten", accordion);	
		
	}

}
