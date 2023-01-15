package com.example.project2.places;

import com.example.project2.animals.Prey;
import com.example.project2.map.Drawable;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Place extends Drawable {
    /**
     * maximum amount of preys that can be in Place
     */
    private int capacity;

    /**
     * collection of Preys that are in Place
     */
    private Collection<Prey> preys;

    /**
     * class constructor
     * @param name  name of Drawable object
     * @param capacity  capacity of Place
     */
    public Place(String name, String type, int capacity){
        super(name, type);
        this.capacity = capacity;
        this.preys = new ArrayList<>();
    }

    /**
     * method for getting capacity of place
     * @return capacity
     */
    public synchronized int getCapacity(){
        return this.capacity;
    }

    /**
     * mathod for adding Prey into Place
     * @param prey Prey
     */
    public synchronized void addAnimal(Prey prey){
        this.preys.add(prey);
    }

    /**
     * mathod for removing Prey from Place
     * @param prey Prey
     */
    public synchronized void removeAnimal(Prey prey){
        if(this.preys.contains(prey))
            this.preys.remove(prey);
    }

    /**
     * method for getting all Preys inside Place
     * @return preys
     */
    public synchronized Collection<Prey> getPreys(){
        return this.preys;
    }

    @Override
    public ListView<String> present() {
        ListView<String> properties = super.present();
        properties.getItems().add("Capacity: "+this.capacity);
        properties.getItems().add("Number of Preys inside: "+this.getPreys().size());
        return properties;
    }
}
