package com.example.project2.map;

import com.example.project2.animals.Animal;
import com.example.project2.animals.Prey;

import java.util.Collection;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

public class Junction extends Cell{
    /**
     * Drawable object which is placed on given Junction
     */
    private Drawable object;

    public Junction(int x, int y){
        super(x,y);
        this.object = null;
    }

    /**
     * overrided method for adding Drawable object into Junction
     * @param obj   Drawable object
     */
    @Override
    public void addObject(Drawable obj) throws InterruptedException {
        Boolean stop = false;
        Animal animal = (Animal) obj;
        while(!this.checkAdd())
            sleep((long) (1100-animal.getSpeed()));

        if(obj instanceof Prey)
            stop = ((Prey) obj).isStopped();

        if(!stop)
            this.putObject(obj);
    }

    private synchronized void putObject(Drawable obj){
        this.object = obj;
    }

    private synchronized Boolean checkAdd() {
        if(this.object != null)
            return false;
        return true;
    }

    /**
     * overrided method for removing Drawable object from Junction
     * @param obj   Drawable object
     */
    @Override
    public synchronized void removeObject(Drawable obj){
        this.object = null;
        notifyAll();
    }

    /**
     * overrided method for getting objects placed on given cell
     * @return collection of Drawable objects
     */
    @Override
    public synchronized Collection<Drawable> getObjects() {
        var list = new LinkedList<Drawable>();
        if(this.object != null)
            list.add(this.object);
        return list;
    }
}
