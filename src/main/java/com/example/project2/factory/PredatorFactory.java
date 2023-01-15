package com.example.project2.factory;

import com.example.project2.GameController;
import com.example.project2.animals.Predator;
import com.example.project2.map.Cell;
import com.example.project2.map.Map;

import java.util.Random;

public class PredatorFactory {
    private GameController controller;

    public PredatorFactory(GameController controller){
        this.controller = controller;
    }

    /**
     * method for generating new Predator and placing it on random cell
     * @return new Predator
     * @throws InterruptedException
     */
    public Predator createPredator() throws InterruptedException {
        Random random = new Random();
        int n = Map.getN();
        int x = random.nextInt(n);
        int y = random.nextInt(n);
        Cell cell = Map.getCell(x, y);
        while(cell.getObjects().size() > 0){
            x = random.nextInt(n);
            y = random.nextInt(n);
            cell = Map.getCell(x, y);
        }
        Predator predator = new Predator(1+random.nextDouble()*999, 1+random.nextDouble()*999, 501+random.nextDouble()*999, "Predator", x, y, this.controller);
        cell.addObject(predator);
        return predator;
    }
}
