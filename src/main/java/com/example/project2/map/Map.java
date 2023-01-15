package com.example.project2.map;

import com.example.project2.GameController;
import com.example.project2.factory.PathFactory;
import com.example.project2.factory.PredatorFactory;
import com.example.project2.factory.PreyFactory;
import com.example.project2.places.Place;

import java.util.*;

public class Map {
    /**
     * side length of the squared map
     */
    private static int n;

    /**
     * cells on the map
     */
    private static Hashtable<Integer, Cell> cells;

    /**
     * paths on the map
     */
    private static Hashtable<Integer, Path> paths;

    /**
     * cells with places on the map
     */
    private static LinkedList<Cell> cellsWithPlaces;

    /**
     * class constructor, generates map NxN built with Cells
     * @param n side length of the squared map
     */
    public Map(int n){
        Map.n = n;
        cells = new Hashtable<>();
        paths = new Hashtable<>();
        cellsWithPlaces = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                NormalCell c = new NormalCell(i, j);
                cells.put(c.hashCode(), c);
            }
        }
    }

    /**
     * method for getting Map's size
     * @return n
     */
    public static int getN(){
        return n;
    }

    /**
     * method for getting all Cells from map
     * @return hashtable with Cell
     */
    public static Hashtable<Integer, Cell> getMap(){
        return cells;
    }

    /**
     * method for getting concrete cell
     * @param x coordinate X of cell
     * @param y coordinate Y of cell
     * @return Cell
     */
    public static Cell getCell(int x, int y){
        return cells.get(y * n + x);
    }

    /**
     * method for adding cell with Place to Map
     * @param cell cell with Place
     */
    public static void addPlace(Cell cell){
        cellsWithPlaces.add(cell);
    }

    /**
     * method for checking if n given cell there can be placed new Place
     * @param x coordinate x
     * @param y coordinate y
     * @return true/false
     */
    public static boolean checkPlaces(int x, int y){
        for(var obj : cellsWithPlaces){
            if(obj.getX() == x ||
                obj.getX() == x-1 ||
                obj.getX() == x+1 ||
                obj.getX() == x-2 ||
                obj.getX() == x+2 ||
                obj.getY() == y ||
                obj.getY() == y-1 ||
                obj.getY() == y+1 ||
                obj.getY() == y-2 ||
                obj.getY() == y+2)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * method for generating Paths between Places
     */
    public void generatePaths(){
        PathFactory pathFactory = new PathFactory();
        ListIterator<Cell> it1 = cellsWithPlaces.listIterator();
        while(it1.hasNext())
        {
            Cell c1 = it1.next();
            ListIterator<Cell> it2 = cellsWithPlaces.listIterator(it1.nextIndex());
            while(it2.hasNext())
            {
                Cell c2 = it2.next();
                Path path = pathFactory.createPath(c1, c2);
                this.addPath(path);
            }
        }
    }

    /**
     * method for adding Path into the Map
     * @param path Path for add
     */
    public void addPath(Path path){
        this.paths.put(path.hashCode(), path);
    }

    /**
     * method for getting all Paths
     * @return Hashtable with Path
     */
    public static Hashtable<Integer, Path> getPaths(){
        return paths;
    }

    /**
     * method for adding Animals onto Map
     */
    public void addAnimals(GameController controller) throws InterruptedException {
        int capacity = 0;
        for(var place : this.cellsWithPlaces){
            Place p = (Place) place.getObjects().iterator().next();
            capacity += p.getCapacity();
        }

        Set<Integer> keys = this.paths.keySet();
        List<Integer> pathsKeys = new ArrayList();
        pathsKeys.addAll(keys);

        PredatorFactory predatorFactory = new PredatorFactory(controller);
        PreyFactory preyFactory = new PreyFactory(controller);

        for (int i = 0; i < capacity*1/3; i++) {
            Collections.shuffle(pathsKeys);
            preyFactory.createPrey(pathsKeys);
            predatorFactory.createPredator();
        }
    }
}
