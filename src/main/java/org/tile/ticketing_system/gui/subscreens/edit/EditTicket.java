package org.tile.ticketing_system.gui.subscreens.edit;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.tile.ticketing_system.domein.Bijlage;
import org.tile.ticketing_system.domein.Ticket;
import org.tile.ticketing_system.domein.dto.CreateTicketDto;
import org.tile.ticketing_system.domein.dto.EditTicketDto;
import org.tile.ticketing_system.domein.interfaces.ITicket;
import org.tile.ticketing_system.domein.controllers.DomainControllerAbstraction;
import org.tile.ticketing_system.gui.general.MainBorderPane;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import lombok.extern.java.Log;

@Log
public class EditTicket extends GridPane {

    @FXML
    private TextField txfTitel;

    @FXML
    private TextArea txaOmschrijving;

    @FXML
    private TextArea txaOpmerking;

    @FXML
    private TextArea txaBijlageDescription;

    @FXML
    private Button btnAanmakenTicket;

    @FXML
    private Button btnAnnuleren;

    @FXML
    private ComboBox<Ticket.Status> cbxStatus;

    @FXML
    private Label lblTicketID;

    @FXML
    private Label lblIsGewijzigd;

    @FXML
    private Label lblDatumAangemaakt;

    @FXML
    private Label lblDatumAfgesloten;

    @FXML
    private Label lblDatumGewijzigd;

    @FXML
    private Label lblAantalWijzigingen;

    @FXML
    private Label lblCompany;

    @FXML
    private Label lblContract;

    @FXML
    private Button btnUpload;

    @FXML
    private Label lbluploadedFileName;

    @FXML
    private Button btnDownload;

    @FXML
    private Label lblChat;

    private ComboBox<String> cbxCreateTicketCompanies;

    public final MainBorderPane borderPaneController;
    private final DomainControllerAbstraction loggedInUserController;
    private ITicket ticket;
    Bijlage nieuweBijlage;

