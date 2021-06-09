package org.tile.ticketing_system.gui.general;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import org.tile.ticketing_system.domein.Ticket;
import org.tile.ticketing_system.domein.User;
import org.tile.ticketing_system.domein.controllers.DomainControllerAbstraction;
import org.tile.ticketing_system.domein.controllers.LoginController;
import org.tile.ticketing_system.gui.subscreens.overview.ContractOverzicht;
import org.tile.ticketing_system.gui.subscreens.overview.ContractTypesOverzicht;
import org.tile.ticketing_system.gui.subscreens.overview.GebruikerOverzicht;
import org.tile.ticketing_system.gui.subscreens.overview.KPIs;
import org.tile.ticketing_system.gui.subscreens.overview.Statistics;
import org.tile.ticketing_system.gui.subscreens.overview.TicketOverzicht;

public class MainBorderPane extends BorderPane {
	@FXML
	private HBox hBoxTop;
    @FXML
    private ImageView imgLogo;
    @FXML
    private Button btnOverzichtTickets;
    @FXML
    private Button btnStatistieken;
    @FXML
    private Button btnKPI;
    @FXML
    private Button btnKnowledgebase;
    @FXML
    private Button btnOverzichtGebruikers;
    @FXML
    private Label lblFooter;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnOverzichtContracten;
    @FXML
    private Button btnOverzichtContractTypes;
    @FXML
    private Button btnDashBoard;

    private final DomainControllerAbstraction controllerForLoggedInUser;

    public MainBorderPane(DomainControllerAbstraction controllerForLoggedInUser) {
        this.controllerForLoggedInUser = controllerForLoggedInUser;

        FXMLLoader loader = new
                FXMLLoader(getClass().getResource("MainBorderPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        this.setCenter(new Dashboard(this));
        tekenButtons();
    }

    private void tekenButtons() {
        User loggedInUser = this.controllerForLoggedInUser.getLoggedInUser();
        if (loggedInUser.isSupportManager()) {
            hBoxTop.getChildren().remove(btnOverzichtGebruikers);
        }
        if(loggedInUser.isTechnician()){
            hBoxTop.getChildren().remove(btnOverzichtGebruikers);
            hBoxTop.getChildren().remove(btnStatistieken);
            hBoxTop.getChildren().remove(btnOverzichtContracten);
            hBoxTop.getChildren().remove(btnOverzichtContractTypes);
            hBoxTop.getChildren().remove(btnKPI);
            
        }
        if(loggedInUser.isAdministrator()) {
            hBoxTop.getChildren().remove(btnOverzichtTickets);
            hBoxTop.getChildren().remove(btnStatistieken);
            hBoxTop.getChildren().remove(btnOverzichtContracten);
            hBoxTop.getChildren().remove(btnOverzichtContractTypes);
            hBoxTop.getChildren().remove(btnKPI);
        }
    }
    
    public DomainControllerAbstraction getControllerForLoggedInUser() {
    	return this.controllerForLoggedInUser;
    }

    @FXML
    void btnDashBoardOnAction(ActionEvent event) {
        this.setCenter(new Dashboard(this));
    }

    @FXML
    public void btnOverzichtTicketsOnAction(ActionEvent event) {
        this.setOverzichtTickets("OPENSTAAND");
    }

    @FXML
    public void btnStatistiekenOnAction(ActionEvent event) {
        this.setCenter(new Statistics(this));
    }
    
    @FXML
    public void btnKPIOnAction(ActionEvent event) {
        this.setCenter(new KPIs(this));
    }

    @FXML
    void btnKnowledgeBaseOnAction(ActionEvent event) {

    }

    @FXML
    void btnOverzichtGebruikersOnAction(ActionEvent event) {
    	this.setOverzichtGebruikers();
    }

    @FXML
    public void btnOverzichtContractenOnAction(ActionEvent event) {
        this.setOverzichtContracten();
    }

    @FXML
    void btnOverzichtContractTypesOnAction(ActionEvent event) {
        this.setOverzichtContractTypes();
    }

    @FXML
    void btnLogout(ActionEvent event) {
        Scene sc = new Scene(new LoginScreen(new LoginController()));
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setScene(sc);
        stage.centerOnScreen();
        stage.show();
    }

    public void setRightScreen(Node rightScreen) {
    	ObservableList<Node> splitScreens = ((SplitScreenOverview) this.getCenter()).getItems();
    	if(splitScreens.size() == 2) splitScreens.remove(1);
    	splitScreens.add(1, rightScreen);
    }
    
    public void removeRightScreen() {
    	ObservableList<Node> splitScreens = ((SplitScreenOverview) this.getCenter()).getItems();
    	splitScreens.remove(1);
    }
    
    public void setOverzichtGebruikers() {
    	this.setCenter(new SplitScreenOverview(new GebruikerOverzicht(this)));
    }
    
    public void setOverzichtGebruikers(String role) {
    	GebruikerOverzicht overzicht = new GebruikerOverzicht(this);
    	overzicht.filter(role);
    	this.setCenter(new SplitScreenOverview(overzicht));
    }
    
    public void setOverzichtTickets() {
    	this.setCenter(new SplitScreenOverview(new TicketOverzicht(this)));
    }
    
    public void setOverzichtTickets(Ticket.Status status) {
    	TicketOverzicht overzicht = new TicketOverzicht(this);
    	overzicht.setStatusFilter(status);
    	this.setCenter(new SplitScreenOverview(overzicht));
    }
    
    public void setOverzichtTickets(String status) {
    	TicketOverzicht overzicht = new TicketOverzicht(this);
    	overzicht.setStatusFilter(status);
    	this.setCenter(new SplitScreenOverview(overzicht));
    }

    public void setOverzichtContracten() {
        this.setCenter(new SplitScreenOverview(new ContractOverzicht(this)));
    }

    public void setOverzichtContractTypes() {
        this.setCenter(new SplitScreenOverview(new ContractTypesOverzicht(this)));
    }

}
