package org.tile.ticketing_system.gui.subscreens.overview;
import org.tile.ticketing_system.gui.general.MainBorderPane;

import javafx.scene.chart.Chart;

public class Statistics extends GrafiekScherm {

	// -----------CONSTRUCTOR-------------------
	public Statistics(MainBorderPane mainBorderPane) {
		super(mainBorderPane);

		// Set Charts
		setStatStatusTicket();
		setStatContractTypeTicket();
		setStatGemiddeldeLooptijd();
		setStatTicketsAfgewerktPerMaand();
		setStatTicketsAangemaaktPerMaand();
		setStatCompletedInTimePercentageContractTypes();
		setStatPercentageTicketsCompletedInTimePerMonth();
	}

	private void setStatStatusTicket() {
		Chart chart = createPieChart(controllerForLoggedInUser.getStatStatusTicket(), "Tickets per status");
		tekenTab("Ticket Status",chart);
	}

	private void setStatContractTypeTicket() {
		Chart chart = createPieChart(controllerForLoggedInUser.getStatContractTypeTicket(), "Tickets per Contract Type");
		tekenTab("Ticket ContractType", chart);
	}
	
	private void setStatGemiddeldeLooptijd() {
		Chart chart = createBarChart(
				controllerForLoggedInUser.getStatGemiddeldeLooptijd(),
				"Gemiddelde looptijd van tickets per contract type",
				"Contract Type",
				"Gemiddelde in dagen"
				);
		tekenTab("Ticket gemiddelde looptijd", chart);
	}

//	private double getGemiddeldAantalWijzigingenTickets() {
//
//		return controllerForLoggedInUser.getAllTickets().stream()
//				.filter(e -> e.getStatus() == Ticket.Status.AFGEHANDELD).mapToDouble(e -> e.getAantalWijzigingen())
//				.average().orElse(0);
//	}

	private void setStatTicketsAfgewerktPerMaand() {
		Chart chart = createBarChart(
				controllerForLoggedInUser.getStatTicketsAfgewerktPerMaand(),
				"Tickets afgewerkt per maand",
				"Maand",
				"Aantal"
				);
		tekenTab("Afgewerkte tickets per maand", chart);
	}
	
	private void setStatTicketsAangemaaktPerMaand() {
		Chart chart = createBarChart(
				controllerForLoggedInUser.getStatTicketsAangemaaktPerMaand(),
				"Tickets aangemaakt per maand",
				"Maand",
				"Aantal"
				);
		tekenTab("Aangemaakte tickets per maand", chart);
	}
	
	private void setStatCompletedInTimePercentageContractTypes() {
		Chart chart = createBarChart(
				controllerForLoggedInUser.getStatCompletedInTimePercentageContractTypes(),
				"Tickets tijdig afgehandeld per type contract",
				"Contract Type",
				"Percentage"
				);
		tekenTab("Tickets tijdig afgehandeld per type contract", chart);
	}
	
	private void setStatPercentageTicketsCompletedInTimePerMonth() {
		Chart chart = createLineChart(controllerForLoggedInUser.getStatPercentageTicketsCompletedInTimePerMonth(),
				"Percentage tickets tijdig afgehandeld per maand",
				"Maand",
				"Percentage");
		tekenTab("Percentage tickets tijdig afgehandeld per maand", chart);
	}
}
