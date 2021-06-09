package org.tile.ticketing_system.gui.general;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.event.ActionEvent;

import org.tile.ticketing_system.domein.controllers.DomainControllerAbstraction;
import org.tile.ticketing_system.domein.controllers.LoginController;

public class LoginScreen extends GridPane {

	private final LoginController loginController;

	@FXML
	private Label lblTitel;
	@FXML
	private Button btnAanmelden;
	@FXML
	private TextField tfEmail;
	@FXML
	private PasswordField pfWachtwoord;

	public LoginScreen(LoginController loginController) {
		this.loginController = loginController;
		FXMLLoader loader = new
				FXMLLoader(getClass().getResource("LoginScreen.fxml"));
				loader.setRoot(this);
				loader.setController(this);
				try
				{
				loader.load();
				}
				catch (IOException ex)
				{
				throw new RuntimeException(ex);
				}
				
				//administrator
//				this.tfEmail.setText("administrator@gmail.com");
//				pfWachtwoord.setText("administrator");
				//Technieker
//				this.tfEmail.setText("technician@gmail.com");
//				pfWachtwoord.setText("technician");
//				//supportManager
				this.tfEmail.setText("supportmanager@gmail.com");
				pfWachtwoord.setText("supportmanager");
		
	}

	// Event Listener on Button[#btnAanmelden].onAction
	@FXML
	public void btnAanmeldenOnAction(ActionEvent event) {
				try {
					tfEmail.requestFocus();
					DomainControllerAbstraction controllerForLoggedInUser = this.loginController.login(this.tfEmail.getText(), this.pfWachtwoord.getText());
					//((Stage) this.getScene().getWindow()).;
					//Stage stage = (Stage) this.getScene().getWindow();
					//Stage stage = new Stage();
					Scene sc = new Scene(new MainBorderPane(controllerForLoggedInUser));
					Stage stage = (Stage) this.getScene().getWindow();
					stage.setScene(sc);
					//stage.setTitle(String.format(""));
					stage.centerOnScreen();
					stage.setMaximized(true);
					stage.show();
				} catch (IllegalArgumentException error) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);

					DialogPane dialogPane = alert.getDialogPane();
					dialogPane.getStylesheets().add(
							getClass().getResource("/org/tile/ticketing_system/gui/style.css").toExternalForm());
					dialogPane.getStyleClass().add("dialog-pane");
					alert.setGraphic(new ImageView(new Image("/images/notcorrect.png")));
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("/images/actemium-logo-empotybg.png"));

					alert.setTitle("Login mislukt");
					alert.setHeaderText("Inloggen mislukt");
					alert.setContentText(error.getMessage());
					alert.showAndWait();

				}
	}
}
