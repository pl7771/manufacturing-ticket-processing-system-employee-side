package org.tile.ticketing_system.gui.subscreens.edit;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.tile.ticketing_system.domein.User;
import org.tile.ticketing_system.domein.User.StatusGebruiker;
import org.tile.ticketing_system.domein.dto.CreateEmployeeDto;
import org.tile.ticketing_system.domein.dto.EditEmployeeDto;
import org.tile.ticketing_system.domein.interfaces.IEmployee;
import org.tile.ticketing_system.domein.interfaces.IRole;
import org.tile.ticketing_system.domein.interfaces.IUser;
import org.tile.ticketing_system.domein.controllers.DomainControllerAbstraction;
import org.tile.ticketing_system.gui.general.MainBorderPane;

public class EditWerknemer extends GridPane {

	@FXML
	private TextField txfPersoneelsnummer;
	@FXML
	private TextField txfVoornaam;
	@FXML
	private TextField txfNaam;
	@FXML
	private TextField txfGebruikersnaam;
	@FXML
	private TextField txfPhone;
	@FXML
	private ComboBox<IRole> cbxRole;
	@FXML
	private TextField txfAdres;
	@FXML
	private Button btnBevestigWijzigingen;
	@FXML
	private Button btnAnnuleren;
	@FXML
	private ComboBox<User.StatusGebruiker> cbxStatus;

	private final MainBorderPane mainBorderPane;
	private final DomainControllerAbstraction loggedInUserController;
	private IUser userToEdit;
	private IEmployee employeeToEdit;

	// -----------CONSTRUCTOR-------------
	public EditWerknemer(MainBorderPane mainBorderPane) {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("EditWerknemerScreen.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		this.mainBorderPane = mainBorderPane;
		this.loggedInUserController = mainBorderPane.getControllerForLoggedInUser();

		// filter roles
		txfPersoneelsnummer.setDisable(true);
		loggedInUserController.changeFilter_Role_WithoutClient(true);
		// setFields
		setCreateWerknemerFields();
		// setFocus
		txfVoornaam.requestFocus();
	}

	public EditWerknemer(MainBorderPane mainBorderPane, IUser user) {
		this(mainBorderPane);
		this.userToEdit = user;
		this.employeeToEdit = loggedInUserController.getEmployeeByApplicationUserId(user.getId());
		this.setEditWerknemerFields();
		cbxStatus.setDisable(false);
	}

	private void setCreateWerknemerFields() {
		cbxStatus.setItems(FXCollections.observableArrayList(User.StatusGebruiker.values()));
		cbxRole.setItems(loggedInUserController.getAllRoles());
		cbxStatus.setValue(StatusGebruiker.GEBLOKKEERD);
		cbxStatus.setDisable(true);
	}

	// -----------SET FIELDS-------------
	private void setEditWerknemerFields() {
		txfPersoneelsnummer.setText(employeeToEdit.getEmployeeId());
		txfPersoneelsnummer.setDisable(true);
		txfVoornaam.setText(userToEdit.getFirstName() == null ? "" : userToEdit.getFirstName());
		txfNaam.setText(userToEdit.getLastName() == null ? "" : userToEdit.getLastName());
		txfGebruikersnaam.setText(userToEdit.getUserName() == null ? "" : userToEdit.getUserName());
		cbxRole.setValue(userToEdit.getRole());
		txfAdres.setText(employeeToEdit.getAddress());
		cbxStatus.setItems(FXCollections.observableArrayList(User.StatusGebruiker.values()));
		cbxStatus.setValue(userToEdit.getStatusGebruiker() == null ? StatusGebruiker.GEBLOKKEERD
				: StatusGebruiker.valueOf(userToEdit.getStatusGebruiker()));
		txfPhone.setText(userToEdit.getPhoneNumber() == null ? "" : userToEdit.getPhoneNumber());
	}

	// -----------BUTTONEVENTS-------------
	@FXML
	void annulerenWerknemerWijzigen(MouseEvent event) {
		this.mainBorderPane.removeRightScreen();
	}

	@FXML
	void bevestigWijzigenWerknemer(MouseEvent event) {
		if (this.userToEdit == null)
			createEmployee();
		else
			editEmployee();
	}

	private void editEmployee() {
		try {
			loggedInUserController.editEmployee(new EditEmployeeDto(userToEdit.getId(), txfVoornaam.getText(),
					txfNaam.getText(), txfGebruikersnaam.getText(), txfPhone.getText(), cbxRole.getValue(),
					txfAdres.getText(), cbxStatus.getValue()));
			showAlert("Gelukt", "Editeren employee gelukt", null);
			this.mainBorderPane.setOverzichtGebruikers();
		} catch (Exception e) {
			showAlert("Niet gelukt", "Editeren employee niet gelukt", e);
		}
	}

	private void createEmployee() {
		try {
			loggedInUserController.createEmployee(new CreateEmployeeDto(
					txfVoornaam.getText(),
					txfNaam.getText(),
					txfGebruikersnaam.getText(),
					txfPhone.getText(),
					txfAdres.getText(),
					cbxRole.getValue() == null ? null : cbxRole.getValue().getName()
					));
			showAlert("Gelukt", "Creeren employee gelukt", null);
			this.mainBorderPane.setOverzichtGebruikers();
		} catch (Exception e) {
			showAlert("Niet gelukt", "Creeren employee niet gelukt", e);
		}
	}

	private void showAlert(String s1, String s2, Exception e) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(
				getClass().getResource("/org/tile/ticketing_system/gui/style.css").toExternalForm());
		dialogPane.getStyleClass().add("dialog-pane");

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/images/actemium-logo-empotybg.png"));
		alert.setTitle(s1);
		alert.setHeaderText(s2);
		if (e != null) {
			alert.setGraphic(new ImageView(new Image("/images/notcorrect.png")));
			alert.setContentText(e.getMessage());
		}
		else{
			alert.setGraphic(new ImageView(new Image("/images/correct.png")));

		}
		alert.showAndWait();
	}

}
