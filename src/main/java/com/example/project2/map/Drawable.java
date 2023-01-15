package com.example.project2.map;

import javafx.scene.control.ListView;

public abstract class Drawable {
    /**
     * name of Drawable object
     */
    private String name;
    private String type;

    /**
     * class constructor
     * @param name  name of Drawable object
     * @param type  type of Drawable object
     */
    public Drawable(String name, String type){
        this.name = name;
        this.type = type;
    }

    /**
     * method for getting Drawable's object type
     * @return  type of object
     */
    public String getType(){
        return this.type;
    }

    /**
     * method for getting Drawable's object name
     * @return  name of object
     */
    public String getName(){
        return this.name;
    }

    /**
     * method for presenting object's properties
     * @return
     */
    public ListView<String> present(){
        ListView<String> properties = new ListView<>();
        properties.getItems().add("Type: "+this.type);
        properties.getItems().add("Name: "+this.name);
        return properties;
    }
}
