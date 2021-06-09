package org.tile.ticketing_system.gui.subscreens.edit;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.tile.ticketing_system.domein.ContractType;
import org.tile.ticketing_system.domein.dto.CreateContractTypeDto;
import org.tile.ticketing_system.domein.dto.EditContractTypeDto;
import org.tile.ticketing_system.domein.interfaces.IContractType;
import org.tile.ticketing_system.domein.controllers.DomainControllerAbstraction;
import org.tile.ticketing_system.gui.general.MainBorderPane;

import java.io.IOException;

public class EditContractType extends GridPane {

    @FXML
    private Button btnContractTypeWijzigen;

    @FXML
    private Button btnAnnuleren;
    
    @FXML
    private Button btnClosePane;

    @FXML
    private Label lblContractTypeId;

    @FXML
    private Label lblAantalContractTypes;
    
    @FXML
    private Label lblBehandeldeTickets;
    
    @FXML
    private Label lblPercentageTickets;

    @FXML
    private TextField txfContractTypeName;

    @FXML
    private TextField txfMaxAfhaandeldTijd;

    @FXML
    private TextField txfMinDoorloopTijd;

    @FXML
    private TextField txfPrijs;

    @FXML
    private ComboBox<ContractType.GedekteTijdstippen> cbxGedekteTijdStippen;

    @FXML
    private ComboBox<ContractType.ManierAanmakenTicket> cbxManieAanmakenTicket;

    @FXML
    private ComboBox<ContractType.ContractTypeStatus> cbxContractTypeStatus;

    @FXML
    private Label lblStartDatum1;

    private final MainBorderPane borderPaneController;
    private final DomainControllerAbstraction loggedInUserController;
    private IContractType contractType;

    public EditContractType(MainBorderPane borderPaneController){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditContractType.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        this.borderPaneController = borderPaneController;
        this.loggedInUserController = borderPaneController.getControllerForLoggedInUser();
        setComboBoxesForGedekteTijdStippen_ManierAanmakenTicket_Status();
        cbxContractTypeStatus.setValue(ContractType.ContractTypeStatus.NIET_ACTIEF);
        cbxContractTypeStatus.setDisable(true);


        setListenersForDoubleFields();


    }



    public EditContractType(MainBorderPane borderPaneController, IContractType contractType) {
        this(borderPaneController);
        this.contractType = contractType;
        int aantalBehandeldeTickets = loggedInUserController.getCompletedTicketsCountPerContractType(contractType);
        String stringValueBehandeldeTickets = String.valueOf(aantalBehandeldeTickets) == null ? "0" : String.valueOf(aantalBehandeldeTickets);

        lblContractTypeId.setText(String.valueOf(contractType.getContractTypeId()));
        lblAantalContractTypes.setText(String.valueOf(loggedInUserController.getActiveContractCountByContractType(contractType)));
        lblBehandeldeTickets.setText(stringValueBehandeldeTickets);
        lblPercentageTickets.setText(Integer.toString(loggedInUserController.getPercentageTicketsCompletedInTimePerContractType(contractType)) + "%");
            txfContractTypeName.setText(contractType.getName());
            txfMaxAfhaandeldTijd.setText(String.valueOf((int)contractType.getMaximaleAfhandeltijd()));
            txfMinDoorloopTijd.setText(String.valueOf((int)contractType.getMinimaleDoorlooptijd()));
            txfPrijs.setText(String.valueOf(contractType.getPrijs()));
            setComboBoxesForGedekteTijdStippen_ManierAanmakenTicket_Status();
            cbxGedekteTijdStippen.setValue(contractType.getGedekteTijdstippen());
            cbxManieAanmakenTicket.setValue(contractType.getManierAanmakenTicket());
            cbxContractTypeStatus.setValue(contractType.getStatus());
        cbxContractTypeStatus.setDisable(false);

        setListenersForDoubleFields();
    }

    @FXML
    void btnAnnuleren(ActionEvent event) {
        borderPaneController.removeRightScreen();
    }

    @FXML
    void btnContractTypeWijzigenOnAction(ActionEvent event) {
        if (contractType == null) {
            createContractType();
        } else {
            editContractType(contractType);
        }
    }

    private void createContractType() {
        try {
            this.loggedInUserController.createContractType(new CreateContractTypeDto(
                    txfContractTypeName.getText(),
                    Double.parseDouble(txfMaxAfhaandeldTijd.getText().isEmpty() ? "-1" : txfMaxAfhaandeldTijd.getText()),
                    Double.parseDouble(txfMinDoorloopTijd.getText().isEmpty() ? "-1" : txfMinDoorloopTijd.getText()),
                    Double.parseDouble(txfPrijs.getText().isEmpty() ? "-1" : txfPrijs.getText()),
                    cbxManieAanmakenTicket.getSelectionModel().getSelectedItem(),
                    cbxGedekteTijdStippen.getSelectionModel().getSelectedItem(),
                    cbxContractTypeStatus.getSelectionModel().getSelectedItem()));
            borderPaneController.setOverzichtContractTypes();
            showAlert("Gelukt", "Creeren contract type gelukt", null);
        } catch (Exception e) {
            showAlert("Niet gelukt", "Fout creeren contact type", e);
           // this.borderPaneController.setRightScreen(new EditContractType(borderPaneController));
        }
    }

    private void editContractType(IContractType contractType2) {
        try {
            this.loggedInUserController.editContractType(new EditContractTypeDto(
                    contractType2.getContractTypeId(),
                    txfContractTypeName.getText(),
                    Double.parseDouble(txfMaxAfhaandeldTijd.getText()),
                    Double.parseDouble(txfMinDoorloopTijd.getText()),
                    Double.parseDouble(txfPrijs.getText()),
                    cbxManieAanmakenTicket.getSelectionModel().getSelectedItem(),
                    cbxGedekteTijdStippen.getSelectionModel().getSelectedItem(),
                    cbxContractTypeStatus.getSelectionModel().getSelectedItem()));
            borderPaneController.setOverzichtContractTypes();
            showAlert("Gelukt", "Eediteren contract type gelukt", null);
        } catch (Exception e) {
            showAlert("Niet gelukt", "Fout editeren contract type", e);
           // this.borderPaneController.setRightScreen(new EditContractType(borderPaneController, contractType));
        }
    }

    private void setComboBoxesForGedekteTijdStippen_ManierAanmakenTicket_Status() {
        cbxGedekteTijdStippen.setItems(FXCollections.observableArrayList(ContractType.GedekteTijdstippen.values()));
        cbxManieAanmakenTicket.setItems(FXCollections.observableArrayList(ContractType.ManierAanmakenTicket.values()));
        cbxContractTypeStatus.setItems(FXCollections.observableArrayList(ContractType.ContractTypeStatus.values()));
    }

    private void showAlert(String s1, String s2, Exception e){
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

    private void setListenersForDoubleFields() {
        txfPrijs.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^[0-9]\\d+(\\[.]{0,1}\\d*)?$")) {
                    txfPrijs.setText(newValue.replaceAll("[^\\d.$]", ""));
                }
            }
        });

        txfMaxAfhaandeldTijd.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txfMaxAfhaandeldTijd.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        txfMinDoorloopTijd.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txfMinDoorloopTijd.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

}


