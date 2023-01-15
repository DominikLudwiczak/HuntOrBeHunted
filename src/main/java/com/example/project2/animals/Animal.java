package com.example.project2.animals;

import com.example.project2.GameController;
import com.example.project2.map.Drawable;
import com.example.project2.map.Map;
import javafx.scene.control.ListView;

public abstract class Animal extends Drawable {
    /**
     * Animal's health level
     */
    private double health;

    /**
     * Animal's speed value
     */
    private double speed;

    /**
     * Animal's strength value
     */
    private double strength;

    /**
     * name of Animal's species
     */
    private String spec_name;

    /**
     * current Animal's x-coordinate
     */
    private int x;

    /**
     * current Animal's y-coordinate
     */
    private int y;

    /**
     * game's controller
     */
    protected GameController controller;

    /**
     * class constructor
     * @param name  name of Drawable object
     * @param type type of Drawable object
     * @param health Animal's health
     * @param speed Animal's speed
     * @param strength Animal's strength
     * @param spec_name name of Animal's species
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Animal(String name, String type, double health, double speed, double strength, String spec_name, int x, int y, GameController controller) {
        super(name, type);
        this.health = health;
        this.speed = speed;
        this.strength = strength;
        this.spec_name = spec_name;
        this.x = x;
        this.y = y;
        this.controller = controller;
    }

    /**
     * method for starting Animal's thread
     */
    public void start(){}

    /**
     * method for getting Animal's x-coordinate
     * @return x-coordinate
     */
    public synchronized int getX(){
        return this.x;
    }

    /**
     * method for getting Animal's y-coordinate
     * @return y-coordinate
     */
    public synchronized int getY(){
        return this.y;
    }

    /**
     * method for updating Animal's coordinates
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public synchronized void updateCoords(int x, int y) {
        int old_x = this.x;
        int old_y = this.y;
        this.x = x;
        this.y = y;
        Map.getCell(old_x, old_y).removeObject(this);
        this.controller.moveAnimal(this, old_x, old_y);
    }

    /**
     * method for getting Animal's speed
     * @return speed
     */
    public double getSpeed(){
        return this.speed;
    }

    /**
     * method for getting Animal's strength
     * @return strength
     */
    public double getStrength(){
        return this.strength;
    }

    /**
     * method for decreasing Animal's health
     * @param hitValue
     * @return
     */
    public synchronized double decreaseHealth(double hitValue){
        this.health = Math.max(this.health-hitValue, 0);
        return this.health;
    }

    /**
     * method for adding new Animal to Map
     */
    public void addAnimal(Animal animal){
        this.controller.addAnimal(animal);
    }

    @Override
    public ListView<String> present() {
        ListView<String> properties = super.present();
        properties.getItems().add("Health: "+this.health);
        properties.getItems().add("Speed: "+this.speed);
        properties.getItems().add("Strength: "+this.strength);
        properties.getItems().add("Spec. name: "+this.spec_name);
        properties.getItems().add("x: "+this.x);
        properties.getItems().add("y: "+this.y);
        return properties;
    }
}
