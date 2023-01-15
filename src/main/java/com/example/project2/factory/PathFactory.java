package com.example.project2.factory;

import com.example.project2.map.Cell;
import com.example.project2.map.Junction;
import com.example.project2.map.Map;
import com.example.project2.map.Path;
import com.example.project2.places.Place;
import javafx.util.Pair;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class PathFactory {
    private static Hashtable<Integer, Junction> junctions = new Hashtable<>();;

    /**
     * method for generating new Path
     * @param c1 starting Cell
     * @param c2 ending Cell
     * @return new Path
     */
    public Path createPath(Cell c1, Cell c2){
        int y = c1.getY();
        int x = c1.getX();

        Place p1 = (Place) c1.getObjects().iterator().next();
        Place p2 = (Place) c2.getObjects().iterator().next();

        Queue<Cell> cellsOnPath = new LinkedList<>();
        cellsOnPath.add(c1);
        c1.addToPath();

        Pair<Integer[], Queue<Cell>> pair = this.linePath(y, x, c2.getY(), false);
        cellsOnPath.addAll(pair.getValue());
        y = pair.getKey()[0];
        x = pair.getKey()[1];
        cellsOnPath.addAll(this.linePath(x, y, c2.getX(), true).getValue());

        return new Path(cellsOnPath, p1, p2);
    }


    private Pair<Integer[], Queue<Cell>> linePath(int curr, int constant, int target, boolean x){
        Queue<Cell> cellsOnPath = new LinkedList<>();
        int n = Map.getN();
        Cell c;


        while (true) {
            if (curr > target)
                curr -= 1;
            else
                curr += 1;

            if (x)
                c = Map.getCell(curr, constant);
            else
                c = Map.getCell(constant, curr);

            int X=c.getX(), Y=c.getY();
            if (c.isPath() &&
                    c.getObjects().size() == 0 &&
                    (
                            (X-1 >= 0 && Y+1 < n && Map.getCell(X-1, Y).isPath() && Map.getCell(X, Y+1).isPath()) ||
                                    (X-1 >= 0 && Y-1 >= 0 && Map.getCell(X-1, Y).isPath() && Map.getCell(X, Y-1).isPath()) ||
                                    (X+1 < n && Y+1 < n && Map.getCell(X+1, Y).isPath() && Map.getCell(X, Y+1).isPath()) ||
                                    (X+1 < n && Y-1 >= 0 && Map.getCell(X+1, Y).isPath() && Map.getCell(X, Y-1).isPath()) ||
                                    (X-1 >= 0 && X+1 < n && Map.getCell(X-1, Y).isPath() && Map.getCell(X+1, Y).isPath() && curr == target && !x) ||
                                    (Y-1 >= 0 && Y+1 < n && Map.getCell(X, Y-1).isPath() && Map.getCell(X, Y+1).isPath() && curr == target && !x)
                    ) &&
                    !junctions.containsKey(c.hashCode())
            ) {
                c = new Junction(c.getX(), c.getY());
                c.addToPath();
                c.setJunction();
                junctions.put(c.hashCode(), (Junction) c);
                Map.getMap().remove(c.hashCode());
                Map.getMap().put(c.hashCode(), c);

                for (var path : Map.getPaths().entrySet()) {
                    if (path.getValue().getCells().contains(c)) {
                        path.getValue().getCells().remove(c.hashCode());
                        path.getValue().addCell(c);
                    }
                }
            }

            c.addToPath();
            cellsOnPath.add(c);

            if (curr == target)
                break;
        }

        Integer[] ret = new Integer[2];
        ret[0] = curr;
        ret[1] = constant;
        return new Pair(ret, cellsOnPath);
    }
}
