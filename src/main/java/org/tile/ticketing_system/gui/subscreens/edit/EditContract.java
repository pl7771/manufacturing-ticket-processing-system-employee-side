package org.tile.ticketing_system.gui.subscreens.edit;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.tile.ticketing_system.domein.Contract;
import org.tile.ticketing_system.domein.ContractType;
import org.tile.ticketing_system.domein.interfaces.IContract;
import org.tile.ticketing_system.gui.general.MainBorderPane;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class EditContract extends GridPane {

    @FXML
    private Button btnContractWijzigen;

    @FXML
    private Button btnAnnuleren;

    @FXML
    private ComboBox<Contract.ContractStatus> cbxStatusContract;

    @FXML
    private Label lblContractId;

    @FXML
    private Label lblStartDatum;

    @FXML
    private Label lblCompany;

    @FXML
    private Label lblVervalt;

    @FXML
    private ComboBox<ContractType.ContractTypeStatus> cbxTypeContract;

    @FXML
    private DatePicker datePickerEindDatumAanpassen;


    private MainBorderPane borderPaneController;

    private IContract contract;

    public EditContract(MainBorderPane borderPaneController){
        FXMLLoader loader = new
                FXMLLoader(getClass().getResource("EditContract.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        this.borderPaneController = borderPaneController;
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public EditContract(MainBorderPane borderPaneController, IContract contract){
        FXMLLoader loader = new
                FXMLLoader(getClass().getResource("EditContract.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        this.borderPaneController = borderPaneController;
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        this.contract = contract;

        lblContractId.setText(String.valueOf(contract.getContractId()));
        lblCompany.setText(contract.getCompany().getName());
        lblStartDatum.setText(contract.getStartDatum().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        lblCompany.setText(contract.getCompany().getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        datePickerEindDatumAanpassen.setValue(contract.getEindDatum().toLocalDate());
        datePickerEindDatumAanpassen.setDisable(true);
        cbxTypeContract.setItems(FXCollections.observableArrayList(ContractType.ContractTypeStatus.values()));
        cbxTypeContract.setValue(contract.getType().getStatus());
        cbxTypeContract.setDisable(true);
        //wss moet er niet zijn zelfs he, hoort enkel bij contracttypes
        cbxStatusContract.setItems(FXCollections.observableArrayList(Contract.ContractStatus.values()));
        cbxStatusContract.setValue(contract.getStatus());
        cbxStatusContract.setDisable(true);
        lblVervalt.setText(contract.vervaltBinnenkort() ? "Vervalt binnenkort" : "Loopt nog een tijdje");
    }



    @FXML
    void btnAnnuleren(ActionEvent event) {
        borderPaneController.removeRightScreen();
    }

    @FXML
    void btnContractWijzigenOnAction(ActionEvent event) {

    }

}

