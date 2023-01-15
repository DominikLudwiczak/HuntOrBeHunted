package com.example.project2.map;

import com.example.project2.animals.Prey;
import com.example.project2.places.Place;

import java.util.Collection;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

public class NormalCell extends Cell{
    /**
     * Drawable objects which are placed on given Cell
     */
    private Collection<Drawable> objects;

    public NormalCell(int x, int y){
        super(x,y);
        this.objects = new LinkedList<>();
    }

    /**
     * overrided method for adding Drawable object into Cell
     * @param obj   Drawable object
     */
    @Override
    public void addObject(Drawable obj) throws InterruptedException {
        Boolean stop = false;
        Place place = null;
        if(obj instanceof Prey){
            place = this.getObjects().stream().filter(Place.class::isInstance).map(Place.class::cast).findFirst().orElse(null);
            Prey prey = (Prey) obj;
            while(!this.checkAdd(place, prey) && !prey.isStopped()){
                sleep((long) (1100-prey.getSpeed()));
            }
            stop = prey.isStopped();
        }
        if(!stop)
            this.putObject(obj);
    }

    private synchronized void putObject(Drawable obj){
        this.objects.add(obj);
    }

    private synchronized Boolean checkAdd(Place place, Prey prey){
        if(place != null) {
            if (place.getPreys().size() >= place.getCapacity())
                return false;
            else if(!prey.isStopped())
                place.addAnimal(prey);
        }
        return true;
    }

    /**
     * overrided method for removing Drawable object from Cell
     * @param obj   Drawable object
     */
    @Override
    public synchronized void removeObject(Drawable obj){
        this.objects.remove(obj);
        notifyAll();
    }

    /**
     * overrided method for getting objects placed on given cell
     * @return collection of Drawable objects
     */
    @Override
    public synchronized Collection<Drawable> getObjects() {
        return this.objects;
    }
}
