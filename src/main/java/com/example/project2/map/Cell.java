package com.example.project2.map;

import java.util.Collection;
import java.util.LinkedList;

public abstract class Cell {
    /**
     * X coordinate
     */
    private int x;

    /**
     * Y coordinate
     */
    private int y;

    /**
     * mark if this call belongs to path
     */
    private boolean isPath;

    /**
     * mark if this call is Junction
     */
    private boolean isJunction;

    /**
     * class constructor, initialize also empty list of objects placed on Cell
     * @param x X coordinate
     * @param y Y coordinate
     */
    public Cell(int x, int y){
        this.x = x;
        this.y = y;
        this.isPath = false;
        this.isJunction = false;
    }

    /**
     * method for getting coordinate X
     * @return X coordinate
     */
    public int getX(){
        return this.x;
    }

    /**
     * method for getting coordinate Y
     * @return Y coordinate
     */
    public int getY(){
        return this.y;
    }

    /**
     * method for checking if given cell belongs to Path
     * @return true/false
     */
    public synchronized boolean isPath(){ return this.isPath; }

    /**
     * method for checking if given cell is Junction
     * @return true/false
     */
    public synchronized boolean isJunction(){ return this.isJunction; }

    /**
     * method for marking given cell, that belongs to Path
     */
    public void addToPath(){
        this.isPath = true;
    }

    /**
     * method for marking given cell, that is Junction
     */
    public void setJunction(){
        this.isJunction = true;
    }

    /**
     * method for adding Drawable object into Cell
     * @param obj   Drawable object
     */
    public void addObject(Drawable obj) throws InterruptedException {}

    /**
     * method for removing Drawable object from Cell
     * @param obj   Drawable object
     */
    public synchronized void removeObject(Drawable obj){}

    /**
     * method for getting objects placed on given cell
     * @return collection of Drawable objects
     */
    public synchronized Collection<Drawable> getObjects(){
        return new LinkedList<Drawable>();
    }

    /**
     * Overrided method for generating hashCode
     * @return hashCode
     */
    @Override public int hashCode(){
        int ret = this.y * Map.getN() + this.x;
        return ret;
    }
}
