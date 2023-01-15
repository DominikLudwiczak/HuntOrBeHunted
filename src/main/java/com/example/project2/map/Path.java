package com.example.project2.map;

import com.example.project2.places.Place;

import java.util.Queue;

public class Path {
    /**
     * Cells that creates Path
     */
    private Queue<Cell> cells;

    /**
     * Start place
     */
    private Place start;

    /**
     * End place
     */
    private Place end;

    /**
     * class constructor
     * @param cells Cells that creates Path
     */
    public Path(Queue<Cell> cells, Place start, Place end){
        this.cells = cells;
        this.start = start;
        this.end = end;
    }

    /**
     * method for getting Cells from Path
     * @return collection of Cells
     */
    public Queue<Cell> getCells(){
        return this.cells;
    }

    /**
     * method for adding Cell into Path
     * @param cell
     */
    public void addCell(Cell cell){
        this.cells.add(cell);
    }

    /**
     * method for getting starting Place
     * @return Place
     */
    public Place getStart(){
        return this.start;
    }

    /**
     * method for getting ending Place
     * @return Place
     */
    public Place getEnd(){
        return this.end;
    }

    @Override
    public int hashCode() {
        return (this.start.getName() + " " + this.end.getName()).hashCode();
    }
}
