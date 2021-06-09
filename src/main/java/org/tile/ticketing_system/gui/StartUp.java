package org.tile.ticketing_system.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.tile.ticketing_system.domein.controllers.LoginController;
import org.tile.ticketing_system.gui.general.LoginScreen;

public class StartUp extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			var loginController = new LoginController();
			var root = new LoginScreen(loginController);

			var scene = new Scene(root);
			scene.getStylesheets().add("org/tile/ticketing_system/gui/style.css");
			primaryStage.setScene(scene);
			primaryStage.setTitle("Actemium");
			primaryStage.getIcons().add(new Image("/images/actemium-logo-empotybg.png"));
			primaryStage.centerOnScreen();
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
