package com.example.project2.animals;

import com.example.project2.GameController;
import com.example.project2.map.Cell;
import com.example.project2.map.Map;
import com.example.project2.places.Place;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import static java.lang.Thread.sleep;

public class Predator extends Animal{
    private static int predatorsCount = 0;

    /**
     * Predator's current mode
     */
    private String mode;

    /**
     * class constructor
     * @param health Animal's health
     * @param speed Animal's speed
     * @param strength Animal's strength
     * @param spec_name name of Animal's species
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Predator(double health, double speed, double strength, String spec_name, int x, int y, GameController controller) {
        super("Predator "+predatorsCount, "Predator", health, speed, strength, spec_name, x, y, controller);
        predatorsCount++;
    }

    /**
     * overrided method for starting Predator's thread
     */
    @Override
    public void start(){
        new Thread(super.getName()){
            public void run(){
                try {
                    while(true){
                        relax();
                        eat();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }

    /**
     * method for Hunting mode for Predator,
     * kills Prey and changes Predator's mode into relax
     */
    private synchronized void eat() throws InterruptedException{
        this.mode = "Hunt";
        ArrayList<Cell> actions = new ArrayList<>();
        Cell cell;
        Boolean searching;
        Boolean hunted = false;
        Boolean place = false;

        while(true) {
            actions.clear();
            searching = true;
            int x = super.getX();
            int y = super.getY();

            for(int i=Math.max(x-2, 0); i <= Math.min(x+2, Map.getN()-1); i++){
                for(int j=Math.max(y-2, 0); j <= Math.min(y+2, Map.getN()-1); j++){
                    cell = Map.getCell(i, j);
                    if(i==x && j==y){
                        continue;
                    }

                    place = false;
                    Iterator it = cell.getObjects().iterator();
                    while (it.hasNext()) {
                        var obj = it.next();
                        if(obj instanceof Place){
                            place = true;
                            break;
                        }
                        if (obj instanceof Prey) {
                            searching = false;
                            hunted = this.hunt((Prey) obj, x, y);
                            break;
                        }
                    }
                    if(place)
                        continue;
                    if(!searching)
                        break;
                    if(i >= x-1 && i <= x+1 && j >= y-1 && j <= y+1){
                        actions.add(cell);
                    }
                }
                if(!searching)
                    break;
            }

            if(hunted)
                break;

            if(searching) {
                Collections.shuffle(actions);
                var action = actions.get(0);
                action.addObject(this);
                super.updateCoords(action.getX(), action.getY());
                sleep((long) (1100 - super.getSpeed()));
            }
        }
    }

    private Boolean hunt(Prey prey, int x, int y) throws InterruptedException {
        while(!prey.isInPlace() && prey != null){
            if(x == prey.getX() && y == prey.getY()){
                double hitValue = this.getStrength() - prey.getStrength();
                double preyHealth = prey.decreaseHealth(hitValue);
                if(preyHealth == 0){
                    prey.deletePrey();
                    prey = null;
                    return true;
                }
            }

            if(x > prey.getX())
                x--;
            else if(x < prey.getX())
                x++;

            if(y > prey.getY())
                y--;
            else if(y < prey.getY())
                y++;
            if(Map.getCell(x, y).getObjects().stream().anyMatch(o -> Place.class.isAssignableFrom(o.getClass())) || Math.abs(x-prey.getX()) > 2 || Math.abs(y-prey.getY()) > 2)
                return false;
            Map.getCell(x, y).addObject(this);
            super.updateCoords(x, y);
            sleep((long) (1100 - super.getSpeed()));
        }
        return false;
    }

    /**
     * method for relaxing Predator after hunting,
     * after some time changes Predator's mode into hunting
     */
    private synchronized void relax() throws InterruptedException {
        this.mode = "Relax";
        sleep(10000);
    }

    @Override
    public ListView<String> present() {
        ListView<String> properties = super.present();
        properties.getItems().add("Current mode: "+this.mode);
        return properties;
    }
}
