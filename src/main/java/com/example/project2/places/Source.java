package com.example.project2.places;

import javafx.scene.control.ListView;

public abstract class Source extends Place{
    /**
     * speed of replenishing animal's water or food level
     */
    private double replenish_speed;

    /**
     *
     * @param name  name of Drawable object
     * @param capacity  capacity of Place
     * @param replenish_speed   replenish speed
     */
    public Source(String name, String type, int capacity, double replenish_speed){
        super(name, type, capacity);
        this.replenish_speed = replenish_speed;
    }

    /**
     * method for getting speed of replenishing
     */
    public double getReplenishSpeed(){
        return this.replenish_speed;
    }

    @Override
    public ListView<String> present() {
        ListView<String> properties = super.present();
        properties.getItems().add("Replenish speed: "+this.replenish_speed);
        return properties;
    }
}
