package com.example.project2.factory;

import com.example.project2.map.Map;
import com.example.project2.places.Hideout;

import java.util.Random;

public class HideoutFactory{
    private static int hideoutsCount = 0;

    /**
     * method for generating new Hideout and placing it on Map
     * @return new Hideout
     */
    public Hideout createHideout() throws InterruptedException {
        Random random = new Random();
        int n = Map.getN();
        Hideout hideout = new Hideout("Hideout "+hideoutsCount, random.nextInt(n/15)+1);
        hideoutsCount++;

        var cell = Map.getCell(random.nextInt(n), random.nextInt(n));
        while(cell.getObjects().size() > 0 || !Map.checkPlaces(cell.getX(), cell.getY()))
        {
            cell = Map.getCell(random.nextInt(n), random.nextInt(n));
        }

        cell.addObject(hideout);
        Map.addPlace(cell);
        return hideout;
    }
}
