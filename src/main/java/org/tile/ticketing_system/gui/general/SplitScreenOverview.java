package org.tile.ticketing_system.gui.general;


import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;

public class SplitScreenOverview extends SplitPane {

    public SplitScreenOverview(Pane leftScreen){

        FXMLLoader loader = new
                FXMLLoader(getClass().getResource("SplitScreenOverview.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        this.getItems().add(0, leftScreen);
        this.getItems().add(1, new Pane());
    }

}

