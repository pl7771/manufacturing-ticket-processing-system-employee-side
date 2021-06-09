package org.tile.ticketing_system.gui.subscreens.overview;

import javafx.beans.property.SimpleIntegerProperty;
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
import org.tile.ticketing_system.domein.Contract;
import org.tile.ticketing_system.domein.controllers.DomainControllerAbstraction;
import org.tile.ticketing_system.domein.interfaces.IContract;
import org.tile.ticketing_system.gui.subscreens.edit.EditContract;
import org.tile.ticketing_system.gui.general.MainBorderPane;

import java.io.IOException;
import java.time.format.DateTimeFormatter;


public class ContractOverzicht extends VBox {
        @FXML
        private HBox tableGebruikers;
        @FXML
        private TableView<IContract> tabelOverzichtContracten;
        @FXML
        private TableColumn<IContract, Integer> colContractId;
        @FXML
        private TableColumn<IContract, String> colStartDatum;
        @FXML
        private TableColumn<IContract, String> colEindDatum;
        @FXML
        private TableColumn<IContract, String> colType;
        @FXML
        private TableColumn<IContract, String> colStatus;
        @FXML
        private TableColumn<IContract, String> colVervalt;
        @FXML
        private TableColumn<IContract, String> colBedrijf;
        @FXML
        private Button buttonKlantAanmaken;

        private MainBorderPane borderPaneController;
        private DomainControllerAbstraction dc;

        public ContractOverzicht(MainBorderPane borderPaneController){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ContractOverzicht.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            this.borderPaneController = borderPaneController;
            this.dc = borderPaneController.getControllerForLoggedInUser();

            try {
                loader.load();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            tabelOverzichtContracten.setItems(dc.getAllContracts());
            colContractId.setCellValueFactory(e -> new SimpleIntegerProperty(e.getValue().getContractId()).asObject());
            colStartDatum.setCellValueFactory(e -> new SimpleStringProperty(
                    e.getValue().getStartDatum() == null ? " " : e.getValue().getEindDatum().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            );
            colEindDatum.setCellValueFactory(e -> new SimpleStringProperty(
                    e.getValue().getEindDatum() == null ? " " : e.getValue().getEindDatum().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            );
            colType.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getType().getName()));
            colStatus.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getStatus().toString()));
            colVervalt.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().vervaltBinnenkort() ? "Vervalt binnenkort" : "Loopt nog een tijdje"));
            colBedrijf.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getCompany().getName()));


            tabelOverzichtContracten.getSelectionModel().selectedItemProperty().addListener((obsV, oldV, newV) -> {
            	try {
                TablePosition pos = tabelOverzichtContracten.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                IContract contract = tabelOverzichtContracten.getItems().get(row);
                //set splitscreen
                borderPaneController.setRightScreen(new EditContract(borderPaneController, contract));
            	} catch (IndexOutOfBoundsException iobe) {
    				//er hoeft niets te gebeuren, na het filteren is er geen cel meer geselecteerd
    			}
            });
        }

        @FXML
        void buttonContractAanmakenOnAction(ActionEvent event) {
            borderPaneController.setRightScreen(new EditContract(borderPaneController));
        }

    }

