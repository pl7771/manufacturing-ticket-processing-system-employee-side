package org.tile.ticketing_system.gui.subscreens.overview;
import java.io.IOException;

import org.tile.ticketing_system.domein.ContractType;
import org.tile.ticketing_system.domein.controllers.DomainControllerAbstraction;
import org.tile.ticketing_system.domein.interfaces.IContractType;
import org.tile.ticketing_system.gui.general.MainBorderPane;
import org.tile.ticketing_system.gui.subscreens.edit.EditContractType;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ContractTypesOverzicht extends VBox {

    @FXML
    private HBox tableGebruikers;

    @FXML
    private TableView<IContractType> tabelOverzichtContractTypes;

    @FXML
    private TableColumn<IContractType, String> colContractTypeId;

    @FXML
    private TableColumn<IContractType, String> colContractTypeName;


    @FXML
    private TableColumn<IContractType, String> colStatus;

    @FXML
    private TableColumn<IContractType, String> colContracten;


    @FXML
    private Button buttonContractTypeAanmaken;

    private MainBorderPane borderPaneController;
    private DomainControllerAbstraction loggedInUserController;


    public ContractTypesOverzicht(MainBorderPane borderPaneController){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ContractTypesOverzicht.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        this.borderPaneController = borderPaneController;
        this.loggedInUserController = borderPaneController.getControllerForLoggedInUser();
        tabelOverzichtContractTypes.setItems(loggedInUserController.getAllContractTypes());
        colContractTypeId.setCellValueFactory(e -> new SimpleStringProperty(String.valueOf(e.getValue().getContractTypeId())));
        colContractTypeName.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getName()));
        colContracten.setCellValueFactory(e -> new SimpleStringProperty(String.valueOf(loggedInUserController.getActiveContractCountByContractType(e.getValue()))));
        colStatus.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getStatus().toString()));


        tabelOverzichtContractTypes.getSelectionModel().selectedItemProperty().addListener((obsV, oldV, newV) -> {
        	try{
            TablePosition pos = tabelOverzichtContractTypes.getSelectionModel().getSelectedCells().get(0);
            int row = pos.getRow();
            IContractType contractType = tabelOverzichtContractTypes.getItems().get(row);
            borderPaneController.setRightScreen(new EditContractType(borderPaneController, contractType));
        	} catch (IndexOutOfBoundsException iobe) {
				//er hoeft niets te gebeuren, na het filteren is er geen cel meer geselecteerd
			}
        });

    }

    @FXML
    void buttonContractTypeAanmakenOnAction(ActionEvent event) {
        borderPaneController.setRightScreen(new EditContractType(borderPaneController));
    }

}

