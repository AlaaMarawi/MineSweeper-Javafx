/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaFx_mayinTarlasi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author alaam
 */
public class MayinTapplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        
        Parent parent = FXMLLoader.load(getClass().getResource("Game.fxml"));
        
        Scene scene = new Scene(parent);

        stage.setTitle("* MineSweepr Game *");
        stage.setResizable(false);
        stage.getIcons().add(new Image(MayinTapplication.class.getResourceAsStream("mine2.jpg")));
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
