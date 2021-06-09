package org.tile.ticketing_system.gui.subscreens.overview;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.tile.ticketing_system.domein.Role;
import org.tile.ticketing_system.domein.User;
import org.tile.ticketing_system.domein.controllers.DomainControllerAbstraction;
import org.tile.ticketing_system.domein.interfaces.IUser;
import org.tile.ticketing_system.gui.general.Dashboard;
import org.tile.ticketing_system.gui.general.MainBorderPane;
import org.tile.ticketing_system.gui.subscreens.edit.EditKlant;
import org.tile.ticketing_system.gui.subscreens.edit.EditWerknemer;

public class GebruikerOverzicht extends VBox {

	@FXML
	private HBox tableGebruikers;
	@FXML
	private TableView<IUser> tabelOverzichtGebruikers;
	@FXML
	private TableColumn<IUser, String> colGebruikersnaam;
	@FXML
	private TableColumn<IUser, String> colVoornaam;
	@FXML
	private TableColumn<IUser, String> colNaam;
	@FXML
	private TableColumn<IUser, String> colCompany;
	@FXML
	private TableColumn<IUser, String> colRole;
	@FXML
	private TableColumn<IUser, String> colStatus;
	@FXML
	private TableColumn<IUser, IUser> colDetails;
	@FXML
	private TableColumn<IUser, IUser> colEdit;
	@FXML
	private Button buttonUserAanmaken;
	@FXML
	private Button buttonTerug;
	@FXML
	private TextField txfFilterVoornaam;
	@FXML
	private TextField txfFilterGebruikersnaam;
	@FXML
	private ComboBox<Role.RoleType> cmbFilterRole;
	@FXML
	private Button btnClearFilterRole;
	@FXML
	private ComboBox<User.StatusGebruiker> cmbFilterStatus;
	@FXML
	private Button btnClearFilterStatus;
	@FXML
	private Button buttonKlantAanmaken;
	@FXML
	private Button buttonWerknemerAanmaken;

	private final MainBorderPane mainBorderPane;
	private final DomainControllerAbstraction loggedInUserController;

	public GebruikerOverzicht(MainBorderPane mainBorderPane) {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("GebruikerOverzicht.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		this.mainBorderPane = mainBorderPane;
		this.loggedInUserController = mainBorderPane.getControllerForLoggedInUser();

		clearAllFilters();

		tabelOverzichtGebruikers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		//setCellValueFactory
		colGebruikersnaam.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getUserName()));
		colVoornaam.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getFirstName()));
		colNaam.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getLastName()));
		colCompany.setCellValueFactory(e -> new SimpleStringProperty(
				e.getValue().isKlant() ? loggedInUserController.getClientByApplicationUserId(e.getValue().getId()).getCompany().getName() : "-"));
		colRole.setCellValueFactory(e -> new SimpleStringProperty(
				e.getValue().getUserRoles().isEmpty() ? "Geen role togewezen" : e.getValue().getRole().toString())); // waarom 0? omdat er altijd maar een rol in de list van roles zit
		colStatus.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getStatusGebruiker()));

		//setItems
		tabelOverzichtGebruikers.setItems(loggedInUserController.getFilteredUserList());
		cmbFilterRole.setItems(FXCollections.observableArrayList(Role.RoleType.values()));
		cmbFilterStatus.setItems(FXCollections.observableArrayList(User.StatusGebruiker.values()));

		//Add change listener
		tabelOverzichtGebruikers.getSelectionModel().selectedItemProperty().addListener((obsV, oldV, newV) -> {
			try {
			var pos = tabelOverzichtGebruikers.getSelectionModel().getSelectedCells().get(0);
			int row = pos.getRow();
			var user = tabelOverzichtGebruikers.getItems().get(row);
			//set splitscreen
			if (user.isKlant())
				mainBorderPane.setRightScreen(new EditKlant(mainBorderPane, user));
			else
				mainBorderPane.setRightScreen(new EditWerknemer(mainBorderPane, user));
			} catch (IndexOutOfBoundsException iobe) {
				//er hoeft niets te gebeuren, na het filteren is er geen cel meer geselecteerd
			}
		});

		//filterOnAction
		cmbFilterRole.setOnAction(e -> {
			filter();
		});

		cmbFilterStatus.setOnAction(e -> {
			filter();
		});
	}

	@FXML
	void buttonKlantAanmakenOnAction(ActionEvent event) {
		mainBorderPane.setRightScreen(new EditKlant(mainBorderPane));
	}

	@FXML
	void buttonWerknemerAanmakenOnAction(ActionEvent event) {
		mainBorderPane.setRightScreen(new EditWerknemer(mainBorderPane));
	}

	@FXML
	void goBackToDashboard(MouseEvent event) {
		mainBorderPane.setCenter(new Dashboard(mainBorderPane));
	}

	@FXML
	void filterOpVoornaam(KeyEvent event) {
		filter();
	}

	@FXML
	void filterOpGebruikersnaam(KeyEvent event) {
		filter();
	}

	@FXML
	void clearFilterRole(ActionEvent event) {
		cmbFilterRole.valueProperty().set(null);
		filter();
	}

	@FXML
	void clearFilterStatus(ActionEvent event) {
		cmbFilterStatus.valueProperty().set(null);
		filter();
	}
	
	private void filter() {
		String role = cmbFilterRole.getValue() == null ? null : cmbFilterRole.getValue().name();
		filter(role);
	}
	
	public void filter(String role) {
		String status = cmbFilterStatus.getValue() == null ? null : cmbFilterStatus.getValue().name();
		String voornaam = txfFilterVoornaam.getText();
		String gebruikersnaam = txfFilterGebruikersnaam.getText();
		this.loggedInUserController.changeFilter_User(status, role, voornaam, gebruikersnaam);
	}
	
	private void clearAllFilters() {
		this.loggedInUserController.changeFilter_User(null, null, "", "");
	}
}
