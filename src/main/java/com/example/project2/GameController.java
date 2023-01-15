package com.example.project2;

import com.example.project2.factory.HideoutFactory;
import com.example.project2.factory.PredatorFactory;
import com.example.project2.factory.PreyFactory;
import com.example.project2.animals.Animal;
import com.example.project2.animals.Predator;
import com.example.project2.animals.Prey;
import com.example.project2.factory.SourceFactory;
import com.example.project2.map.Cell;
import com.example.project2.map.Drawable;
import com.example.project2.map.Map;
import com.example.project2.places.Hideout;
import com.example.project2.places.Place;
import com.example.project2.places.Plant;
import com.example.project2.places.Water;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GameController implements Initializable {
    @FXML
    private AnchorPane GameControl = new AnchorPane();
    @FXML
    private Button preyControl = new Button();
    @FXML
    private Button predatorControl = new Button();
    @FXML
    private AnchorPane GamePane = new AnchorPane();
    @FXML
    private Canvas GameMap = new Canvas();
    @FXML
    private Canvas Textures = new Canvas();
    private static int w;
    private StatisticsController statisticController;
    private Image prey;
    private Image predator;

    public GameController(StatisticsController controller){
        statisticController = controller;

        try {
            prey = new Image(new FileInputStream("src/main/java/resources/textures/prey.png"));
            predator = new Image(new FileInputStream("src/main/java/resources/textures/predator.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        preyControl.setOnMouseClicked((MouseEvent event) -> {
            try {
                this.addPrey();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        predatorControl.setOnMouseClicked((MouseEvent event) -> {
            try {
                this.addPredator();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        int n = 60;
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        double screenWidth = Screen.getPrimary().getBounds().getWidth()-20;
        w = (int) (screenHeight / n);
        GameMap.setHeight(screenHeight);
        GameMap.setWidth(screenHeight);
        Textures.setHeight(screenHeight);
        Textures.setWidth(screenHeight);
        GameControl.setMinWidth((screenWidth-screenHeight)/2);
        Map map = new Map(n);
        Random random = new Random();

        Textures.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int x = (int) (Math.round(event.getX())/w);
                int y = (int) (Math.round(event.getY())/w);

                statisticController.viewProperties(x, y);
            }
        });

        try{
            HideoutFactory hideoutFactory = new HideoutFactory();
            for (int i = 0; i < 5; i++) {
                hideoutFactory.createHideout();
            }

            SourceFactory sourceFactory = new SourceFactory();
            for (int i = 0; i < 5; i++) {
                sourceFactory.createSource("Water");
            }

            for (int i = 0; i < 5; i++) {
                sourceFactory.createSource("Plant");
            }

            map.generatePaths();
            map.addAnimals(this);
            Collection<Animal> animals = new LinkedList<>();

            Image water = new Image(new FileInputStream("src/main/java/resources/textures/water.jpg"));
            Image hideout = new Image(new FileInputStream("src/main/java/resources/textures/hideout.png"));
            Image plant = new Image(new FileInputStream("src/main/java/resources/textures/plant.jpg"));

            for (var field : Map.getMap().values()) {
                int x = field.getX();
                int y = field.getY();

                var objects = field.getObjects();
                if(objects.size() > 0)
                {
                    Drawable obj = objects.iterator().next();

                    if(obj instanceof Animal)
                        animals.add((Animal) obj);

                    if(obj instanceof Place){
                        Image texture = null;

                        if (obj instanceof Hideout)
                            texture = hideout;
                        if (obj instanceof Plant)
                            texture = plant;
                        if (obj instanceof Water)
                            texture = water;

                        this.addTexture(texture, x, y, obj);
                    }
                }

                GraphicsContext gc = GameMap.getGraphicsContext2D();
                if(field.isPath())
                    gc.setFill(Paint.valueOf("#AE780C"));
                else
                    gc.setFill(Paint.valueOf("#E1C747"));
                gc.fillRect(x*w, y*w, w, w);
            }

            for(var animal : animals){
                this.addAnimal(animal);
                animal.start();
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void moveAnimal(Animal animal, int old_x, int old_y){
        GraphicsContext gc = Textures.getGraphicsContext2D();

        Cell cell = Map.getCell(old_x, old_y);
        var objects = cell.getObjects();
        if(!objects.stream().anyMatch(o -> Place.class.isAssignableFrom(o.getClass()))) {
            gc.clearRect(old_x * w, old_y * w, w, w);
            for(var obj : objects){
                Image texture = null;
                if (obj instanceof Prey)
                    texture = this.prey;

                if (obj instanceof Predator)
                    texture = this.predator;
                this.addTexture(texture, old_x, old_y, obj);
                break;
            }
        }
        this.addAnimal(animal);
    }

    /**
     * method for adding Animal to map
     * @param animal
     */
    public synchronized void addAnimal(Animal animal){
        Cell cell = Map.getCell(animal.getX(), animal.getY());
        var objects = cell.getObjects();
        if(!objects.stream().anyMatch(o -> Place.class.isAssignableFrom(o.getClass()))) {
            Image texture = null;
            if (animal instanceof Prey)
                texture = this.prey;

            if (animal instanceof Predator)
                texture = this.predator;
            this.addTexture(texture, animal.getX(), animal.getY(), animal);
        }
    }

    private synchronized void addTexture(Image texture, int x, int y, Drawable obj){
        GraphicsContext gc = Textures.getGraphicsContext2D();
        gc.clearRect(x*w, y*w, w, w);
        gc.drawImage(texture, x*w, y*w, w, w);
    }

    /**
     * method for adding new Prey
     */
    @FXML
    private void addPrey() throws InterruptedException {
        PreyFactory preyFactory = new PreyFactory(this);
        Set<Integer> keys = Map.getPaths().keySet();
        List<Integer> pathsKeys = new ArrayList();
        pathsKeys.addAll(keys);
        Prey prey = preyFactory.createPrey(pathsKeys);
        this.addAnimal(prey);
        prey.start();
    }

    /**
     * method for adding new Predator
     */
    @FXML
    private void addPredator() throws InterruptedException {
        PredatorFactory predatorFactory = new PredatorFactory(this);
        Predator predator = predatorFactory.createPredator();
        this.addAnimal(predator);
        predator.start();
    }
}