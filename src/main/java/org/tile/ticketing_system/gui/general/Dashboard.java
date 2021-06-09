package org.tile.ticketing_system.gui.general;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.tile.ticketing_system.domein.Ticket;
import org.tile.ticketing_system.domein.User;
import org.tile.ticketing_system.domein.controllers.DomainControllerAbstraction;
import org.tile.ticketing_system.gui.subscreens.edit.EditTicket;
import org.tile.ticketing_system.gui.subscreens.overview.Statistics;

public class Dashboard extends VBox {
	@FXML
	private HBox hBox1;
	@FXML
	private Label lblTicketChanges;
	@FXML
	private HBox hBoxTicketsLabel;
	@FXML
	private HBox hBox2;

	private final MainBorderPane mainBorderPane;
	private final DomainControllerAbstraction controllerForLoggedInUser;

	public Dashboard(MainBorderPane mainBorderPane) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		this.mainBorderPane = mainBorderPane;
		this.controllerForLoggedInUser = mainBorderPane.getControllerForLoggedInUser();
		this.tekenDashboard();
	}

	private void tekenDashboard() {
		User loggedInUser = this.controllerForLoggedInUser.getLoggedInUser();

		DashboardButton btnStatistieken = tekenButton("Statistieken");
		DashboardButton btnBeheerKlanten = tekenButton("Beheer klanten");
		DashboardButton btnBeheerWerknemers = tekenButton("Beheer werknemers");
		DashboardButton btnTicketAanmaken = tekenButton("Ticket aanmaken");
		DashboardButton btnContracten = tekenButton("Raadpleeg contracten");
		DashboardButton btnContractTypes = tekenButton("Raadpleeg contract types");
		DashboardButton btnKnowledgeBase = tekenButton("Raadpleeg knowledge base");
		DashboardButton openstaandeTickets = tekenButton("Openstaande tickets");
		DashboardButton afgehandeldeTickets = tekenButton("Afgehandelde tickets");


		if (loggedInUser.isAdministrator()) {
			hBox1.getChildren().addAll(
					btnBeheerKlanten,
					btnBeheerWerknemers);
		}
		if (loggedInUser.isSupportManager()) {
			hBox1.getChildren().addAll(
					btnStatistieken,
					btnTicketAanmaken,
					btnContracten,
					btnContractTypes);
		}
		if (loggedInUser.isTechnician()) {
			hBox1.getChildren().addAll(
					btnStatistieken,
					btnKnowledgeBase);
		}
		if (loggedInUser.isTechnician() || loggedInUser.isSupportManager()) {
			hBox2.getChildren().addAll(
					openstaandeTickets,
					afgehandeldeTickets);
			long ticketWijzigingen = controllerForLoggedInUser.getTicketWijzigingen();
			if(ticketWijzigingen == 0) {
				hBoxTicketsLabel.getChildren().remove(lblTicketChanges);
				System.out.println("remove lblTicketChanges");
			} else {
				lblTicketChanges.setText(String.valueOf(ticketWijzigingen));
			}
		} else {
			hBoxTicketsLabel.getChildren().clear();
		}


		btnTicketAanmaken.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				mainBorderPane.setOverzichtTickets();
				mainBorderPane.setRightScreen(new EditTicket(mainBorderPane));
			}
		});
		btnBeheerKlanten.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				mainBorderPane.setOverzichtGebruikers("client");
			}
		});
		btnBeheerWerknemers.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				mainBorderPane.setOverzichtGebruikers("employee");
			}
		});
		btnContracten.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				mainBorderPane.setOverzichtContracten();
			}
		});
		btnContractTypes.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				mainBorderPane.setOverzichtContractTypes();
			}
		});
		openstaandeTickets.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				mainBorderPane.setOverzichtTickets("OPENSTAAND");
			}
		});
		afgehandeldeTickets.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				mainBorderPane.setOverzichtTickets(Ticket.Status.AFGEHANDELD);
			}
		});
		btnStatistieken.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				mainBorderPane.setCenter(new Statistics(mainBorderPane));
			}
		});

	}

	private DashboardButton tekenButton(String lblText) {
		DashboardButton button = new DashboardButton(lblText);
		HBox.setMargin(button, new Insets(10, 0, 10, 20));
		return button;
	}

	public static class DashboardButton extends Button {

		@FXML
		private Button button;

		public DashboardButton(String lblText){
			FXMLLoader loader = new
					FXMLLoader(getClass().getResource("DashboardButton.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			try
			{
				loader.load();
			}
			catch (IOException ex)
			{
				throw new RuntimeException(ex);
			}
			this.setText(lblText);
		}

	}

}
