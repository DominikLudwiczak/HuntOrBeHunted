package com.example.project2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import java.io.IOException;

public class StudentGame extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StudentGame.class.getResource("statistics-view.fxml"));
        Scene statisticsScene = new Scene(fxmlLoader.load());

        GameController gameController = new GameController(fxmlLoader.getController());
        fxmlLoader = new FXMLLoader(StudentGame.class.getResource("game-view.fxml"));
        fxmlLoader.setController(gameController);
        Scene gameScene = new Scene(fxmlLoader.load());

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(gameScene.getRoot(), statisticsScene.getRoot());

        Scene mainScene = new Scene(splitPane);
        mainScene.getStylesheets().add(getClass().getResource("game-style.css").toExternalForm());
        mainScene.getStylesheets().add(getClass().getResource("statistics-style.css").toExternalForm());

        stage.setTitle("Hunt or be hunted");
        stage.setScene(mainScene);
        stage.setFullScreen(true);
        stage.show();
        stage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}