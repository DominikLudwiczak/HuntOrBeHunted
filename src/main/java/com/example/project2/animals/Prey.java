package com.example.project2.animals;

import com.example.project2.GameController;
import com.example.project2.map.Cell;
import com.example.project2.map.Map;
import com.example.project2.map.Path;
import com.example.project2.places.Hideout;
import com.example.project2.places.Place;
import com.example.project2.places.Source;
import javafx.scene.control.ListView;

import java.util.*;

import static java.lang.Thread.sleep;

public class Prey extends Animal{
    private static int preysCount = 0;

    /**
     * animal's water level
     */
    private double water_level;

    /**
     * animal's food level
     */
    private double food_level;

    /**
     * marks if Prey is in some Place
     */
    private Boolean inPlace;

    /**
     * variable for stopping thread
     */
    private Boolean stop;

    /**
     * class constructor
     * @param health Animal's health
     * @param speed Animal's speed
     * @param strength Animal's strength
     * @param spec_name name of Animal's species
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Prey(double health, double speed, double strength, String spec_name, int x, int y, GameController controller) {
        super("Prey "+preysCount, "Prey", health, speed, strength, spec_name, x, y, controller);
        preysCount++;
        this.water_level = 50;
        this.food_level = 50;
        this.inPlace = false;
        this.stop = false;
    }

    /**
     * method for getting stop flag
     * @return stop
     */
    public synchronized Boolean isStopped(){
        return this.stop;
    }

    /**
     * overrided method for starting Prey's thread
     */
    @Override
    public void start(){
        new Thread(super.getName()){
            public void run(){
                Random random = new Random();
                Place hideout = null;
                while(!stop){
                    try {
                        if(water_level <= 15)
                            drink();
                        if(food_level <= 15 && !stop)
                            eat();
                        Cell cell = Map.getCell(Prey.super.getX(), Prey.super.getY());
                        if(cell.getObjects().size() > 0 && !cell.getObjects().iterator().next().getType().equals("Hideout") && !stop){
                            hideout = goToPlace("Hideout");
                        }
                        decreaseFood();
                        decreaseWater();
                        if(random.nextDouble() <= 0.75 && hideout != null && !stop)
                            reproduce((Hideout) hideout, Prey.super.getX(), Prey.super.getY());
                        sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }.start();
    }

    /**
     * method that decreases water level
     */
    private synchronized void decreaseWater(){
        this.water_level -= 0.5;
    }

    /**
     * method that decreases∂ food level
     */
    private synchronized void decreaseFood(){
        this.food_level -= 0.5;
    }

    /**
     * method for resetting water level
     */
    private synchronized void resetWater(){
        this.water_level = 50;
    }

    /**
     * method for resetting food level
     */
    private synchronized void resetFood(){
        this.food_level = 50;
    }

    /**
     * method that moves Prey into food source (Plant),
     * then increases food_level and returns Prey to Hideout
     */
    private void eat() throws InterruptedException {
        Source src = (Source) this.goToPlace("Plant");
        if(src != null) {
            sleep((long) src.getReplenishSpeed());
            this.resetFood();
        }
    }

    /**
     * method that moves Prey into drink source (Water),
     * then increases water_level and returns Prey to Hideout
     */
    private void drink() throws InterruptedException {
        Source src = (Source) this.goToPlace("Water");
        if(src != null) {
            sleep((long) src.getReplenishSpeed());
            this.resetWater();
        }
    }

    /**
     * method that moves Prey to some target Place
     *
     * @param placeTag tag of the target Place
     */
    private Place goToPlace(String placeTag) throws InterruptedException {
        List<Path> pathsWithTarget = new ArrayList<>();
        Cell cell = Map.getCell(super.getX(), super.getY());
        for(var path : Map.getPaths().entrySet()){
            if(path.getValue().getCells().contains(cell) && (path.getValue().getStart().getType().equals(placeTag) || path.getValue().getEnd().getType().equals(placeTag))){
                pathsWithTarget.add(path.getValue());
            }
        }

        Collections.shuffle(pathsWithTarget);
        Iterator<Path> pathIt = pathsWithTarget.iterator();
        if(pathIt.hasNext()) {
            Path path = pathIt.next();

            List<Cell> cells = (List<Cell>) path.getCells();
            if (path.getStart().getType().equals(placeTag)) {
                Collections.reverse(cells);
                path.getEnd().removeAnimal(this);
            } else
                path.getStart().removeAnimal(this);

            ListIterator<Cell> it = cells.listIterator(cells.indexOf(cell));
            cell = it.next();
            this.inPlace = false;
            while (it.hasNext() && !this.stop) {
                cell = it.next();
                cell.addObject(this);
                if (!this.stop)
                    super.updateCoords(cell.getX(), cell.getY());
                sleep((long) (1100 - super.getSpeed()));
                decreaseFood();
                decreaseWater();
            }

            if(!this.stop) {
                this.inPlace = true;
                if (path.getStart().getType().equals(placeTag))
                    return path.getStart();
                else
                    return path.getEnd();
            }
        }
        return null;
    }

    /**
     * method for reproducing Preys, it works only if Prey is in Hideout and there is place for one another Prey
     */
    private synchronized void reproduce(Hideout hideout, int x, int y) throws InterruptedException {
        if(hideout.getCapacity() > hideout.getPreys().size()) {
            Random random = new Random();
            Prey newPrey = new Prey(1 + random.nextDouble() * 999, 1 + random.nextDouble() * 499, 1 + random.nextDouble() * 499, "Prey", x, y, super.controller);
            Cell cell = Map.getCell(x, y);
            cell.addObject(newPrey);
            super.addAnimal(newPrey);
            newPrey.start();
        }
    }

    /**
     * method for getting bool value if Prey is in some Place
     * @return
     */
    public synchronized Boolean isInPlace(){
        return this.inPlace;
    }

    /**
     * method for deleting Prey after kill
     */
    public void deletePrey(){
        this.stop = true;
        int x = super.getX();
        int y = super.getY();
        Cell cell = Map.getCell(x, y);
        cell.removeObject(this);
    }

    @Override
    public ListView<String> present() {
        ListView<String> properties = super.present();
        properties.getItems().add("Water level: "+this.water_level);
        properties.getItems().add("Food level: "+this.food_level);
        return properties;
    }
}
