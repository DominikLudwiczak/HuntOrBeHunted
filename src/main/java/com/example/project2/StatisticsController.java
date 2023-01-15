package com.example.project2;

import com.example.project2.map.Cell;
import com.example.project2.map.Drawable;
import com.example.project2.map.Map;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable {
    @FXML
    private AnchorPane properties;
    @FXML
    private Label topLabel;
    @FXML
    private Accordion accordion;
    public StatisticsController(){
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        double screenWidth = Screen.getPrimary().getBounds().getWidth()-20;
        properties.setMinWidth((screenWidth-screenHeight)/2);
        this.topLabel.setText("Click on the object to view its properties");
    }

    /**
     * method for printing properties of given object in Accordion
     * @param x coordinate x on the map
     * @param y coordinate y on the map
     */
    public void viewProperties(int x, int y){
        Cell cell = Map.getCell(x, y);
        topLabel.setText("Properties of cell with coordinates: ("+cell.getX()+", "+cell.getY()+")");
        this.accordion.getPanes().clear();
        Iterator it = cell.getObjects().iterator();
        while(it.hasNext()){
            Drawable obj = (Drawable) it.next();
//            DrawableSubscriber subscriber = new DrawableSubscriber();
//            obj.subscribe(subscriber);
            TitledPane pane = new TitledPane();
            pane.setText(obj.getName());
            ListView<String> properties = obj.present();
            pane.getChildrenUnmodifiable().clear();
            pane.setContent(properties);
            this.accordion.getPanes().add(pane);
        }
    }
}
