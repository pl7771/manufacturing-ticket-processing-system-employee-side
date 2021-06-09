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
import lombok.extern.java.Log;
import org.tile.ticketing_system.domein.User;
import org.tile.ticketing_system.domein.User.StatusGebruiker;
import org.tile.ticketing_system.domein.dto.CreateClientDto;
import org.tile.ticketing_system.domein.dto.EditClientDto;
import org.tile.ticketing_system.domein.interfaces.IUser;
import org.tile.ticketing_system.domein.controllers.DomainControllerAbstraction;
import org.tile.ticketing_system.gui.general.MainBorderPane;

@Log
public class EditKlant extends GridPane {

	@FXML
	private TextField txfGebruikernaam;
	@FXML
	private TextField txfNaam;
	@FXML
	private TextField txfVoornaam;
	@FXML
	private ComboBox<String> cbxBedrijf;
	@FXML
	private ComboBox<User.StatusGebruiker> cbxStatus;
	@FXML
	private TextField txfContactPersoon;
	@FXML
	private Button btnBevestigWijzigingen;
	@FXML
	private Button btnAnnuleren;

	private final MainBorderPane mainBorderPane;
	private final DomainControllerAbstraction loggedInUserController;
	private IUser userToEdit;

	public EditKlant(MainBorderPane mainBorderPane) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("EditKlantScreen.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		this.mainBorderPane = mainBorderPane;
		this.loggedInUserController = mainBorderPane.getControllerForLoggedInUser();
		setCreateKlantFields();
		// TO DO - CONTACT PERSON
	}

	public EditKlant(MainBorderPane mainBorderPane, IUser user) {
		this(mainBorderPane);
		this.userToEdit = user;
		// Set Fields
		setEditKlantFields(user);
	}

	private void setCreateKlantFields(){
		cbxBedrijf.setItems(FXCollections.observableArrayList(this.loggedInUserController.getAllCompanyNames()));
		//cbxStatus.setItems(FXCollections.observableArrayList(User.StatusGebruiker.values()));
		cbxStatus.setValue(StatusGebruiker.GEBLOKKEERD);
		cbxStatus.setDisable(true);
	}

	private void setEditKlantFields(IUser user){
		txfGebruikernaam.setText(userToEdit.getUserName() == null ? "" : userToEdit.getUserName());
		txfNaam.setText(userToEdit.getLastName() == null ? "" : userToEdit.getLastName());
		txfVoornaam.setText(userToEdit.getFirstName() == null ? "" : userToEdit.getFirstName());
		cbxBedrijf.setValue(user.isKlant() ? loggedInUserController.getClientByApplicationUserId(user.getId()).getCompany().getName() : "-");
		cbxStatus.setItems(FXCollections.observableArrayList(User.StatusGebruiker.values()));
		cbxStatus.setValue(userToEdit.getStatusGebruiker() == null ? StatusGebruiker.GEBLOKKEERD : StatusGebruiker.valueOf(userToEdit.getStatusGebruiker()));
		cbxStatus.setDisable(false);
	}

	@FXML
	void annulerenKlantWijzigen(MouseEvent event) {
		this.mainBorderPane.removeRightScreen();
	}

	@FXML
	void bevestigWijzigenKlant(MouseEvent event) {
		if (this.userToEdit == null)
			createClient();
		else
			editClient();
	}

	private void editClient() {
		try {
			this.loggedInUserController.editClient(new EditClientDto(
					userToEdit.getId(),
					cbxBedrijf.getValue(),
					txfVoornaam.getText(),
					txfNaam.getText(),
					txfGebruikernaam.getText(),
					cbxStatus.getValue()));
			showAlert("Gelukt", "Editeren klant gelukt", null);
			this.mainBorderPane.setOverzichtGebruikers();
		} catch (Exception e) {
			showAlert("Niet gelukt", "Editeren klant niet gelukt", e);
		}

	}
	
	private void createClient() {
		try {
			this.loggedInUserController.createClient(new CreateClientDto(
					cbxBedrijf.getValue(),
					txfVoornaam.getText(),
					txfNaam.getText(),
					txfGebruikernaam.getText()));
			showAlert("Gelukt", "Creeren klant gelukt", null);
			this.mainBorderPane.setOverzichtGebruikers();
		}
		catch (Exception e) {
			showAlert("Niet gelukt", "Creeren klant niet gelukt", e);
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
