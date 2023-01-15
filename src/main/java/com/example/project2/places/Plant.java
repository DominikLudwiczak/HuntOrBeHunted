package com.example.project2.places;

public class Plant extends Source{
    /**
     * class constructor
     * @param name  name of Drawable object
     * @param capacity  capacity of Place
     * @param replenish_speed   speed of replenish animal's food level
     */
    public Plant(String name, int capacity, double replenish_speed){
        super(name, "Plant", capacity, replenish_speed);
    }
}
