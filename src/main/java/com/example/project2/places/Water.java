package com.example.project2.places;

public class Water extends Source{
    /**
     * class constructor
     * @param name  name of Drawable object
     * @param capacity  capacity of Place
     * @param replenish_speed   speed of replenishing animal's water level
     */
    public Water(String name, int capacity, double replenish_speed){
        super(name, "Water", capacity, replenish_speed);
    }
}
