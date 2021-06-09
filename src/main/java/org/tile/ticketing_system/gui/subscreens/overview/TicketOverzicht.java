package org.tile.ticketing_system.gui.subscreens.overview;

import java.io.IOException;
import java.util.stream.Stream;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.tile.ticketing_system.domein.Ticket;
import org.tile.ticketing_system.domein.controllers.DomainControllerAbstraction;
import org.tile.ticketing_system.domein.interfaces.ITicket;
import org.tile.ticketing_system.gui.subscreens.edit.EditTicket;
import org.tile.ticketing_system.gui.general.Dashboard;
import org.tile.ticketing_system.gui.general.MainBorderPane;

public class TicketOverzicht extends VBox {

	@FXML
	private TableView<ITicket> tabelOverzichtTickets;

	@FXML
	private TableColumn<ITicket, Integer> ticketId;

	@FXML
	private TableColumn<ITicket, String> titel;

	@FXML
	private TableColumn<ITicket, Integer> aantalWijzigingen;

	@FXML
	private TableColumn<ITicket, String> status;

	@FXML
	private TableColumn<ITicket, String> company;

	@FXML
	private TableColumn<ITicket, Boolean> isGewijzigd;

	@FXML
	private Button buttonTcketAanmaken;

	@FXML
	private Button buttonTerug;

	@FXML
	private ComboBox<Ticket.Status> cmbFIlterStatus;

	@FXML
	private Button btnClearStatusFilter;

	private final MainBorderPane mainBorderPane;
	private final DomainControllerAbstraction controllerForLoggedInUser;

	public TicketOverzicht(MainBorderPane mainBorderPane) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TicketOverzicht.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		this.mainBorderPane = mainBorderPane;
		this.controllerForLoggedInUser = mainBorderPane.getControllerForLoggedInUser();
		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		tabelOverzichtTickets.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		//set Last Session
		controllerForLoggedInUser.updateLastSession();
		
		//setCellValueFactory
		ticketId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getTicketId()).asObject());
		titel.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitel()));
		aantalWijzigingen.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getAantalWijzigingen()).asObject());
		status.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus().toString()));
		company.setCellValueFactory(data -> {
			if(data.getValue().getCompany() == null){
				return new SimpleStringProperty("geen bedrijf");
			}
			return new SimpleStringProperty(data.getValue().getCompany().getName());
		});
		isGewijzigd.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isGewijzigd()));
		
		//setItems
		tabelOverzichtTickets.setItems(this.controllerForLoggedInUser.getAllTickets());
		cmbFIlterStatus.setItems(FXCollections.observableArrayList(Ticket.Status.values()));
		
		//Add change listener
		tabelOverzichtTickets.getSelectionModel().selectedItemProperty().addListener((obsV, oldV, newV) -> {
			try {
			TablePosition pos = tabelOverzichtTickets.getSelectionModel().getSelectedCells().get(0);
			int row = pos.getRow();
			ITicket ticket = tabelOverzichtTickets.getItems().get(row);
			//set splitscreen
			mainBorderPane.setRightScreen(new EditTicket(mainBorderPane, ticket));
			} catch (IndexOutOfBoundsException iobe) {
				//er hoeft niets te gebeuren, na het filteren is er geen cel meer geselecteerd
			}
		});

//		Platform.runLater(() ->
//				Stream.of(ticketId, isGewijzigd)
//						.forEach(cmn -> tabelOverzichtTickets.resizeColumn(cmn, (-1. / 4) * tabelOverzichtTickets.getWidth())));

		//filterOnAction
		cmbFIlterStatus.setOnAction(e -> {
			if(cmbFIlterStatus.getValue() != null) {
			String filterValue = cmbFIlterStatus.getValue().name();
			controllerForLoggedInUser.changeFilter_Ticket_Status(filterValue);
			}
		});
	}

	@FXML
	void goBackToDashboard(MouseEvent event) {
		mainBorderPane.setCenter(new Dashboard(mainBorderPane));
	}

	@FXML
	void goToScreenTicketAanmaken(MouseEvent event) {
		mainBorderPane.setRightScreen(new EditTicket(mainBorderPane));
	}

	@FXML
	void clearStatusFilter(ActionEvent event) {
		this.cmbFIlterStatus.valueProperty().set(null);
		controllerForLoggedInUser.changeFilter_Ticket_Status("");
	}
	
	public void setStatusFilter(Ticket.Status status) {
		cmbFIlterStatus.valueProperty().set(status);
		controllerForLoggedInUser.changeFilter_Ticket_Status(status.name());
	}
	
	public void setStatusFilter(String status) {
		controllerForLoggedInUser.changeFilter_Ticket_Status(status);
	}
}
