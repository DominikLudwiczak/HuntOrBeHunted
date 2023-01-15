package com.example.project2.factory;

import com.example.project2.map.Map;
import com.example.project2.places.Plant;
import com.example.project2.places.Source;
import com.example.project2.places.Water;

import java.util.Random;

public class SourceFactory {
    private static int plantsCount = 0;
    private static int watersCount = 0;

    /**
     * method for generating new Source and placing it on Map
     * @return new Source
     */
    public Source createSource(String type) throws InterruptedException {
        Random random = new Random();
        int n = Map.getN();
        Source source = null;

        if(type.equals("Plant")) {
            source = new Plant(type + " " + plantsCount, random.nextInt(n / 15) + 1, 1000 + random.nextFloat() * 4999);
            plantsCount++;
        } else {
            source = new Water(type + " " + watersCount, random.nextInt(n / 15) + 1, 1000 + random.nextFloat() * 4999);
            watersCount++;
        }

        var cell = Map.getCell(random.nextInt(n), random.nextInt(n));
        while(cell.getObjects().size() > 0 || !Map.checkPlaces(cell.getX(), cell.getY()))
        {
            cell = Map.getCell(random.nextInt(n), random.nextInt(n));
        }

        cell.addObject(source);
        Map.addPlace(cell);
        return source;
    }
}
