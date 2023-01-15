package com.example.project2.factory;

import com.example.project2.GameController;
import com.example.project2.animals.Prey;
import com.example.project2.map.Cell;
import com.example.project2.map.Map;
import com.example.project2.map.Path;

import java.util.*;

public class PreyFactory {
    private GameController controller;

    public PreyFactory(GameController controller){
        this.controller = controller;
    }

    /**
     * method for generating new Prey and placing it on random path which leads to Hideout
     * @param pathsKeys shuffled path keys
     * @return new Prey
     * @throws InterruptedException
     */
    public Prey createPrey(List<Integer> pathsKeys) throws InterruptedException {
        Random random = new Random();
        Iterator it = pathsKeys.iterator();
        Path path = Map.getPaths().get(it.next().hashCode());
        while(!path.getStart().getType().equals("Hideout") && !path.getEnd().getType().equals("Hideout")){
            path = Map.getPaths().get(it.next().hashCode());
        }

        Queue<Cell> cellsOnPath = path.getCells();
        List<Cell> cells = new ArrayList<>();
        cells.addAll(cellsOnPath);
        Collections.shuffle(cells);
        it = cells.iterator();
        Cell cell = (Cell) it.next();
        while(cell.getObjects().size() > 0){
            cell = (Cell) it.next();
        }

        Prey prey =  new Prey(1+random.nextDouble()*999, 1+random.nextDouble()*499, 1+random.nextDouble()*499, "Prey", cell.getX(), cell.getY(), controller);
        cell.addObject(prey);
        return prey;
    }
}