    public EditTicket(MainBorderPane borderPaneController) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditTicket.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        this.borderPaneController = borderPaneController;
        this.loggedInUserController = borderPaneController.getControllerForLoggedInUser();
        replaceCompanyNamesCbx();
        this.add(cbxCreateTicketCompanies, 2, 6);
        cbxCreateTicketCompanies.setStyle("-fx-background-color: #FFF;-fx-border-color:  #c6d200;\n" +
                "    -fx-border-width: 1px;\n" +
                "    -fx-border-radius: 5px;");
        cbxStatus.setDisable(true);
        cbxStatus.setItems(FXCollections.observableArrayList(Ticket.Status.values()));
        cbxStatus.setValue(Ticket.Status.AANGEMAAKT);
        this.btnAanmakenTicket.setText("Ticket aanmaken");
        this.btnAanmakenTicket.setDisable(false);
        // empty edit screen
        btnDownload.setDisable(true);

    }

    public EditTicket(MainBorderPane borderPaneController, ITicket ticket) {
        this(borderPaneController);
        this.ticket = ticket;

        txfTitel.setText(ticket.getTitel());
        txaOmschrijving.setText(ticket.getOmschrijving());
        txaOpmerking.setText(null);
        txaBijlageDescription.setText(ticket.getImageDescription());
        cbxStatus.setDisable(false);
        cbxStatus.setItems(FXCollections.observableArrayList(Ticket.Status.values()));
        cbxStatus.setValue(ticket.getStatus());
        lblTicketID.setText(String.valueOf(ticket.getTicketId()));
        lblIsGewijzigd.setText(String.valueOf(ticket.isGewijzigd()));
        lblChat.setText(ticket.getOpmerkingen() == null ? "" : ticket.getOpmerkingen());
        this.btnAanmakenTicket.setText("Wijzigingen opslaan");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(ticket.getStatus() == Ticket.Status.AFGEHANDELD){
            btnAanmakenTicket.setDisable(true);
        }
        if(ticket.getBijlage() == null){
            btnDownload.setDisable(true);
        }
        else{
            btnDownload.setDisable(false);
        }
        lblDatumAangemaakt
                .setText(ticket.getDatumAangemaakt() == null ? "-" : ticket.getDatumAangemaakt().format(formatter));
        lblDatumAfgesloten
                .setText(ticket.getDatumAfgesloten() == null ? "OPEN" : ticket.getDatumAfgesloten().format(formatter));
        lblDatumGewijzigd
                .setText(ticket.getDatumGewijzigd() == null ? "-" : ticket.getDatumGewijzigd().format(formatter));

        lblAantalWijzigingen.setText(String.valueOf(ticket.getAantalWijzigingen()));
        this.getChildren().remove(cbxCreateTicketCompanies);
        this.add(lblCompany, 2, 6);
        lblCompany.setText(ticket.getCompany() == null ? "Geen bedrijf toegewezen" : ticket.getCompany().getName());
        lblContract.setText(
                ticket.getContract() == null ? "Geen contract toegewezen" : ticket.getContract().getType().getName());
        lbluploadedFileName
                .setText(ticket.getBijlage() == null ? "Geen Bijlage" : ticket.getBijlage().getName());

        if(ticket.getStatus().equals(Ticket.Status.AFGEHANDELD)){
            txfTitel.setDisable(true);
            txaOmschrijving.setDisable(true);
            txaOpmerking.setDisable(true);
            cbxStatus.setDisable(true);
            txaBijlageDescription.setDisable(true);
            btnUpload.setDisable(true);
            btnDownload.setDisable(true);
        }
    }

    @FXML
    void annulerenTicketWijzigen(MouseEvent event) {
        borderPaneController.removeRightScreen();
    }

    @FXML
    void saveChangedTicket(MouseEvent event) {
        if (ticket == null) {
            createTicket();
        } else {
            editTicket();
        }
    }

    private void createTicket() {
        try {
            this.loggedInUserController.createTicket(new CreateTicketDto(
                    txfTitel.getText(),
                    txaOmschrijving.getText(),
                    txaOpmerking.getText(),
                    Ticket.Status.AANGEMAAKT,
                    cbxCreateTicketCompanies.getSelectionModel().getSelectedItem(),
                    null, //TODO: Contract
                    nieuweBijlage,
                    txaBijlageDescription.getText()));
            borderPaneController.setOverzichtTickets();
            showAlert("Gelukt", "Creeren ticket gelukt", null);
        } catch (Exception e) {
            showAlert("Niet gelukt", "Creeren ticket niet gelukt", e);
            // this.borderPaneController.setRightScreen(this);
        }
    }

    private void editTicket() {
        try {
            this.loggedInUserController.editTicket(new EditTicketDto(
                    ticket.getTicketId(),
                    txfTitel.getText(),
                    txaOmschrijving.getText(),

                    String.format(fillChat(txaOpmerking.getText())),

                    cbxStatus.getSelectionModel().getSelectedItem(),
                    txaBijlageDescription.getText(),
                    nieuweBijlage));
            borderPaneController.setOverzichtTickets();
            showAlert("Gelukt", "Editeren ticket gelukt", null);
        } catch (Exception e) {
            showAlert("Niet gelukt", "Fout editeren ticket", e);
            // this.borderPaneController.setRightScreen(this);
        }
    }

    private String fillChat(String message){
        return message == null ? lblChat.getText() +
                System.lineSeparator()+"[" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " " +
                        borderPaneController.getControllerForLoggedInUser().getLoggedInUser().getFirstName() + " " +
                            borderPaneController.getControllerForLoggedInUser().getLoggedInUser().getLastName() +"]: " + "Ticket gewijzigd zonder extra commentaar" : lblChat.getText() +
                System.lineSeparator()+"[" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " " +
                        borderPaneController.getControllerForLoggedInUser().getLoggedInUser().getFirstName() + " " +
                            borderPaneController.getControllerForLoggedInUser().getLoggedInUser().getLastName() +"]: " + message;
    }


    private void replaceCompanyNamesCbx() {
        this.getChildren().remove(lblCompany);
        cbxCreateTicketCompanies = new ComboBox<>();
        cbxCreateTicketCompanies.setItems(FXCollections
                .observableArrayList(borderPaneController.getControllerForLoggedInUser().getAllCompanyNames()));
        cbxCreateTicketCompanies.setMaxWidth(Double.MAX_VALUE);
    }

    @FXML
    void buttonOnAction(ActionEvent event) {

    }

    @FXML
    void uploadFile(MouseEvent event) {
		nieuweBijlage = new Bijlage();
		var fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
			lbluploadedFileName.setText(selectedFile.getName());

			try {
				byte[] selectedFileContents = FileUtils.readFileToByteArray(selectedFile);
				nieuweBijlage.setDataFiles(selectedFileContents);
				nieuweBijlage.setName(selectedFile.getName());
				//ticket.setBijlage(nieuweBijlage);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException ne) {
				log.info("geen upload-bestand gekozen");
			}
			 finally {
			//	log.info(ticket.getBijlage().toString());
			}
		}
    }

    @FXML
    void downloadFile(MouseEvent event) {
        if (ticket.getBijlage() != null) {
            var directoryChooser = new DirectoryChooser();
            var selectedDirectory = directoryChooser.showDialog(null);
            try {
                FileUtils.writeByteArrayToFile(new File(selectedDirectory + "//" + ticket.getBijlage().getName()), ticket.getBijlage().getDataFiles());
                showAlert("Download bijlage", "Bijlage opgeslagen op: " + selectedDirectory.getAbsolutePath(), null);
            } catch (IOException e) {
                showAlert("Fout", "Je hebt niet de nodige toegangsrechten om bestanden op te slaan in deze directory", e);
            } catch (NullPointerException ne) {
                log.info("geen download-directory gekozen");
            }
        } else {
            showAlert("Download bijlage", "Geen bijlage beschikbaar", null);
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
